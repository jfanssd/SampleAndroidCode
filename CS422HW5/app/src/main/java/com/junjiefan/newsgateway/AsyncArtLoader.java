package com.junjiefan.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class AsyncArtLoader extends AsyncTask {

    private ArticleService1 articleService;
    private String source;
    private ArrayList<Article> artList = new ArrayList<>();

    private static final String URLL = "https://newsapi.org/v2/top-headlines?pageSize=10&sources=";
    private static final String URLR ="&apiKey=5917771bae1b478293de7ca8b8b3c7c9";

    private static final String TAG = "AsyncArtLoader";


    public AsyncArtLoader(ArticleService1 service, String sc) {

        this.articleService=service;
        this.source=sc;

       // Toast.makeText(articleService.getApplicationContext(),"do loading!!!!!!!!",Toast.LENGTH_SHORT).show();

    }

    @Override
    protected Object doInBackground(Object[] objects) {

 //      Toast.makeText(articleService.getApplicationContext(),"123213123213",Toast.LENGTH_SHORT).show();


        Uri dataUri = Uri.parse(URLL+source+URLR);

        Log.d(TAG, "doInBackground: "+dataUri.toString());
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
            //Toast.makeText(articleService.getApplicationContext(),Ntag,Toast.LENGTH_SHORT).show();

            if (Ntag.equals("articles")) {

                readArr(reader);

            }else {
                reader.skipValue();
            }


        }


        reader.endObject();


    }
    private void readArr(JsonReader reader) throws IOException {


        reader.beginArray();


        while(reader.hasNext()){

            readAobj(reader);


        }


        reader.endArray();

    }
    private void readAobj(JsonReader reader) throws IOException {

        String headline="";
        String imageUrl="";
        String date="";
        String Author="";
        String text="";
        String url="";

        reader.beginObject();

        while(reader.hasNext()){

            String Ntag = reader.nextName();

            if (Ntag.equals("title") && reader.peek()!=JsonToken.NULL) {

                headline=reader.nextString();
                Log.d(TAG, "readAobj:title " +headline);

            }else if(Ntag.equals("urlToImage") && reader.peek()!=JsonToken.NULL){

                imageUrl=reader.nextString();
                Log.d(TAG, "readAobj:urlToImage "+ imageUrl);

            }else if(Ntag.equals("publishedAt") && reader.peek()!=JsonToken.NULL){

                date=reader.nextString();
                Log.d(TAG, "readAobj:publishedAt "+ date);

            }else if(Ntag.equals("author") && reader.peek()!=JsonToken.NULL){

                Author = reader.nextString();
                Log.d(TAG, "readAobj:author "+ Author);

            }else if(Ntag.equals("description") && reader.peek()!=JsonToken.NULL){

                text=reader.nextString();
                Log.d(TAG, "readAobj:content "+text);
            }else if(Ntag.equals("url") && reader.peek()!=JsonToken.NULL){

                url=reader.nextString();
                Log.d(TAG, "readAobj:url "+url);

            }else {
                reader.skipValue();
            }


        }









        artList.add(new Article(headline,imageUrl,date,Author,text,url));




        reader.endObject();

    }


    @Override
    protected void onPostExecute(Object o) {


        articleService.updateArt(artList);
    }
}
