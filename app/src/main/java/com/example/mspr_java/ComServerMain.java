package com.example.mspr_java;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import model.DtoOutIdentification;
import model.Response;
import utils.FullResponseBuilder;


public class ComServerMain extends AsyncTask {

    private Main_Activity context;

    public ComServerMain(Main_Activity context) {
        this.context = context;
    }

    //-->https://stackoverflow.com/questions/2938502/sending-post-data-in-android
    //request.setHeader("Authorization","Bearer "+token);
    String path;
    Map<String, String> headers;
    String body;
    Response response; //FAIRE UN AFTTERBACKGROUNDMACHIN
    String instruction;


    public void request(String path, Map<String, String> headers, String body, String instruction){
        this.path = path;
        this.headers = headers;
        this.body = body;
        this.instruction = instruction;
        execute(this);
    }
    public HttpURLConnection setRequestHeaders(HttpURLConnection con, Map<String, String> headers) {
        for (String key : headers.keySet()) {
            con.setRequestProperty(key, headers.get(key));
        }
        return con;
    }
    public String generateJson(String image, String id) {
        DtoOutIdentification dto = new DtoOutIdentification(image,id);
        Gson gson = new Gson();
        String payload = gson.toJson(dto);
        return payload;
    }


    @Override
    protected Object doInBackground(Object[] objects) {
        try {
            URL url = new URL("http://shyndard.eu:8080/" + path);
            print("URL :"+ url.toString());
            print("BODY : "+ body);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con = setRequestHeaders(con, headers);
            con.setConnectTimeout(20000);
            con.setReadTimeout(20000);
            con.setDoOutput(true);

            if (body != null) {
                //ERREUR LEVEE A LA LIGNE EN DESSOUS
                OutputStream os = con.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(body);
                osw.flush();
                osw.close();
                os.close();
            }

            con.connect();



            response = FullResponseBuilder.getFullResponse(con);
            print("Response :"+ response.toString());
        }catch(Exception e){
            print("ERREUR :"+e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(instruction.equals("getAll"))
            context.retourComGetAll(response.getStatus(),response.getBody());
    }

    public void print(final String s){
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("LOG --thread",s);
            }
        });
    }
}
