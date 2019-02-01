package com.junjiefan.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> SourseList = new ArrayList<>();
    private ArrayList<String> SidList = new ArrayList<>();
    private ArrayList<String> CateList= new ArrayList<>();
    private ArrayList<Article> AtList = new ArrayList<>();
    private Menu opt_menu;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<Fragment> fragments;
    private MyPageAdapter pageAdapter;
    private ViewPager pager;
    public static int screenWidth, screenHeight;
    private String currentSource;

    private NewsReciver newsReciver;
    static final String DATA_BROADCAST_FROM_SERVICE = "DATA_BROADCAST_FROM_SERVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        currentSource="";


        newsReciver = new NewsReciver();


        Intent intent = new Intent(MainActivity.this, ArticleService1.class);
        startService(intent);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerList = findViewById(R.id.drawer_list);


        // Set up the drawer item click callback method
        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if(SourseList.get(position).equals(currentSource)){


                        }else{

                            setTitle(SourseList.get(position));
                            currentSource=SourseList.get(position);
                            Intent intent = new Intent();
                            intent.setAction("GET_ART_REQ");
                            intent.putExtra("Source", SidList.get(position));
                            sendBroadcast(intent);


                        }


                        //Toast.makeText(getApplicationContext(),"drawer cliked and send broad cast"+SourseList.get(position),Toast.LENGTH_SHORT).show();

                        mDrawerLayout.closeDrawer(mDrawerList);
                    }
                }
        );


        // Create the drawer toggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );


        fragments = new ArrayList<>();

        pageAdapter = new MyPageAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.viewpager);
        pager.setAdapter(pageAdapter);

//        // Load the data
//        if (regionData.isEmpty())
//            new AsyncRegionLoader(this).execute();
//
//
        if(SourseList.isEmpty()){


            new AsyncScourseLoader(this,"all").execute();
        }

    }


//    private void selectItem(int position) {
//
//        pager.setBackground(null);
//        currentSubRegion = subRegionDisplayed.get(position);
//
//        new AsyncSubRegionLoader(this).execute(currentSubRegion);
//
//        mDrawerLayout.closeDrawer(mDrawerList);
//
//    }


    //  2 below to make the drawer-toggle work properly:

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }




    // open the drawer when the toggle is clicked
    // Same method is called when an options menu item is selected.

    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
           // Log.d(TAG, "onOptionsItemSelected: mDrawerToggle " + item);
            return true;
        }

       // setTitle(item.getTitle());

//        subRegionDisplayed.clear();
//        subRegionDisplayed.addAll(regionData.get(item.getTitle()));



        new AsyncScourseLoader(this,item.getTitle().toString()).execute();
        return super.onOptionsItemSelected(item);

    }



    // set up the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        opt_menu = menu;
        return true;
    }

    public void updateSource(ArrayList<String> sourceName,ArrayList<String> sourceId) {

        SourseList.clear();
        SidList.clear();
        SourseList.addAll(sourceName);
        SidList.addAll(sourceId);
        mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_item, SourseList) );
        //Toast.makeText(getApplicationContext(),sourceName.get(0),Toast.LENGTH_SHORT).show();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

    }



    private class MyPageAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of a fragment has been changed.
         * Create a new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragments
            baseId += getCount() + n;
        }

    }


    class NewsReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action == null)
                return;
            switch (action) {
                case "DATA_FROM_SERVICE":

                    if (intent.hasExtra("ArtList"))

                        reDofragments((ArrayList<Article>) intent.getSerializableExtra("ArtList"));

//                    ((TextView) findViewById(R.id.textView)).setText(newFruit.toString());
                    break;

                default:
                    //Log.d(TAG, "onReceive: Unkown broadcast received");
            }
        }
    }

    private void reDofragments(ArrayList<Article> artList) {



       // fragments.clear();
       // Toast.makeText(getApplicationContext(), artList.size()+" getget",Toast.LENGTH_SHORT).show();
        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        fragments.clear();

        for (int i = 0; i < artList.size(); i++) {
            fragments.add(ArtFragment.newInstance(artList.get(i), i+1, artList.size()));
            pageAdapter.notifyChangeInPosition(i);
        }

        mDrawerLayout.setBackgroundColor(Color.WHITE);
        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);





    }


    @Override
    protected void onResume() {

        IntentFilter filter1 = new IntentFilter("DATA_FROM_SERVICE");
        registerReceiver(newsReciver, filter1);

        super.onResume();
    }

    @Override
    protected void onStop() {

        unregisterReceiver(newsReciver);
        super.onStop();
    }

    @Override
    protected void onDestroy() {

        Intent i = new Intent(this,ArticleService1.class);
        stopService(i);

        super.onDestroy();
    }
}
