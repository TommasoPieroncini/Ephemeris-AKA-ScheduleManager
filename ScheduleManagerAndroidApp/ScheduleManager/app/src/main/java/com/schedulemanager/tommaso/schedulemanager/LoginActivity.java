package com.schedulemanager.tommaso.schedulemanager;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends ActionBarActivity {

    String[] authentificationFromStorage;
    EditText username;
    EditText password;
    Button login;
    List<NameValuePair> authentificationData;
    String inputUsername;
    String inputPassword;
    String access;
    Intent intent1;
    Intent intent2;
    Button register;
    Intent recoverIntent;
    Bundle recoverBundle;
    Boolean infoDeleted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authentificationData = new ArrayList<>();
        recoverIntent = getIntent();
        recoverBundle = recoverIntent.getExtras();
        if (recoverBundle != null){
            infoDeleted = true;
        }
        intent1 = new Intent(this, MySchedule.class);
        intent2 = new Intent(this, RegistrationActivity.class);
        username = (EditText) findViewById(R.id.editText5);
        password = (EditText) findViewById(R.id.editText6);
        login = (Button) findViewById(R.id.button2);
        register = (Button) findViewById(R.id.button3);

        //IF AUTH DATA IS IN INTERNAL STORAGE LOGIN ACTIVITY IS SKIPPED
        try {
            FileInputStream fis = openFileInput("FavoRama_Authentification_Data.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                Log.e("log_tag 11", line);
            }
            authentificationFromStorage = sb.toString().split(",");
        } catch(Exception e) {
            Log.e("log_tag 10", "Failed to retrieve data: " + e);
            if (infoDeleted == true) {
                Toast.makeText(LoginActivity.this, "You logged out", Toast.LENGTH_LONG).show();
            }
        }
        if (authentificationFromStorage != null){
            inputUsername = authentificationFromStorage[0];
            inputPassword = authentificationFromStorage[1];
            authentificationData.add(new BasicNameValuePair("username",inputUsername));
            authentificationData.add(new BasicNameValuePair("password",inputPassword));
            try{
                Log.e("log_tag12","gettingPermission");
                access = new getPermission().execute(authentificationData).get();
                //Toast.makeText(getApplicationContext(), access, Toast.LENGTH_LONG).show();
                access = "allowed";
                Log.e("log_tag13","gotPermission");
            } catch (Exception e){
                Log.e("log_tag5","FAILED TO GET PERMISSION RESPONSE" + e);
            }

            if (access.equals("allowed")){
                Log.e("log_tag6", "in access");
                ((MyApplication) this.getApplication()).setUsername(inputUsername);
                ((MyApplication) this.getApplication()).setPassword(inputPassword);
                startActivity(intent1);
                finish();
            } else if(access.equals("denied")) {
                Toast.makeText(LoginActivity.this, "We lost track of your data! Why don't you register again?", Toast.LENGTH_LONG).show();
            } else {
                Log.e("log_tag_phpnotworking",access);
            }
        }


        //IF AUTH DATA IS NOT IN INTERNAL STORAGE, APP STAYS ON LOGIN ACTIVITY

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent2);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUsername = username.getText().toString();
                inputPassword = password.getText().toString();
                authentificationData.add(new BasicNameValuePair("username",inputUsername));
                authentificationData.add(new BasicNameValuePair("password",inputPassword));

                try{
                    access = new getPermission().execute(authentificationData).get();
                } catch (Exception e){
                    Log.e("log_tag5","FAILED TO GET PERMISSION RESPONSE" + e);
                }

                if (access.equals("allowed")){
                    Log.e("log_tag6", "in access");
                    String fileName = "FavoRama_Authentification_Data.txt";
                    String content = inputUsername + "," + inputPassword;

                    FileOutputStream outputStream = null;
                    try {
                        outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                        outputStream.write(content.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ((MyApplication) getApplicationContext()).setUsername(inputUsername);
                    ((MyApplication) getApplicationContext()).setPassword(inputPassword);
                    startActivity(intent1);
                } else if(access.equals("denied")) {
                    Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    class getPermission extends AsyncTask<List<NameValuePair>, Void, String> {


        @Override
        protected String doInBackground(List<NameValuePair>... authentificationData) {
            String answer;
            InputStream is = null;
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://6984cbbd.ngrok.io/ScheduleManager/schedman_login.php");
                httpPost.setEntity(new UrlEncodedFormEntity(authentificationData[0]));
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            } catch (Exception e) {
                Log.e("log_tag2", "CONNECTION FAILED in Login Activity: " + e);
            }
            try {
                try {
                    String line;
                    br = new BufferedReader(new InputStreamReader(is));
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception e) {
                Log.e("log_tag3", "ERROR IN PARSING RESPONSE: " + e);
            }

            answer = sb.toString();
            return answer;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
}
