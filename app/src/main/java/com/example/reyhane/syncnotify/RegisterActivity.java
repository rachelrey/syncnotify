package com.example.reyhane.syncnotify;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class RegisterActivity extends ActionBarActivity {
    private final static String REGISTER_API_ENDPOINT_URL = "http://10.0.2.2:3000/api/v1/registrations";
    private SharedPreferences mPreferences;
    private String mUserEmail;
    private String mUserPassword;
    private String mUserPasswordConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendRegisterData(View view){

            EditText userEmailField = (EditText) findViewById(R.id.userMail);
            mUserEmail = userEmailField.getText().toString();
            EditText userPasswordField = (EditText) findViewById(R.id.userPassword);
            mUserPassword = userPasswordField.getText().toString();
            EditText userPasswordConfirmationField = (EditText) findViewById(R.id.confirmPassword);
            mUserPasswordConfirmation = userPasswordConfirmationField.getText().toString();

            if (mUserEmail.length() == 0 || mUserPassword.length() == 0 || mUserPasswordConfirmation.length() == 0) {
                Toast.makeText(this, "Please complete all the fields",
                        Toast.LENGTH_LONG).show();
            } else {
                if (!mUserPassword.equals(mUserPasswordConfirmation)) {
                    // password doesn't match confirmation
                    Toast.makeText(this, "Your password doesn't match confirmation, check again",
                            Toast.LENGTH_LONG).show();
                } else {
                    // everything is ok!
                    String[] params = new String[2];
                    params[0] = mUserEmail;
                    params[1] = mUserPassword;
                    new HttpAsyncTask(this, HttpAsyncTask.TYPE_REGISTER_DATA).execute(params);
                }
            }
        }
    }

