package utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.Response;

public class FullResponseBuilder {
    public static Response getFullResponse(HttpURLConnection con) throws IOException {
        int status = con.getResponseCode();

        Reader streamReader = null;
        Log.e("Status",""+status);
        if (status > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuilder body = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            body.append(inputLine);
        }

        in.close();

        return new Response(status, body.toString());
    }
}
