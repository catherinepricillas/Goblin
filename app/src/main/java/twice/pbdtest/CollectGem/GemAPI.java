package twice.pbdtest.CollectGem;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by MaximaXL on 2/25/2017.
 */

public class GemAPI {
    private final String TAG = "GemAPI";
    private final String gemAPILocation = "http://goblin-attack.rmxhaha.tk/gem/get";
    private String mUserToken;
    private String mGemToken;

     public GemAPI(String gemToken, String idToken){
        mUserToken = idToken;
        mGemToken = gemToken;
    }

    public Gem run(){
        HashMap<String,String> postdata = new HashMap<>();
        postdata.put("idToken", mUserToken);
        postdata.put("gemToken", mGemToken);

        String resp = performPostCall(gemAPILocation, postdata);
        Log.v(TAG, resp);

        int count = -1;
        String type = "Error";
        try {
            JSONObject obj = new JSONObject(resp);
            count = obj.getJSONObject("gem").getInt("count");
            type = obj.getJSONObject("gem").getString("type");

            Log.v(TAG, String.valueOf(count));
            Log.v(TAG, type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new Gem(count, type);
    }

    public String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        Log.v(TAG,requestURL);
        try {
            url = new URL(requestURL);
            Log.v(TAG,url.getHost());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            Log.v(TAG, String.valueOf(responseCode));

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
            }
            else {
                response="";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

}
