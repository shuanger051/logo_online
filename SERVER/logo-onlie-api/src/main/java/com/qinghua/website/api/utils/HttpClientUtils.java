package com.qinghua.website.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

@Slf4j
public class HttpClientUtils {

    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(150);
        cm.setDefaultMaxPerRoute(100);
        httpClient = HttpClients.custom().setConnectionManager(cm).setConnectionManagerShared(true).build();
    }

    public static String doGet(String url){
        //创建HttpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = null;
        HttpGet get = new HttpGet(url);
        try {
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.81 Safari/537.36");
            response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //返回json格式
                String res = EntityUtils.toString(response.getEntity());
                return res;
            }
        } catch (IOException e) {
            log.error("[Error ] 发送GET请求失败 ：" + e.getMessage());
        }finally {
            try {
                if(null != response){
                    response.close();
                }
            }catch (Exception e){
                log.error("[Error ] 关闭Get请求的res失败:" + e.getMessage());
            }
        }
        return null;
    }

    public static String getJSON(String url) {
        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpGet httpGet = new HttpGet(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("Content-type", "application/json; charset=utf-8");
            httpGet.setHeader("Accept", "application/json");
            response = httpClient.execute(httpGet);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            result = sb.toString();
        } catch (IOException e) {
            log.error("[Error] HttpClient工具类GET请求出错！" + e.getMessage());
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("[Error] HttpClient工具类GET请求出错！" + e.getMessage());
            }
        }
        return result;
    }


    public static String post(String url, String jsonString) {

        CloseableHttpResponse response = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(30000).setSocketTimeout(30000).build();
            httpPost.setConfig(requestConfig);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            httpPost.setHeader("Accept", "application/json");
            if(null != jsonString){
                httpPost.setEntity(new StringEntity(jsonString, Charset.forName("UTF-8")));
            }
            response = httpClient.execute(httpPost);
            HttpEntity httpEntity = response.getEntity();
            if(null != httpEntity){
                result = EntityUtils.toString(httpEntity,"UTF-8");
            }
        } catch (IOException e) {
            log.error("[Error] HttpClient工具类POST请求出错！" + e.getMessage());
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("[Error] HttpClient工具类POST请求出错！" + e.getMessage());
            }
        }
        return result;
    }

}
