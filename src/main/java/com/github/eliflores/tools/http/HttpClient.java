package com.github.eliflores.tools.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;

public class HttpClient {
    private static final int HTTP_OK_STATUS = 200;

    public String sendGetRequest(String url) {
        HttpGet httpGet = new HttpGet(url);
        ResponseHandler<String> responseHandler = getResponseHandler();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
            return httpClient.execute(httpGet, responseHandler);
        } catch (IOException e) {
            throw new UncheckedIOException("Error while sending HTTP Request.", e);
        }
    }

    public String sendPostRequest(String url, String request) {
        HttpPost httpPost = httpPost(url, request);
        ResponseHandler<String> responseHandler = getResponseHandler();
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            return httpClient.execute(httpPost, responseHandler);
        } catch (IOException e) {
            throw new UncheckedIOException("Error while sending HTTP Request.", e);
        }
    }

    private HttpPost httpPost(String url, String request) {
        HttpPost httpPost = new HttpPost(url);
        HttpEntity httpEntity;
        try {
            httpEntity = new StringEntity(request);
        } catch (UnsupportedEncodingException e) {
            throw new UncheckedIOException("Error while sending HTTP Request.", e);
        }
        httpPost.setEntity(httpEntity);
        return httpPost;
    }

    private static ResponseHandler<String> getResponseHandler() {
        return httpResponse -> {
            int responseStatusCode = httpResponse.getStatusLine().getStatusCode();
            String response = null;
            if (responseStatusCode == HTTP_OK_STATUS) {
                HttpEntity httpEntity = httpResponse.getEntity();
                response = httpEntity != null ? EntityUtils.toString(httpEntity) : null;
            }
            return response;
        };
    }
}
