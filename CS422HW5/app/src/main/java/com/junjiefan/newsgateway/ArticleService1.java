package com.junjiefan.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ArticleService1 extends Service {

    private boolean running=true;
    private ArrayList<Article> ArtArr = new ArrayList<>();
    private SampleReceiver sampleReceiver;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        sampleReceiver = new SampleReceiver();

        IntentFilter filter1 = new IntentFilter("GET_ART_REQ");
        registerReceiver(sampleReceiver, filter1);


        //Creating new thread for my service


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (running) {

                    try {

                        if(ArtArr.isEmpty()){

                            Thread.sleep(250);

                        }else{


                            Intent intent = new Intent();
                            intent.setAction("DATA_FROM_SERVICE");
                            intent.putExtra("ArtList", ArtArr);
                            sendBroadcast(intent);
                            ArtArr.clear();

                        }


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
//                    int index = (int) (Math.random() * fruitList.size());
//                    sendFruit(fruitList.get(index));
                }

                // sendMessage("Service Thread Stopped");


                // Log.d(TAG, "run: Ending loop");
            }
        }).start();



        return Service.START_STICKY;
    }

    public void updateArt(ArrayList<Article> artList) {

       //Toast.makeText(getApplicationContext(),artList.size()+" ",Toast.LENGTH_SHORT).show();
        ArtArr.clear();
        ArtArr.addAll(artList);


    }


    class SampleReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action == null)
                return;
            switch (action) {
                case "GET_ART_REQ":

                    if (intent.hasExtra("Source")){

                        String sc = intent.getStringExtra("Source");

                        starAsy(sc);
                       // Toast.makeText(getApplicationContext(),"service recived!!! "+ sc,Toast.LENGTH_SHORT).show();
                    }

                    break;

                default:
                    //Log.d(TAG, "onReceive: Unkown broadcast received");
            }
        }
    }

    private void starAsy(String sc) {

        //Toast.makeText(getApplicationContext(),"starASY!!! "+ sc,Toast.LENGTH_SHORT).show();
        new AsyncArtLoader(this,sc).execute();
    }


    @Override
    public void onDestroy() {

        unregisterReceiver(sampleReceiver);
        running=false;
        super.onDestroy();
    }
}
