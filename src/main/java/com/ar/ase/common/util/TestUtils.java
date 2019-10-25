package com.ar.ase.common.util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import gui.ava.html.image.generator.HtmlImageGenerator;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xhtmlrenderer.util.FSImageWriter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * TestUtils
 *
 * @author yuanjin
 * @date 2019/9/28
 */
public class TestUtils {

    static String HtmlTemplateStr =

            "<style type=\"text/css\">"+

                    "body {background-color: yellow}"+

                    "h1 {background-color: #00ff00}"+

                    "h2 {background-color: transparent}"+

                    "p {background-color: rgb(250,0,255)}"+

                    "p.no2 {background-color: gray; padding: 20px;}"+

                    "</style>"+



                    "<img src=\"http://p1.img.cctvpic.com/photoAlbum/page/performance/img/2018/8/21/1534782617583_424.jpg\" width=\"660\" height=\"380\" alt=\"cctv.com图片\" />"+



                    "<h1>我是h1标题</h1>"+



                    "<div id=\"\" class=\"\">"+

                    "<img src=\"http://115.182.9.166/cportal/photoAlbum/page/performance/img/2017/5/26/1495792113125_553.jpg\" width=\"480\" height=\"300\" alt=\"cportal图片\" />"+

                    "<h1>这是标题 1</h1>"+

                    "<h2>这是标题 2</h2>"+

                    "<p>这是段落</p>"+

                    "<p class=\"no2\">这个段落设置了内边距。</p>"+

                    "<input type=\"button\" value=\"1\" οnclick=\"点我\" />"+

                    "<input type=\"text\" id=\"ww\" name=\"\" value=\"hahahah\"/>"+

                    "</div>"+



                    "<table border=\"1\" background=\"http://www.w3school.com.cn/i/eg_bg_06.gif\">"+

                    "<tr>"+

                    "<th>Month</th>"+

                    "<th>Savings</th>"+

                    "</tr>"+

                    "<tr>"+

                    "<td>January</td>"+

                    "<td><p>这是第一行</p></td>"+

                    "</tr>"+

                    "<tr>"+

                    "<td><img src=\"http://p1.img.cctvpic.com/photoAlbum/page/performance/img/2018/8/21/1534782617583_424.jpg\" width=\"660\" height=\"380\" alt=\"cctv.com图片\" /></td>"+

                    "<td><h1>这是第二行</h1></td>"+

                    "</tr>"+

                    "</table>"+



                    "<p>有序列表：</p>"+

                    "<ol>"+

                    "<li>打开冰箱门</li>"+

                    "<li>把大象放进去</li>"+

                    "<li>关上冰箱门</li>"+

                    "</ol>"+



                    "<p>无序列表：</p>"+

                    "<ul>"+

                    "<li>雪碧</li>"+

                    "<li>可乐</li>"+

                    "<li>凉茶</li>"+

                    "</ul>";

    public static void main(String[] args) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("nj", "");
//        String html = fillHtml(map);
//        try {
//            String htmlPath = "/Users/yuanjin/IdeaProjects/auto_speech_extraction/src/main/java/com/ar/ase/common/util/ft/2.html";
//            String imagePath = "/Users/yuanjin/IdeaProjects/auto_speech_extraction/src/main/java/com/ar/ase/common/util/ft/3.png";
//            changeImage(html, htmlPath, imagePath, 1000, 1000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Map<String, Object> map = new HashMap<>();
        map.put("nj", "");
        String html = fillHtml(map);

        HtmlImageGenerator imageGenerator = new HtmlImageGenerator();

        Dimension dimension = new Dimension(1000, 1000);
        imageGenerator.setSize(dimension);

        //加载html模版
        imageGenerator.loadHtml(html);
        String imagePath = "/Users/yuanjin/IdeaProjects/auto_speech_extraction/src/main/java/com/ar/ase/common/util/ft/kk.png";
        //把html写入到图片
        imageGenerator.saveAsImage(imagePath);
    }

    public static boolean changeImage(String html, String inputFileName, String outputFileName, int width, int height) throws Exception {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inputFileName), "utf-8"));
        bufferedWriter.write(html);
        bufferedWriter.newLine();
        bufferedWriter.flush();
        bufferedWriter.close();
        File f = new File(inputFileName);
        Java2DRenderer renderer = new Java2DRenderer(f, width, height);
        BufferedImage img = renderer.getImage();
        FSImageWriter imageWriter = new FSImageWriter();
        imageWriter.setWriteCompressionQuality(0.9f);
        imageWriter.write(img, outputFileName);
        File imgFile = new File(outputFileName);
        return imgFile.exists();
    }

    public static String fillHtml(Map<String, Object> map) {
        Configuration configuration = new Configuration();
        String filePath = "/Users/yuanjin/IdeaProjects/auto_speech_extraction/src/main/java/com/ar/ase/common/util/ft/";
        String htmlName = "huidan.html";
        String encoding = "UTF-8";
        String value = "";
        try {
            StringWriter out = new StringWriter();
            configuration.setDirectoryForTemplateLoading(new File(filePath));
            Template template = configuration.getTemplate(htmlName, encoding);
            template.setEncoding(encoding);
            template.process(map, out);
            out.flush();
            out.close();
            value = out.getBuffer().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}

















