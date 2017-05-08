package com.parse.starter;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;


public class MainActivity extends AppCompatActivity implements View.OnKeyListener {

  private EditText userName;
  private EditText password;
  private TextView text;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    userName = (EditText)findViewById(R.id.usernameEmailfield);
    password = (EditText)findViewById(R.id.passwordfield);
    password.setOnKeyListener(this);

    RelativeLayout relativeLayout;
    relativeLayout = (RelativeLayout)findViewById(R.id.topLinearlayout);
    relativeLayout.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
          inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
          e.printStackTrace();
        }
      }
    });

    text     = (TextView)findViewById(R.id.signintext);

    if(ParseUser.getCurrentUser() != null){
      text.setText(ParseUser.getCurrentUser().getUsername());
      startHomeActivity();
    }else{
      text.setText("Sign In");
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

  @Override
  public boolean onKey(View v, int keyCode, KeyEvent event) {

    if(keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN){
      logIn(v);
    }
    return false;
  }

  public void CreateAccount(View view){
    Intent intent = new Intent(MainActivity.this, CreateAccountActivity.class);
    startActivity(intent);
  }

  public void logIn(View view){


    String user = userName.getText().toString().replace(" ","");
    String pass = password.getText().toString();
    if(user.equals("")  || pass.equals("")){
      Toast.makeText(MainActivity.this, "username and password required", Toast.LENGTH_SHORT).show();
    }else {

      ParseUser.logInInBackground(user, pass, new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if (e == null && user != null) {
            Toast.makeText(MainActivity.this, "Log In success", Toast.LENGTH_SHORT).show();
            text.setText(ParseUser.getCurrentUser().getUsername());
            startHomeActivity();
          } else {
            Toast.makeText(MainActivity.this, "Log In failed " + e.getMessage(), Toast.LENGTH_LONG).show();
          }
        }
      });
    }
  }

  public void startHomeActivity(){
    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
    startActivity(intent);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    if(requestCode ==1){
      if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
        Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
      }
    }
  }
}
