package com.fw.domain;

import com.fw.utils.Progress;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author yqf
 */
public class SearchTask implements Runnable {
    private SearchEngine searchEngine;
    private int page;
    private String outFilepath;
    private CountDownLatch countDownLatch;


    SearchTask(SearchEngine searchEngine, int page, String outFilepath, CountDownLatch countDownLatch) {
        this.searchEngine = searchEngine;
        this.page = page;
        this.outFilepath = outFilepath;
        this.countDownLatch = countDownLatch;


    }


    @Override
    public void run() {

        try {

            String search = searchEngine.search(this.page);
            Elements elements = parseHtml(search);
            output(outFilepath,elements,page);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            countDownLatch.countDown();

        }


    }

    private Elements parseHtml(String content) {

        Document doc = Jsoup.parse(content);

        return doc.getElementsByClass("result").select("a");
    }

    /**
     * 输出爬去数据
     */
    private void output(String outFilePath, Elements link,int page) {
        String filePath = outFilePath + "/spider";
        String fileName = filePath + "/page" + page + ".txt";
        File folder = new File(filePath);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        } else {
            FileOutputStream out = null;
            OutputStreamWriter outWriter = null;
            BufferedWriter bufWrite = null;
            try {
                out = new FileOutputStream(fileName);
                outWriter = new OutputStreamWriter(out, StandardCharsets.UTF_8);
                bufWrite = new BufferedWriter(outWriter);

                for (Element element : link) {
                    bufWrite.write(element.text() + "\t\n");
                    bufWrite.write(element.attr("href") + "\t\n");
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bufWrite.close();
                    outWriter.close();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

}
