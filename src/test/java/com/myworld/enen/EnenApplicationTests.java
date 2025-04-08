package com.myworld.enen;

import com.myworld.enen.Service.BlogService;
import com.myworld.enen.utils.GPTKit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@SpringBootTest
class EnenApplicationTests {
    @Autowired
    private BlogService blogService;
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Test
    void contextLoads() {
        System.out.println("I am test !!! hhh");
    }

//    @Test
//    void testOssUploadFile(){
//        UploadOSSFile.upload("src/main/resources/my-world.jpg","blogs/myworld.jpg",endpoint,accessKeyId,accessKeySecret,bucketName);
//    }
    @Test
    void testGptAns(){
        String s = "{  \"id\": \"chatcmpl-example-code-W92sPaQ8Gd9mfB3YXSxve\",  \"object\": \"chat.completion\",  \"created\": 1702951011,  \"model\": \"gpt-3.5-turbo-0613\",  \"choices\": [    {      \"index\": 0,      \"message\": {        \"role\": \"assistant\",        \"content\": \"I'm sorry, but I'm not sure what you mean by \\\"123.\\\" Could you please provide more context or clarify your question?\"      },      \"logprobs\": null,      \"finish_reason\": \"stop\"    }  ],  \"usage\": {    \"prompt_tokens\": 8,    \"completion_tokens\": 27,    \"total_tokens\": 35  },  \"system_fingerprint\": null}";
//        String s = "{  \"id\": \"chatcmpl-8XOyDuNQXDViOM8pjghwyOcmpLYJQ\",  \"object\": \"chat.completion\",  \"created\": 1702971501,  \"model\": \"gpt-3.5-turbo-0613\",  \"choices\": [    {      \"index\": 0,      \"message\": {        \"role\": \"assistant\",        \"content\": \"当然可以！以下是一篇大约500字的文章：\\n\\n标题：生活中的小幸福\\n\\n人生中有一种东西，我们称之为小幸福。小幸福不需要太多的追求，它们存在于我们平凡的日常生活中。小幸福有时就像一个不经意的微笑，让我们感到温暖和快乐。\\n\\n每天早上醒来，第一缕阳光照射进房间，将它击碎为无数微小的光斑，这是一种小幸福。微风轻拂着窗帘，带来清新的气息，这是一种小幸福。尽管我们常常忽视这样微小的事情，但正是这些小幸福构成了我们生活的美好。\\n\\n走在街上，看到一对小孩手拉手地漫步，忍不住露出了微笑，这也是一种小幸福。听到美妙的音乐旋律，感受到乐曲带来的情感共鸣，这同样是一种小幸福。生活中有太多这样的瞬间，让我们感受到生活的美好和幸福。\\n\\n与亲人团聚是一种最令人感到幸福的时刻。无论是在家庭聚餐时分享美味的食物，还是在假日里与家人一起度过轻松的时光，这些都是小幸福。我们可以相互交流、关心彼此，这种温情和爱意让我们感觉到家的温暖。\\n\\n并非每个人都有机会拥有华丽的住宅或闪亮的名牌车。然而，在拥有一个干净整洁的家和一辆简单可靠的车时，我们也能感到小幸福的存在。这种满足感来自于知足，来自于对生活的热爱和享受。\\n\\n小幸福还表现在学习和工作中。解决一个难题、完成一份工作、收到一份表扬、被同事赞赏，这些都会给我们带来小幸福的喜悦。我们将这些微小的成就累积起来，逐渐实现我们更大的目标。\\n\\n在日常生活中，我们也可以通过帮助他人来获得小幸福。为他人撑起一把伞、送给需要的人一杯热茶、与陌生人微笑致意，这些简单而有力的举动，不仅是小幸福的来源，也是社会和谐相处的基石。\\n\\n小幸福让我们懂得了生活的真谛。\"      },      \"logprobs\": null,      \"finish_reason\": \"length\"    }  ],  \"usage\": {    \"prompt_tokens\": 37,    \"completion_tokens\": 800,    \"total_tokens\": 837  },  \"system_fingerprint\": null}";
//        System.out.println(s);
//        System.out.println("  ********************************  ");
        System.out.println(GPTKit.extractMessageFromJSONResponse(s));
    }
    @Test
    void testXssAttack(){
//        URL url = new URL("http://www.baidu.com");
        browserUrl("http://www.baidu.com");
    }
    public void browserUrl(String url){
        //判断是否支持Desktop扩展,如果支持则进行下一步
        if (Desktop.isDesktopSupported()){
            try {
                URI uri = new URI(url);
                Desktop desktop = Desktop.getDesktop(); //创建desktop对象
                //调用默认浏览器打开指定URL
                desktop.browse(uri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                //如果没有默认浏览器时，将引发下列异常
                e.printStackTrace();
            }
        }
    }
}
