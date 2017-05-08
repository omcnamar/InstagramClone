package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class UserFeedActivity extends AppCompatActivity {

    private RecyclerView mBookRecyclerView;
    private Adapter mAdapter;
    private List<Bitmap> list_Of_bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_view);

        Log.i("onCreate", "Start");

        //find a RecyclerView inside the view
        mBookRecyclerView = (RecyclerView)findViewById(R.id.image_recycler_view);
        mBookRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        list_Of_bitmap = new ArrayList<>();


        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        setTitle(username + "'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");
        query.whereEqualTo("username", username);
        query.orderByDescending("CreatedAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size()>0){
                        for(int i = 0; i < objects.size(); i++){
                            ParseFile file = (ParseFile)objects.get(i).get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null && data != null){
                                        Log.i("onCreate", "adding bitmap");
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        list_Of_bitmap.add(bitmap);
                                        updateUI();
                                    }
                                }
                            });

                        }


                    }
                }
            }
        });

        Log.i("onCreate", "end");

    }


    public void updateUI() {

        Log.i("updateUI", "Start");

        if(mAdapter == null) {
            Log.i("updateUI", "Adapter == Null");
            mAdapter = new Adapter(list_Of_bitmap);
            mBookRecyclerView.setAdapter(mAdapter);
        }else{
            Log.i("updateUI", "Adapter not Null");
            mAdapter.setImages(list_Of_bitmap);
            mAdapter.notifyDataSetChanged();
        }
    }

    //Adapter
    private class Adapter extends RecyclerView.Adapter<ImageHolder>{
        private List<Bitmap> images;

        public Adapter(List<Bitmap> im){
            Log.i("Adapter", "constructor");
            images = im;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.i("Adapter", "onCreateViewHolder");
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.activity_user_feed, parent, false);
            return new ImageHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            Log.i("Adapter", "onBindViewHolder");
            Bitmap bitmap = images.get(position);
            holder.bindImage(bitmap);
        }

        @Override
        public int getItemCount() {
            Log.i("Adapter", "getItemCount");
            return images.size();
        }

        public void setImages(List<Bitmap> im){
            Log.i("Adapter", "setImage");
            images = im;
        }
    }

    //ViewHolder
    private class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageView;

        public ImageHolder(View itemView){
            super(itemView);
            Log.i("ImageHolder", "constructor");
            itemView.setOnClickListener(this);

             imageView = (ImageView)itemView.findViewById(R.id.image);
        }

        public void bindImage(Bitmap image){
            Log.i("ImageHolder", "bindImage");
            imageView.setImageBitmap(image);
        }

        public void onClick(View v){
            Log.i("ImageHolder", "onClick");
        }
    }




    /*   private LinearLayout layout;
    private ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);
        layout = (LinearLayout)findViewById(R.id.linearLayout);

        Intent intent = getIntent();

        String username = intent.getStringExtra("username");
        setTitle(username + "'s Feed");

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Images");
        query.whereEqualTo("username", username);
        query.orderByDescending("CreatedAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    if(objects.size()>0){
                        for(ParseObject object: objects){
                            ParseFile file = (ParseFile)object.get("image");
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if(e == null && data != null){
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                                        image = new ImageView(getApplicationContext());
                                        image.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        ));

                                        image.setImageBitmap(bitmap);

                                        layout.addView(image);

                                    }
                                }
                            });
                        }

                    }else {
                        // Add textview 1
                        TextView textView1 = new TextView(getApplicationContext());
                        textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        textView1.setText("User has no Feed");
                        textView1.setTextColor(Color.BLACK);
                        textView1.setPadding(20, 20, 20, 20);//
                        layout.addView(textView1);
                    }
                }
            }
        });
    }*/
}
