package com.beginner.guesscelebrity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends Activity {
    EditText cityname;
    TextView resultText;

    public class DownloadTask extends AsyncTask<String, Void, String> {
        String data = "";
        String result = "";




        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
               return data;



            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;


        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
            JSONObject obj=new JSONObject(s);



            JSONArray jsonArray =obj.getJSONArray("weather");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                JSONObject tempobject=(JSONObject)obj.getJSONObject("main");
                long high= tempobject.getLong("temp_max");
                long low= tempobject.getLong("temp_min");
//                Log.i("high",Long.toString(high));
//                Log.i("low",Long.toString(low));

                String dataS = jsonObject.getString("main");
                String dataT=  jsonObject.getString("description");
                if(dataS!=""&&dataT!=""){
                    result += dataS+": "+dataT+" "+"Max:"+Long.toString(high)+" "+"Min:"+Long.toString(low)+"\r\n";}
            }
            if(result!=""){
                resultText.setText(result);
            }else{
                Toast.makeText(getApplicationContext(),"Error connecting to Server",Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
                Toast.makeText(getApplicationContext(),"Weather not found",Toast.LENGTH_LONG).show();
            }
        }
    }
    public void findWeather(View view){
        try {
            String encodedCityname= URLEncoder.encode(cityname.getText().toString(),"UTF-8");
            DownloadTask task = new DownloadTask();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedCityname+"&units=metric&APPID=da2d87f243c3a86e0a2cc17e56c15dbe");
            InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(cityname.getWindowToken(),0);
        } catch (Exception e) {

            Toast.makeText(getApplicationContext(),"Weather not found",Toast.LENGTH_LONG).show();
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityname=(EditText)findViewById(R.id.editText);
        resultText=(TextView)findViewById(R.id.textView2);




    }
}







//        String[] splitResult=result.split("<div class=\"tm-padded-small uk-margin-small-bottom\">");
//        splitResult=splitResult[1].split("<li class=\"tm-prev tm-disabled uk-hidden-small\">");
//        Pattern p= Pattern.compile("img src=\"(.*?)\"");
//        Matcher m=p.matcher(splitResult[0]);
//        while(m.find()){
//        Log.i("msg",(m.group(1)));
//
//        p=Pattern.compile("alt=\"(.*?)\"");
//        m=p.matcher(splitResult[0]);
//        while(m.find()){
//        Log.i("msg",(m.group(1)));
//
//        Log.i("Contents Of URL", result);