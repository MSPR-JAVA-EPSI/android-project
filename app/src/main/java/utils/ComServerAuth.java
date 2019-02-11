package utils;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mspr_java.Authentification;
import com.google.gson.Gson;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import model.DtoOutIdentification;
import model.DtoToken;
import model.Response;
import utils.FullResponseBuilder;


public class ComServerAuth extends AsyncTask {

    private Authentification context;

    public ComServerAuth(Authentification context) {
        this.context = context;
    }

    //-->https://stackoverflow.com/questions/2938502/sending-post-data-in-android
    //request.setHeader("Authorization","Bearer "+token);
    String path;
    Map<String, String> headers;
    String body;
    Response response;


    public void request(String path, Map<String, String> headers, String body) {
        this.path = path;
        this.headers = headers;
        this.body = body;
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
        try {
            super.onPostExecute(o);
            Gson gson = new Gson();
            String token = (gson.fromJson(response.getBody(), DtoToken.class)).getToken();
            context.retourAuth(response.getStatus(), token);
        }catch (Exception e){
            print("Echec d'authentification");
            context.retourAuth(404,"");
        }
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
