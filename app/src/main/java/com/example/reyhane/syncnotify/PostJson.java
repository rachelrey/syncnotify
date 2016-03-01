package com.example.reyhane.syncnotify;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.HttpResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.String;
import java.io.IOException;

public class PostJson {
    public String url = "";

    private SharedPreferences mPreferences;
    private Context myContext;
    private String msg = "";
    private String json = "";


    public static final String DATA_TYPE_REGISTER = "register_data";
    public static final String DATA_TYPE_SINGLE_CALL_LOG = "single_call_log";

//    public static final String CALLS_URL = "https://radiant-meadow-9818.herokuapp.com/calls.json";
//    public static final String REGISTER_URL = "https://radiant-meadow-9818.herokuapp.com/users";
    public static final String CALLS_URL = "http://192.168.128.167:3000/calls.json";
    public static final String REGISTER_URL = "http://192.168.128.167:3000/users";

    PostJson(String[] data ,Context mContext, String dataType)
    {
        this.myContext = mContext;
        mPreferences = mContext.getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        switch (dataType){
            case PostJson.DATA_TYPE_REGISTER:
                this.json = this.returnRegisterDate(data);
                this.url = PostJson.REGISTER_URL;
                break;
            case PostJson.DATA_TYPE_SINGLE_CALL_LOG:
                this.json = this.returnSingleCallLogData(data);
                this.url = PostJson.CALLS_URL;
                break;
        }
        InputStream inputStream = null;
        try {
            HttpParams params = new BasicHttpParams();
            params.setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION,
                    HttpVersion.HTTP_1_1
            );
            HttpClient httpclient = new DefaultHttpClient(params);
            StringEntity se = new StringEntity(this.json);

            HttpPost httpPost = new HttpPost(this.url);
            httpPost.setEntity(se);
            // Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            if(dataType.equals(PostJson.DATA_TYPE_SINGLE_CALL_LOG)){
                httpPost = this.addAuthHeaders(httpPost);
            }
            if(isConnected(mContext)){
                HttpResponse httpResponse = httpclient.execute(httpPost);
                inputStream = httpResponse.getEntity().getContent();

                if(inputStream != null)
                    this.msg =convertInputStreamToString(inputStream);
                else
                    this.msg = "Did not work!";
            }
            else{
                this.msg = "You are not connected to the Internet!";
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
    }

    public String getMessage()
    {
        return this.msg;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();

        return result;
    }

    public boolean isConnected(Context mContext){
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public String returnRegisterDate(String[] data){
        String userEmail = data[0];
        String userPassword = data[1];
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject user = new JSONObject();
            user.accumulate("email", userEmail);
            user.accumulate("password", userPassword);
            jsonObject.accumulate("user", user);
            json = jsonObject.toString();
        }catch (Exception e)
        {}

        return json;
    }

    public String returnSingleCallLogData(String[] data){
        String number = data[0];
        String datetime = data[1];
        try
        {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("number",number);
        jsonObject.accumulate("datetime", datetime);
        jsonObject.accumulate("call_type", "miss");
        json = jsonObject.toString();
        }catch (Exception e)
        {}

        return json;
    }

    private HttpPost addAuthHeaders(HttpPost httpPost){
        String Auth_key = mPreferences.getString("authentication_token", "");
        String email = mPreferences.getString("email", "");
        httpPost.setHeader("X-User-Email", email);
        httpPost.setHeader("X-User-Token", Auth_key);

        return httpPost;
    }
}
