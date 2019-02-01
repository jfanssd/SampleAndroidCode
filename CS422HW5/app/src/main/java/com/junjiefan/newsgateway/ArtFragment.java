package com.junjiefan.newsgateway;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.Locale;

@SuppressLint("ValidFragment")
class ArtFragment extends Fragment {


    public ArtFragment() {
        // Required empty public constructor
    }


    public static ArtFragment newInstance(Article at, int index, int max)
    {
        ArtFragment f = new ArtFragment();
        Bundle bdl = new Bundle(1);
        bdl.putSerializable("ART", at);
        bdl.putSerializable("INDEX", index);
        bdl.putSerializable("TOTAL_COUNT", max);
        f.setArguments(bdl);
        return f;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_layout = inflater.inflate(R.layout.fragment_article, container, false);


        final Article currentArt = (Article) getArguments().getSerializable("ART");
        int index = getArguments().getInt("INDEX");
        int total = getArguments().getInt("TOTAL_COUNT");

        TextView title = fragment_layout.findViewById(R.id.textViewTitle);
        title.setText(currentArt.getHeadline());


        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currentArt.getUrl()));
                startActivity(i);

            }
        });


        TextView date = fragment_layout.findViewById(R.id.TextViewDate);
        String rawDate = currentArt.getDate();
        String rawDate1=rawDate.substring(0,10);
        String rawDate2=rawDate.substring(11,19);

        date.setText(rawDate1+"  "+rawDate2);

        TextView auth = fragment_layout.findViewById(R.id.textViewAuth);
        auth.setText(currentArt.getAuthor());

        TextView text = fragment_layout.findViewById(R.id.textViewContent);
        text.setText(currentArt.getText());



        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currentArt.getUrl()));
                startActivity(i);

            }
        });





        TextView pageNum = fragment_layout.findViewById(R.id.textViewP);
        pageNum.setText(String.format(Locale.US, "%d of %d", index, total));

        ImageView imageView = fragment_layout.findViewById(R.id.imageView);

        String imageURL= currentArt.getImageUrl();

        if(imageURL.isEmpty()){
            imageView.setVisibility(View.INVISIBLE);

        }else{


            Picasso.with(getContext()).load(imageURL).resize(900,900).into(imageView);

        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currentArt.getUrl()));
                startActivity(i);

            }
        });




        return fragment_layout;
    }



    public void clickFlag(String name) {

        Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(name));

        Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);

    }






}
