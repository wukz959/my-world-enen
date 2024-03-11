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

    private String defaultAns = "本文本为：由于特殊情况使用了默认文本填充回答 (仅数据库可见)";

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
            // 获取用户咨询了多少次
            String count = stringRedisTemplate.opsForValue().get(countKey);
            // 记录问题与相关信息
            ChatRecord chatRecord = new ChatRecord();
            chatRecord.setQuestion(question);
            chatRecord.setAnswer(defaultAns);
            chatRecord.setPublishIp(remoteAddr);
            chatRecord.setCreateTime(TimeUtils.getTimeNow());

            if (count!=null && count.equals(MAX_COUNT_PERSON)) {
                mongoTemplate.save(chatRecord);
                return Result.error(403, "您今天的请求次数已达上限");
            }
            log.info("get question: " + question);
            try {
                ans = gPTService.askQuestion(question);
            } catch (IOException e) {
                log.info("querstion: {},  error: {}", question, e.getMessage());
                mongoTemplate.save(chatRecord);
                return Result.error(500, "服务器好像出问题了哦！！！不如再试试?！");
            }
//            ans = "你好！GPT模型的最大token数量取决于具体的模型架构和预训练过程。在GPT-3这样的较大模型中，最大token数量为2048个。\n但需要注意，进行预测或生成回答时，建议将输入文本限制在模型的最大输入限制范围内，以确保获得最佳的性能和效果。对于GPT-3模型而言，通常建议将输入限制在最大token数量的一半左右，即约1024个tokens。这样做可以避免模型的响应时间过长和性能下降。";
//            ans = "生活不易，全靠演戏\\n\\n生活是一部戏，每个人都在自己的舞台上扮演着不同的角色。有时我们是欢笑的主角，有时又是悲伤的配角。在这个舞台上，我们需要扮演好各种人物，迎接生活中的各种挑战。\\n\\n生活的不易令人感叹，我们时常被现实的压力所困扰。工作上的竞争激烈，学业上的压力重重，人际关系的纷争，物质的追逐等等，都让我们感到疲惫和迷茫。在这个时候，我们需要学会“演戏”。\\n\\n演戏并不是说我们要做伪装和虚假，而是学会扮演不同的角色，去面对和解决生活中的各种问题。就像在舞台上扮演角色一样，我们需要找到自己的定位和方式，展现最好的一面，同时也要注意保护自己的内心，不被困扰所侵蚀。\\n\\n演戏也是一种修炼，我们需要学会换位思考。当我们遇到困境和挫折时，换个角色去看问题，或许就能找到更好的解决方法。用不同的“角色”去看待生活，可以让我们更加客观和理智地面对问题，减轻生活的压力。\\n\\n此外，演戏还可以让我们拥有更强的适应能力和沟通能力。在与人交往的过程中，能够灵活地切换角色，理解他人的立场和需求，将有助于建立良好的人际关系。生活中充满了各种各样的人物，我们需要学会与不同的人相处，处理好彼此之间的关系。\\n\\n生活不易，全靠演戏。只有懂得演戏，我们才能在这个舞台上活得更";
            if (count==null){
                stringRedisTemplate.opsForValue().set(countKey, "1", Duration.ofDays(1));
            }else {
                stringRedisTemplate.opsForValue().increment(countKey);
            }
            //解锁
            stringRedisTemplate.delete(lockKey);
//            // 保存回答
            chatRecord.setAnswer(ans);
            mongoTemplate.save(chatRecord);
        } finally {
            stringRedisTemplate.delete(lockKey);
        }
        //TODO 返回结果是否加上id
        return Result.success().put(ans);
    }

    @GetMapping("/getMsgMaxLength")
    public int getMsgMaxLength(){
        return TEXT_MAX_LENGTH;
    }
}
