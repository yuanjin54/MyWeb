package com.ar.ase.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ar.ase.common.Page;
import com.ar.ase.common.Result;
import com.ar.ase.common.TextObj;
import com.ar.ase.common.util.HttpUtil;
import com.ar.ase.common.util.IPUtils;
import com.ar.ase.common.util.StringUtils;
import com.ar.ase.entity.ChatBot;
import com.ar.ase.entity.SpeechMessage;
import com.ar.ase.entity.TextAbstract;
import com.ar.ase.service.ChatBotService;
import com.ar.ase.service.SpeechMassageService;
import com.ar.ase.service.TextAbstractService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * MonitorController /monitor/index
 *
 * @author yuanjin
 * @date 2019/8/25
 */
@Controller
@Slf4j
@RequestMapping("/monitor")
public class MonitorController {

    @Resource
    private SpeechMassageService speechMassageService;

    @Resource
    private TextAbstractService textAbstractService;

    @Resource
    private ChatBotService chatBotService;

    @GetMapping("/index")
    public String index() {
        return "monitor/massage-index";
    }

    @GetMapping("/abstract-index")
    public String absIndex() {
        return "monitor/abstract-index";
    }

    @GetMapping("/robot-qa")
    public String questionAnswer() {
        return "monitor/robot-qa";
    }

    @GetMapping("/abstract")
    public String abstractText() {
        return "monitor/abstract-extraction";
    }

    @GetMapping("/extraction")
    public String extraction() {
        return "monitor/massage-extraction";
    }

    @RequestMapping("/extract-list")
    @ResponseBody
    public Result extractList(HttpServletRequest request, HttpServletResponse response, String content) {
        Result result = new Result(1, "success");
        String url = "http://39.100.3.165:8668/speaking-extraction";
//        String url = "http://localhost:8668/speaking-extraction";
        System.out.println(request.getParameter("content"));
        String param = "text=" + content;
        System.out.println(content);
        String ipAddress = IPUtils.getIpAddr(request);
        if (StringUtils.isBlank(content)) {
            result.setCode(0);
            result.setMsg("输入为空，请重新输入！");
            return result;
        }
        List<SpeechMessage> list = new ArrayList<>();
        try {
            String responseStr = HttpUtil.sendPost(url, param);
            System.out.println(responseStr);
            Result modelResult = JSONObject.toJavaObject(JSON.parseObject(responseStr), Result.class);
            List data = (List) modelResult.getData();
            if (data != null && data.size() > 0) {
                for (Object obj : data) {
                    List ele = (List) obj;
                    SpeechMessage message = SpeechMessage.builder()
                            .speaker(StringUtils.convertToStr(ele.get(1)))
                            .verb(StringUtils.convertToStr(ele.get(2)))
                            .content(StringUtils.convertToStr(ele.get(3)))
                            .ipAddress(ipAddress)
                            .createTime(new Date())
                            .build();
                    speechMassageService.insert(message);
                    message.setId((Integer) ele.get(0));
                    list.add(message);
                }
            }
            result.setMsg(modelResult.getMsg());
            result.setData(list);
            return result;
        } catch (Exception e) {
            result.setMsg(e.getMessage());
            result.setCode(0);
            return result;
        }
    }


    @RequestMapping("/massage-list")
    @ResponseBody
    public Page list(Page page, SpeechMessage message) {
        PageInfo<SpeechMessage> info = speechMassageService.getMassageListByPage(message, page.getPage(), page.getPageSize());
        Page result = new Page();
        result.setTotal(Integer.parseInt(info.getTotal() + ""));
        result.setRows(info.getList());
        return result;
    }

