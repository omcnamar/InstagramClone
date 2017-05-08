package com.parse.starter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ListView list;

    private ArrayList<String> users;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Query", "onCreate home activity start");
        setContentView(R.layout.activity_home);

        setTitle("All users");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername() +"");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e == null && objects != null){

                    Log.i("Query","Size : " + objects.size() );

                    for(int i =0; i< objects.size(); i++){

                        users.add(objects.get(i).getUsername()+"");
                        Log.i("Query",objects.get(i).getUsername()+"");
                    }
                    ((BaseAdapter) list.getAdapter()).notifyDataSetChanged();
                    Toast.makeText(HomeActivity.this, "Success", Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(HomeActivity.this, "Failed" + e.toString()+"", Toast.LENGTH_LONG).show();
                }
            }
        });

        list = (ListView)findViewById(R.id.list_of_users);
        users = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, users);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HomeActivity.this, UserFeedActivity.class);
                intent.putExtra("username", users.get(position));
                startActivity(intent);
            }
        });

        Log.i("Query", "onCreate home activity end");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.add_photo: {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        getPhoto();
                    }
                } else {
                    getPhoto();
                }
                return true;
            }
            case R.id.logout: {
                ParseUser.logOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                return true;
            }
            case R.id.backButton:{
                ParseUser.logOut();
                Toast.makeText(HomeActivity.this, "backButton", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
            default: return false;

        }
    }

    public void getPhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == RESULT_OK && data != null){
            Uri selectedImage = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),selectedImage);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                byte[] array = stream.toByteArray();

                ParseFile file = new ParseFile("image.png", array );

                ParseObject object = new ParseObject("Images");
                object.put("image", file);
                object.put("username", ParseUser.getCurrentUser().getUsername());

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            Toast.makeText(HomeActivity.this, "Image saved", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(HomeActivity.this, "Failed to upload", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
