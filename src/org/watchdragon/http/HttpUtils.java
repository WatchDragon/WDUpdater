package org.watchdragon.http;

import javax.xml.ws.http.HTTPException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.*;

/**
 * Created by tt36999 on 2017/6/23.
 */
public class HttpUtils {
    protected String url;
    protected HashMap<String,String> paramsString = new HashMap<>();
    protected Map<String,List<String>> header;
    protected String response;
    protected HttpURLConnection urlConnection;
    protected String paraStr = new String();

    public HttpUtils(String url) throws IOException {
        if(paramsString.size()>0) {
            paraStr += "?";
            Iterator iterator = paramsString.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                paraStr += key + "=" + value + "&";
            }
            paraStr = paraStr.substring(0, paraStr.length() - 1);
            this.url += paraStr;
        }
        this.url = url;
        URL linkUrl = new URL(this.url.trim());
        urlConnection = (HttpURLConnection) linkUrl.openConnection();
        urlConnection.setConnectTimeout(10000);
        urlConnection.setReadTimeout(10000);
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    }

    public void addParaString(String key,Object value){
        paramsString.put(key,value.toString());
    }

    public HttpUtils head() throws IOException {
        urlConnection.setRequestMethod("HEAD");
        int responseCode = urlConnection.getResponseCode();
        if(responseCode>=400){
            throw new HTTPException(responseCode);
        }
        this.header=urlConnection.getHeaderFields();
        return this;
    }

    public HttpUtils get() throws IOException {
        BufferedReader bufferedReader = null;
        StringBuilder result = new StringBuilder();
        try {
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(false);
            urlConnection.connect();
            this.header = urlConnection.getHeaderFields();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
        }
        this.response = result.toString();
        return this;
    }

    public List<String> getHeader(String name){
        List<String> list = this.header.get(name);
        return list;
    }

    public String getResponse(){
        return this.response;
    }

}