    @RequestMapping("/abstract")
    @ResponseBody
    public Result abstractList(HttpServletRequest request, HttpServletResponse response, String title, String content) {
        Result result = new Result(1, "success");
        String url = "http://39.100.3.165:8668/abstract-extraction";
        System.out.println(request.getParameter("content"));
        String param = "text=" + content;
        System.out.println(content);
        String ipAddress = IPUtils.getIpAddr(request);
        if (StringUtils.isBlank(content)) {
            result.setCode(0);
            result.setMsg("输入为空，请重新输入！");
            return result;
        }
        try {
            String responseStr = HttpUtil.sendPost(url, param);
            System.out.println(responseStr);
            Result modelResult = JSONObject.toJavaObject(JSON.parseObject(responseStr), Result.class);
            TextAbstract textAbstract = TextAbstract.builder()
                    .title(title)
                    .sourceText(content)
                    .abstractText(StringUtils.convertToStr(modelResult.getData()))
                    .ipAddress(ipAddress)
                    .createTime(new Date())
                    .build();
            if (!"test".equals(content)) {
                textAbstractService.insert(textAbstract);
            }
            result.setData(textAbstract);
            return result;
        } catch (Exception e) {
            result.setCode(0);
            return result;
        }
    }


    @RequestMapping("/abstract-list")
    @ResponseBody
    public Page textAbstract(Page page, TextAbstract textAbstract) {
        PageInfo<TextAbstract> info = textAbstractService.getMassageListByPage(textAbstract, page.getPage(), page.getPageSize());
        Page result = new Page();
        result.setTotal(Integer.parseInt(info.getTotal() + ""));
        result.setRows(info.getList());
        return result;
    }

    @RequestMapping("/robot/qa")
    @ResponseBody
    public Result qa(HttpServletRequest request, HttpServletResponse response, String content) {
        Result result = new Result(1, "success");
        String url = "http://127.0.0.1:8567/message";
        System.out.println(request.getParameter("content"));
        String param = "msg=" + content;
        String ipAddress = IPUtils.getIpAddr(request);
        ChatBot chatBot = ChatBot.builder().ipAddress(ipAddress).createTime(new Date()).build();
        if (!StringUtils.isBlank(getMessage1(content))){
            String ans = getMessage1(content);
            result.setData(ans);
            chatBot.setQuestion(content);
            chatBot.setAnswer(ans);
            chatBotService.insert(chatBot);
            return result;
        }
        try {
            String responseStr = HttpUtil.sendPost(url, param);
            TextObj textObj = JSONObject.toJavaObject(JSON.parseObject(responseStr), TextObj.class);
            System.out.println(textObj.getText());
            result.setData(textObj.getText());
            chatBot.setQuestion(content);
            chatBot.setAnswer(textObj.getText());
            chatBotService.insert(chatBot);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(1);
            result.setData(getMessage2());
            return result;
        }
    }


    private String getMessage1(String question) {
        String[] q1 = {
                "你好",
                "您好",
                "hello",
                "hello~",
                "hi",
                "hi~",
                "您好"
        };
        String[] a1 = {
                "你好",
                "您好",
                "Hello~",
                "Hi~",
                "哈喽，您好",
                "您好，我是帅气的小龙，请多多指教。"
        };
        Random random = new Random();
        if (Arrays.asList(q1).contains(question.toLowerCase())) {
            return a1[random.nextInt(a1.length)];
        }
        return null;
    }

    private String getMessage2() {
        String[] a2 = {
                "亲，你的网络好像有问题呢。",
                "实在抱歉，我的网络好像有问题。",
                "实在抱歉，这个问题我还没学到，我正在努力学习中呢。",
                "这个问题对我太难了，我还没长大呢。",
                "哎呀，我现在还不知道这个问题呢，我的成长是需要一个过程的，希望您理解。",
                "亲，您的这个问题对我太难了~",
                "我不知道你在说什么？",
                "你能再说一遍吗？",
                "麻烦你再说一遍，我耳朵有点不好使！",
                "你是在逗我吗？请说人话吧。",
                "我很无语，能再说一遍不？",
                "等一下哈，网络好像有点问题。"
        };
        Random random = new Random();
        return a2[random.nextInt(a2.length)];
    }

}
