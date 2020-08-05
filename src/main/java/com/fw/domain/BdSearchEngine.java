package com.fw.domain;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.net.URLEncoder;


/**
 * @author yqf
 */
public class BdSearchEngine implements SearchEngine {

    private String url;
    private String keyword;

    public BdSearchEngine(String keyword) {
        try {
            String key = URLEncoder.encode(keyword, "utf-8");
            this.url = Domain.BAI_DU+key;
            this.keyword = keyword;
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Override
    public String search(Integer page) {
        HttpGet httpGet = new HttpGet(this.url+"&pn="+page*Domain.PAGE_SIZE);


        httpGet.setConfig(
                RequestConfig
                        .custom()
                        .setSocketTimeout(30000)
                        .setConnectTimeout(30000)
                        .build()
        );

        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        String responseStr = null;

        try {
            httpClient = HttpClientBuilder.create().build();
            HttpClientContext context = HttpClientContext.create();
            response = httpClient.execute(httpGet, context);
            int state = response.getStatusLine().getStatusCode();

            if (state != HttpStatus.SC_OK) {
                responseStr = "";
            }

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                responseStr = EntityUtils.toString(entity, "utf-8");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return responseStr;
    }
}
