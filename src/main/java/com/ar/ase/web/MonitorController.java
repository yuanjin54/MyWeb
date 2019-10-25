package com.ar.ase.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ar.ase.common.Page;
import com.ar.ase.common.Result;
import com.ar.ase.common.util.HttpUtil;
import com.ar.ase.common.util.IPUtils;
import com.ar.ase.common.util.StringUtils;
import com.ar.ase.entity.SpeechMessage;
import com.ar.ase.entity.TextAbstract;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    @GetMapping("/index")
    public String index() {
        return "monitor/massage-index";
    }

    @GetMapping("/abstract-index")
    public String absIndex() {
        return "monitor/abstract-index";
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
}
