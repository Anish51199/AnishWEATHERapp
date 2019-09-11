package com.example.anishweatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    TextView weatherInfo;
    TextView TempFinal;

    public class DownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... urls) {
            String Result = "";
            URL url;
            HttpURLConnection Connection = null;
            try {
                url = new URL(urls[0]);
                Connection = (HttpURLConnection) url.openConnection();
                InputStream in = Connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    Result += current;
                    data = reader.read();
                }
                return Result;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Couldn't find Weather",Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String WeatherInfo= jsonObject.getString("weather");
                Log.i("Msg",WeatherInfo);

                JSONArray arr= new JSONArray(WeatherInfo);
                String msg="";
                for(int i=0;i<arr.length();i++){
                    JSONObject part=arr.getJSONObject(i);
                    String main =part.getString("main");
                    String description=part.getString("description");
                //   Log.i("MAIN",part.getString("main"));
                 //   Log.i("MAIN",part.getString("description"));
                    if(!main.equals("") && !description.equals("")){
                        msg =main+" : "+description+"\r\n";
                    }
                }
                if(!msg.equals("")){
                    weatherInfo.setText(msg);
                //    Toast.makeText(getApplicationContext(),"Couldn't find Weather",Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Couldn't find Weather",Toast.LENGTH_SHORT).show();
              //  return;
            }
/*
            try {
                JSONObject temp1 = new JSONObject(s);
                String TempInfo=temp1.getString("Value");
                Log.i("msg",TempInfo);
                String TempMsg="";

                JSONArray arr1=new JSONArray(TempInfo);
                for(int i=0;i<arr1.length();i++){
                    JSONObject part2=arr1.getJSONObject(i);
                    String temperature=part2.getString("temp");
                    if(!temperature.equals("")){
                        TempMsg="Temp : "+temperature;
                    }
                }
                if(!TempMsg.equals("")){
                    TempFinal.setText(TempMsg);
                }
            }catch(Exception e){
               e.printStackTrace();
               return;
            } */

        }
    }
    public void ClickOn(View view){
       // editText.getText().toString()
        DownloadTask task= new DownloadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");
       //For keyboard Hiding
        InputMethodManager mgr=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText=findViewById(R.id.editText);
        weatherInfo=findViewById(R.id.weatherView);
        TempFinal=findViewById(R.id.tempFinal);
    }
}
