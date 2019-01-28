package com.example.mspr_java;

import java.io.IOException;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import model.DtoOutIdentification;
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

    //-->https://stackoverflow.com/questions/2938502/sending-post-data-in-android
    void post(String url, String image, String id) throws IOException {
        String payload = generateJson(image,id);
        RequestBody formBody = new FormBody.Builder()
                .add("header", context.getString(R.string.headerAuth))
                .add("body", payload) // --data '{ "image": "image en base 64", "identifier": "identifiant string" }'
                .build();

        Request request = new Request.Builder()
                .url(url)
                .method("POST", formBody)
                .build();
        OkHttpClient client = new OkHttpClient();
        Log.e("Payload",request.toString() +"\n BODY : "+formBody.toString()+" \nVALUE 1: "+((FormBody) formBody).value(1));


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("ONFAILLURE STARTED :",""+ e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("RESPONSE",""+response.message());
                context.retourAuth(response);
            }
        });

    }

    private String generateJson(String image, String id) {
        DtoOutIdentification dto = new DtoOutIdentification(image,id);
        Gson gson = new Gson();
        String payload = gson.toJson(dto);
        return payload;
    }

}
