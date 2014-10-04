package android.blaze.com.tailor.http;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by jonwu on 10/4/14.
 */
public class HttpRequestController {
    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36";
    String charset = "UTF-8";


    public String get(String kBaseEndpoint){
        StringBuffer response = null;
        HttpURLConnection conn = null;
        URL obj;
        try {
            obj = new URL(kBaseEndpoint);
            conn = (HttpURLConnection) obj.openConnection();
            response = processGET(conn);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(conn!=null)
                conn.disconnect();
        }

        //print result
        if(response == null){
            return null;
        }else{
            return response.toString();
        }
    }

    private StringBuffer processGET(HttpURLConnection conn) throws IOException{
        StringBuffer response = null;
        conn.setRequestProperty("Accept-Charset", charset);
        conn.setRequestProperty("User-Agent", USER_AGENT);

        // optional default is GET
        conn.setRequestMethod("GET");
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.connect();
        int responseCode = conn.getResponseCode();
        System.out.println("Response Code (Try): " + responseCode);
        if(responseCode == HttpURLConnection.HTTP_OK){
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        }else {
            throw new IOException();
        }
        return response;

    }


    public String post(String kBaseEndpoint, Map<String, String> data){
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(kBaseEndpoint);

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            for (Map.Entry<String, String> entry : data.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                nameValuePairs.add(new BasicNameValuePair(key, value));
            }
            System.out.println(nameValuePairs);
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            StringBuilder sb=new StringBuilder();
            HttpResponse response = httpclient.execute(httppost);
            InputStream in = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
            Log.d("response in Post method", sb.toString() + "");

            return sb.toString();

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
        return null;
    }


    public void put(String kBaseEndpoint){
        try {
            URL obj = new URL(kBaseEndpoint);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("PUT");
            con.setRequestProperty("Accept-Charset", charset);
            con.setRequestProperty("User-Agent", USER_AGENT);
            int responseCode = 0;
            OutputStreamWriter out = new OutputStreamWriter(
                    con.getOutputStream());
            out.write("Resource content");
            out.close();
            try {
                responseCode = con.getResponseCode();
                System.out.println("Response Code (Try): " + responseCode);

                if(responseCode==401){
                    throw new IOException();
                }
            } catch (IOException e) {
                con = (HttpsURLConnection) obj.openConnection();
                responseCode = con.getResponseCode();
                System.out.println("Response Code (Catch): " + responseCode);
                e.printStackTrace();
            }
            con.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

