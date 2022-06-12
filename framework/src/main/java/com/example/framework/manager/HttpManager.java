package com.example.framework.manager;

import com.example.framework.utils.SHA1;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpManager {

    private static final String TOKEN_URL = "http://api-cn.ronghub.com/user/getToken.json";
    public static final String CLOUD_KEY = "sfci50a7si2ci";
    public static final String CLOUD_SECRET = "V5JqJCMf048";


    private static volatile HttpManager mInstnce = null;

    private OkHttpClient mOkHttpClient;

    private HttpManager() {
        mOkHttpClient = new OkHttpClient();
    }

    public static HttpManager getInstance() {
        if (mInstnce == null) {
            synchronized (HttpManager.class) {
                if (mInstnce == null) {
                    mInstnce = new HttpManager();
                }
            }
        }
        return mInstnce;
    }

    public String postCloudToken(HashMap<String, String> map) {

        String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String Nonce = String.valueOf(Math.floor(Math.random() * 100000));
        String Signature = SHA1.sha1(CLOUD_SECRET + Nonce + Timestamp);

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(TOKEN_URL)
                .addHeader("Timestamp", Timestamp)
                .addHeader("App-key", CLOUD_KEY)
                .addHeader("Nonce", Nonce)
                .addHeader("Signature", Signature)
                .addHeader("Content-Type", "application/x-wwww-form-urlencoded")
                .post(requestBody)
                .build();
        try {
            return mOkHttpClient.newCall(request).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
