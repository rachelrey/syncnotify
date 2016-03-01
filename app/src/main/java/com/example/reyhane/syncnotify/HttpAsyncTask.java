package com.example.reyhane.syncnotify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by reyhaneh on 3/6/2015.
 */
public class HttpAsyncTask extends AsyncTask<String, Void, String> {
    public static final String TYPE_REGISTER_DATA = "register";
    public static final String TYPE_CALLS_DATA = "calls";
    private Context mCntx;
    private SharedPreferences memory;
    private String postType;
    private String email;
    private String authentication_token;


    HttpAsyncTask(Context mCntx,String postType){
        super();
        this.mCntx = mCntx;
        this.memory = mCntx.getSharedPreferences("CurrentUser",Context.MODE_PRIVATE);
        if (postType == null || postType.isEmpty()){
            this.postType = TYPE_CALLS_DATA;
        }else {
            this.postType = postType;
        }
    }

    @Override
    protected String doInBackground(String...params) {
        if (this.postType.equals(TYPE_CALLS_DATA)){
            PostJson post = new PostJson(params, this.mCntx, PostJson.DATA_TYPE_SINGLE_CALL_LOG);
            return post.getMessage();
        }
        else {
            PostJson post = new PostJson(params, this.mCntx, PostJson.DATA_TYPE_REGISTER);
            return post.getMessage();
        }
    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        switch (this.postType){
            case TYPE_CALLS_DATA:
                Toast.makeText(this.mCntx, "Your missed calls has been sent!" + result, Toast.LENGTH_LONG).show();
                MyCallReceiver.checked_2 = 1;
                break;
            case TYPE_REGISTER_DATA:
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    this.email = jsonObject.getString("email");
                    this.authentication_token = jsonObject.getString("authentication_token");
                    Toast.makeText(this.mCntx, "Data Sent!" +email + authentication_token, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {

                }
                SharedPreferences.Editor editor = memory.edit();
                editor.putString("email",this.email);
                editor.putString("authentication_token",this.authentication_token);
                editor.commit();
                Intent intent = new Intent(this.mCntx, MainActivity.class);
                this.mCntx.startActivity(intent);
                break;
        }
    }
}
