package com.fw;

import com.fw.domain.BdSearchEngine;
import com.fw.domain.SearchEngine;
import com.fw.domain.SpiderEasy;


import java.io.*;

public class Main {
    public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, InterruptedException {

        SearchEngine engine = new BdSearchEngine("君正集团");

        new SpiderEasy(
                10,
                engine,
                "/home/yqf/文档",
                1000
        ).start();




    }
}
