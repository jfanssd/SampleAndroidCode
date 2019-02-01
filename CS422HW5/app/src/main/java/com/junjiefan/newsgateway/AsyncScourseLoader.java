package com.junjiefan.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class AsyncScourseLoader extends AsyncTask {

    private MainActivity ma;
    private String cate;
    private ArrayList<String> SourceName = new ArrayList<>();
    private ArrayList<String> SourceId = new ArrayList<>();

    private static final String URLL = "https://newsapi.org/v2/sources?country=us&category=";
    private static final String URLR ="&apiKey=5917771bae1b478293de7ca8b8b3c7c9";

    AsyncScourseLoader(MainActivity Main, String ca){

        this.ma=Main;
        if(ca.equals("all")){
            this.cate="";
        }else{

            this.cate=ca;
        }

        SourceName.clear();
        SourceId.clear();

    }


    @Override
    protected Object doInBackground(Object[] objects) {

        Uri dataUri = Uri.parse(URLL+cate+URLR);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();

            JsonReader reader = new JsonReader(new InputStreamReader(is,"UTF-8"));

            readObj(reader);


            reader.close();


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        return null;
    }

    private void readObj(JsonReader reader) throws IOException {

        reader.beginObject();

        while(reader.hasNext()){

            String Ntag = reader.nextName();

            if (Ntag.equals("sources")) {

                readSArr(reader);
            }else {
                reader.skipValue();
            }


        }


        reader.endObject();


    }

    private void readSArr(JsonReader reader) throws IOException {


        reader.beginArray();


        while(reader.hasNext()){

            readSobj(reader);


        }


        reader.endArray();

    }

    private void readSobj(JsonReader reader) throws IOException {


        reader.beginObject();

        while(reader.hasNext()){

            String Ntag = reader.nextName();

            if (Ntag.equals("name")) {

                SourceName.add(reader.nextString());
            }else if(Ntag.equals("id")){

                SourceId.add(reader.nextString());

            }else {
                reader.skipValue();
            }


        }


        reader.endObject();






    }


    @Override
    protected void onPostExecute(Object o) {
        ma.updateSource(SourceName,SourceId);
    }
}
