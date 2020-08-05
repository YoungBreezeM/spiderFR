package com.fw.domain;


import com.fw.factory.SearchThreadFactory;
import com.fw.utils.Progress;

import java.util.concurrent.*;

/**
 * @author yqf
 */
public class SpiderEasy {
    private int threadNum;
    private SearchEngine searchEngine;
    private String outFilePath;
    private ThreadPoolExecutor threadPoolExecutor;
    private int total;



    public SpiderEasy(Integer threadNum, SearchEngine searchEngine, String outFilePath,int total) {
        this.threadNum = threadNum;
        this.searchEngine = searchEngine;
        this.outFilePath = outFilePath;
        this.total = total;
        this.threadPoolExecutor = new ThreadPoolExecutor(
                10,
                10,
                5,
                TimeUnit.MICROSECONDS,
                new LinkedBlockingQueue<>(100),
                new SearchThreadFactory("spider")

        );
    }

    public SpiderEasy() {
    }

    public Integer getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }

    public SearchEngine getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(SearchEngine searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getOutFilePath() {
        return outFilePath;
    }

    public void setOutFilePath(String outFilePath) {
        this.outFilePath = outFilePath;
    }

    /**开始爬取*/
    public void start(){
        Progress progress = new Progress(100);
        int pageSize = total/Domain.PAGE_SIZE;
        CountDownLatch countDownLatch = new CountDownLatch(pageSize);

        for (int i = 0; i < pageSize; i++) {
            threadPoolExecutor.execute(
                    new SearchTask(
                            searchEngine,
                            i,
                            outFilePath,
                            countDownLatch
                    )

            );
        }

        //开启任务进度
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(
                ()->{

                    long l = pageSize - countDownLatch.getCount();
                    progress.setSchedule((double)l/pageSize);
                    if(l==pageSize){
                        service.shutdown();
                    }

                },
                0,
                1,
                TimeUnit.SECONDS
        );

        //等待线程结束，关闭线程池
        try {
            countDownLatch.await();
            threadPoolExecutor.shutdown();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
