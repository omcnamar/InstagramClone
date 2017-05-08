
package com.parse.starter;

import android.app.Application;
import android.util.Log;


import com.parse.Parse;
import com.parse.ParseACL;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);
    Log.i("Query", "initialize start");

    // Add your initialization code here
    Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
            .applicationId("17b16232feded5a934fc7eef9fe8c68774b3a273")
            .clientKey("a27a972212b575c584ad944e9122f4ac0d738a14")
            .server("http://ec2-35-166-219-122.us-west-2.compute.amazonaws.com:80/parse/")
            .build()
    );
    Log.i("Query", "initialize end");

    //ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    defaultACL.setPublicWriteAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

  }
}
