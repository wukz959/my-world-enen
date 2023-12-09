package com.myworld.enen.controller;

import com.myworld.enen.Service.GPTService;
import com.myworld.enen.bean.ChatRecord;
import com.myworld.enen.utils.Result;
import com.myworld.enen.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Duration;

import static com.myworld.enen.constants.Constants.*;

/**
 * @ClassName GPTController
 * @Author wkz
 * @Date 2023/12/5 10:34
 * @Version 1.0
 */
@Slf4j
@CrossOrigin
@RestController
public class GPTController {

    @Autowired
    private GPTService gPTService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Value("${myGPT.textMaxLength}")
    private int TEXT_MAX_LENGTH;
    @Value(("${myGPT.maxCountPerson}"))
    private String MAX_COUNT_PERSON;

    @PostMapping("/talkToGPT")
    public Result talkToGPT(@RequestBody ChatRecord questionRecord, HttpServletRequest request){
        // cloudflare的ai 网关接受请求的参数不能带有换行符\n
        String question = questionRecord.getQuestion().replaceAll("\n", "  ");
        log.info("get question: {}", question);
        if (question.length() > TEXT_MAX_LENGTH){
            return Result.error(403, "文本长度超过限制");
        }
        String remoteAddr = request.getRemoteAddr();
        String lockKey = remoteAddr + LOCK_KEY;
        String lockValue = Thread.currentThread().getId() + LOCK_VALUE;
        String countKey = remoteAddr + COUNT_KEY;
        //令牌桶限流暂时不写，一切先从简

        //防抖处理
        Boolean lock = stringRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, lockValue, Duration.ofMinutes(1));
        String ans = null;
        try {
            if (lock!=null&&!lock){
                return Result.error(403, "请勿重复点击");
            }
            String count = stringRedisTemplate.opsForValue().get(countKey);
            if (count!=null && count.equals(MAX_COUNT_PERSON)) {
                return Result.error(403, "您今天的请求次数已达上限");
            }
            log.info("get question: " + question);
//            try {
//                ans = gPTService.askQuestion(question);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return Result.error(500, "服务器好像出问题了哦！！！不如再试试?！");
//            }
            ans = "你好！GPT模型的最大token数量取决于具体的模型架构和预训练过程。在GPT-3这样的较大模型中，最大token数量为2048个。\n但需要注意，进行预测或生成回答时，建议将输入文本限制在模型的最大输入限制范围内，以确保获得最佳的性能和效果。对于GPT-3模型而言，通常建议将输入限制在最大token数量的一半左右，即约1024个tokens。这样做可以避免模型的响应时间过长和性能下降。";
            if (count==null){
                stringRedisTemplate.opsForValue().set(countKey, "1", Duration.ofDays(1));
            }else {
                stringRedisTemplate.opsForValue().increment(countKey);
            }
            //解锁
            stringRedisTemplate.delete(lockKey);

            // 保存问题与回答
            ChatRecord chatRecord = new ChatRecord();
            chatRecord.setQuestion(question);
            chatRecord.setAnswer(ans);
            chatRecord.setPublishIp(remoteAddr);
            chatRecord.setCreateTime(TimeUtils.getTimeNow());
            mongoTemplate.save(chatRecord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stringRedisTemplate.delete(lockKey);
        }
        return Result.success().put(ans);
    }

    @GetMapping("/getMsgMaxLength")
    public int getMsgMaxLength(){
        return TEXT_MAX_LENGTH;
    }
}
