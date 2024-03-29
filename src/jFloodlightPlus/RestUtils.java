package jFloodlightPlus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class RestUtils {
    // -------------------
    // GET Method
    // -------------------

    // base GET method
    public static String doGet(String urlString) throws MalformedURLException,
            IOException, RuntimeException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String temp;
            String result = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            while ((temp = br.readLine()) != null) {
                result = result + temp;
            }

            conn.disconnect();
            return result;
        }
        else if (conn.getResponseCode() == HttpURLConnection.HTTP_NO_CONTENT) {
            // no content but OK, just no response entity
            // for clear static flow entries
            return "";
        }
        else {
            // NOT OK
            throw new RuntimeException("Failed: HTTP error code : "
                    + conn.getResponseCode());
        }
    }

    public static String doGet(String urlString, Map<String, String> paraMap)
            throws MalformedURLException, IOException, RuntimeException {
        return doGet(urlString + "?" + prepareGetParameterString(paraMap));
    }

    private static String prepareGetParameterString(Map<String, String> paraMap) {
        String result = "";
        boolean head = true;

        for (Map.Entry<String, String> entry : paraMap.entrySet()) {
            if (entry.getValue() != null) {
                // prefix
                if (head) {
                    head = false;
                }
                else {
                    result += "&";
                }

                // content append
                result += entry.getKey() + "=" + entry.getValue();
            }
        }

        return result;
    }

    // --------------------
    // POST method
    // --------------------

    public static String doPost(String urlString, String parameterString)
            throws MalformedURLException, IOException, RuntimeException {
        URL url;
        OutputStream os;
        // String parameterString;
        HttpURLConnection conn;

        url = new URL(urlString);
        conn = (HttpURLConnection) url.openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");

        os = conn.getOutputStream();
        os.write(parameterString.getBytes());
        os.flush();

        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            String temp;
            String result = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            while ((temp = br.readLine()) != null) {
                result = result + temp;
            }

            conn.disconnect();
            return result;
        }
        else {
            // NOT OK
            throw new RuntimeException("Failed: HTTP error code : "
                    + conn.getResponseCode());
        }
    }

    // ----------------------
    // DELETE method
    // ----------------------

    // HTTP DELETE with source id in URI version(Normal Delete)
    public static String doDelete(String urlString) throws ClientProtocolException,
            IOException {
        HttpClient hc;
        HttpResponse response;
        HttpEntity responseEntity;
        HttpDelete deleteRequest;

        // construct http client
        hc = new DefaultHttpClient();

        // set request
        deleteRequest = new HttpDelete(urlString);

        // get response
        response = hc.execute(deleteRequest);
        responseEntity = response.getEntity();

        // return content
        if (responseEntity != null)
            return EntityUtils.toString(responseEntity);
        else
            return null;
    }

    // HTTP DELTE with String entity
    public static String doDelete(String urlString, String paraString)
            throws ClientProtocolException, IOException {
        HttpClient hc;
        HttpResponse response;
        HttpEntity responseEntity;
        StringEntity paraStringEntity;
        HttpDeleteWithEntity deleteRequest;

        // construct http client
        hc = new DefaultHttpClient();

        // set request
        paraStringEntity = new StringEntity(paraString);

        deleteRequest = new HttpDeleteWithEntity(urlString);
        deleteRequest.setEntity(paraStringEntity);

        // get response
        response = hc.execute(deleteRequest);
        responseEntity = response.getEntity();

        // return content
        if (responseEntity != null)
            return EntityUtils.toString(responseEntity);
        else
            return null;
    }

    // -------------------
    // PUT method
    // -------------------

    public static String doPut(String urlString, String paraString)
            throws ClientProtocolException, IOException {
        HttpClient hc;
        HttpResponse response;
        HttpEntity responseEntity;
        StringEntity paraStringEntity;
        HttpPut deleteRequest;

        // construct http client
        hc = new DefaultHttpClient();

        // set request
        paraStringEntity = new StringEntity(paraString);

        deleteRequest = new HttpPut(urlString);
        deleteRequest.setEntity(paraStringEntity);

        // get response
        response = hc.execute(deleteRequest);
        responseEntity = response.getEntity();

        // return content
        if (responseEntity != null)
            return EntityUtils.toString(responseEntity);
        else
            return null;
    }
}
