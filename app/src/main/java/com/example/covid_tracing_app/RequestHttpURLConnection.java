package com.example.covid_tracing_app;

import android.content.ContentValues;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RequestHttpURLConnection {

    public String request(String _url, JSONObject _params, String _method){

        HttpURLConnection urlConn = null;

        /**
         * 1. StringBuffer에 파라미터 연결
         * */
        /*
        // URL 뒤에 붙여서 보낼 파라미터.
        StringBuffer sbParams = new StringBuffer();

        // 보낼 데이터가 없으면 파라미터를 비운다.
        if (_params == null)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
            String param = _params.toString();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(param);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            ArrayList<Object> keyList = new ArrayList<Object>();
            JSONArray order = null;

            try {
                order = jsonArray.getJSONArray(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < _params.length(); i++) {
                //key 취득
                Iterator<String> keys = null;
                try {
                    keys = order.getJSONObject(i).keys();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //key를 리스트에 저장
                do {
                    String key = String.valueOf(keys.next());
                    keyList.add(key);
                } while (keys.hasNext());

                JSONObject jsonObject = null;
                try {
                    jsonObject = order.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                boolean isAnd = false;
                // 파라미터 키와 값.
                for (int j = 0; j < keyList.size(); j++) {
                    String strKey = keyList.get(j).toString();
                    String value = null;
                    try {
                        value = jsonObject.getString(strKey);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (isAnd)
                        sbParams.append("&");

                    sbParams.append(strKey).append("=").append(value);

                    // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                    if (!isAnd)
                        if (keyList.size() >= 2)
                            isAnd = true;
                }
            }

        }
        */



        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         * */
        try {
            String requrl = _url;
            /*
            if (_method=="GET"){
                requrl=requrl+"?"+sbParams;
            }
            */
            URL url = new URL(requrl);
            urlConn = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정.
            urlConn.setReadTimeout(10000);
            urlConn.setConnectTimeout(15000);
            urlConn.setRequestMethod(_method);
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setRequestProperty("Accept-Charset", "utf-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("Content-Type", "application/json");
            // [2-2]. parameter 전달 및 데이터 읽어오기.

            if(_method=="POST"){
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(urlConn.getOutputStream()));
                pw.println(_params);
                pw.flush(); // 출력 스트림을 flush. 버퍼링 된 모든 출력 바이트를 강제 실행.
                pw.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.
            }

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            //if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
            //    return null;

            String tmp = urlConn.getRequestMethod();

            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null) {
                page += line;
            }

            try {
                JSONObject jsonObject = new JSONObject(page);
                page = jsonObject.getString("msg");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return tmp+_method+page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }
        return null;
    }

}
