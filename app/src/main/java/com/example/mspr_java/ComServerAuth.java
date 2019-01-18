package com.example.mspr_java;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ComServerAuth {

    private Authentification context;

    public ComServerAuth(Authentification context) {
        this.context = context;
    }

    void post(String url) throws IOException {

        RequestBody formBody = new FormBody.Builder()
                .add("header", context.getString(R.string.headerAuth))
                .add("data", "")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .method("POST", formBody)
                .build();
        OkHttpClient client = new OkHttpClient();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("", "", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                context.retourAuth(null);
            }
        });

    }

}
