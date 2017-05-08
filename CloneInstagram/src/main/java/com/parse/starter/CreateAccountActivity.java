package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class CreateAccountActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText repassword;
    private GridLayout gridLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ParseUser.logOut();
        firstName        = (EditText)       findViewById(R.id.firstnamefield);
        lastName         = (EditText)       findViewById(R.id.lastnamefield);
        email            = (EditText)       findViewById(R.id.emailfield);
        username         = (EditText)       findViewById(R.id.usernamefield);
        password         = (EditText)       findViewById(R.id.passwordfield);
        repassword       = (EditText)       findViewById(R.id.retypepasswordfield);
        gridLayout       = (GridLayout)     findViewById(R.id.gridlayoutregister);

        gridLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void SignUp(View view){

        if( !(username.getText().toString().equals("")) && !(password.getText().toString().equals(""))) {
            if (password.getText().toString().equals(repassword.getText().toString())) {

                ParseUser newUser = new ParseUser();
                newUser.setUsername(username.getText().toString().replace(" ",""));
                newUser.put("FirstName", firstName.getText().toString().replace(" ", ""));
                newUser.put("LastName", lastName.getText().toString().replace(" ", ""));
                newUser.setPassword(password.getText().toString().replace(" ", ""));
                newUser.setEmail(email.getText().toString().replace(" ", ""));

                newUser.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(CreateAccountActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            startHomeActivity();

                        } else {
                            Toast.makeText(CreateAccountActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Password did not match", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "username and password required", Toast.LENGTH_SHORT).show();
        }
    }
    public void startHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
