package com.schedulemanager.tommaso.schedulemanager;

import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MySchedule extends ActionBarActivity {

    private WeekView mWeekView;
    private List<WeekViewEvent> weekEventsList;
    private ArrayList[] data;
    private ArrayList<WeekViewEvent> schedule1;
    private ArrayList<WeekViewEvent> schedule2;
    private ArrayList<WeekViewEvent> schedule3;
    private ArrayList<String> schedule4;
    private ArrayList<String> coursenames = new ArrayList<>();
    private ArrayList<String> days = new ArrayList<>();
    private ArrayList<String> times = new ArrayList<>();
    private ArrayList<String> professors = new ArrayList<>();
    private int color = 3;
    private Button logOut;
    Intent logOutIntent;
    Bundle logOutBooleanBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        schedule1 = new ArrayList<>();
        schedule2 = new ArrayList<>();
        schedule3 = new ArrayList<>();
        // Get a reference for the week view in the layout.
        logOut = (Button) findViewById(R.id.button5);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        logOutIntent = new Intent(this,LoginActivity.class);
        logOutBooleanBundle = new Bundle();
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File authData = new File(getFilesDir(), "FavoRama_Authentification_Data.txt");
                Boolean infoDeleted = authData.delete();
                logOutBooleanBundle.putBoolean("infoDeleted", infoDeleted);
                logOutIntent.putExtras(logOutBooleanBundle);
                startActivity(logOutIntent);
            }
        });

// Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

            }
        });

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        mWeekView.setMonthChangeListener(new WeekView.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                try {
                    data = new connectToMySql().execute().get();
                } catch (Exception e) {
                    Log.e("log_tag", "error in connect");
                }
                //courses4 = data[3];
                ArrayList<String> course1 = data[0];
                ArrayList<String> course2 = data[1];
                ArrayList<String> course3 = data[2];
                ArrayList<String> course4 = data[3];
                /*schedule1.add(course1.get(0));
                schedule1.add(course2.get(0));
                schedule1.add(course3.get(0));
                schedule1.add(course4.get(0));
                schedule2.add(course1.get(1));
                schedule2.add(course2.get(1));
                schedule2.add(course3.get(1));
                schedule2.add(course4.get(1));
                schedule3.add(course1.get(2));
                schedule3.add(course2.get(2));
                schedule3.add(course3.get(2));
                schedule3.add(course4.get(2));*/

                /*String[] course21 = course1.get(1).split("/");
                String[] course22 = course2.get(1).split("/");
                String[] course23 = course3.get(1).split("/");
                String[] course24 = course4.get(1).split("/");
                String[] course31 = course1.get(2).split("/");
                String[] course32 = course2.get(2).split("/");
                String[] course33 = course3.get(2).split("/");
                String[] course34 = course4.get(2).split("/");
                String[] course41 = course1.get(3).split("/");
                String[] course42 = course2.get(3).split("/");
                String[] course43 = course3.get(3).split("/");
                String[] course44 = course4.get(3).split("/");*/
                Log.e("log_tag_coursecolumn1", course1.toString());
                for (int x = 0; x < 3; x++) {
                    String[] course11 = course1.get(x).split("/");
                    String[] course12 = course2.get(x).split("/");
                    String[] course13 = course3.get(x).split("/");
                    String[] course14 = course4.get(x).split("/");
                    coursenames.add(course11[0]);
                    coursenames.add(course12[0]);
                    coursenames.add(course13[0]);
                    coursenames.add(course14[0]);
                    days.add(course11[1]);
                    days.add(course12[1]);
                    days.add(course13[1]);
                    days.add(course14[1]);
                    times.add(course11[2]);
                    times.add(course12[2]);
                    times.add(course13[2]);
                    times.add(course14[2]);
                    professors.add(course11[3]);
                    professors.add(course12[3]);
                    professors.add(course13[3]);
                    professors.add(course14[3]);
                }
                Log.e("log_tag_schedule1", schedule1.toString());
                List<WeekViewEvent> events = getEvents(newYear, newMonth, coursenames, days, times, professors);
                return events;
            }
        });

// Set long press listener for events.
        mWeekView.setEventLongPressListener(new WeekView.EventLongPressListener() {
            @Override
            public void onEventLongPress(WeekViewEvent event, RectF eventRect) {

            }
        });

        /*WeekView.MonthChangeListener mMonthChangeListener = new WeekView.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = getEvents(newYear, newMonth);
                return events;
            }
        };*/
    }

    private List<WeekViewEvent> getEvents(int newYear, int newMonth, ArrayList<String> coursenames, ArrayList<String> days, ArrayList<String> times, ArrayList<String> professors) {
        weekEventsList = new ArrayList<WeekViewEvent>();
        for (int x = 0; x < coursenames.size(); x++) {
            for (int y = 0; y < days.get(x).length(); y++) {
                int day = 0;
                if (days.get(x).charAt(y)=='M'){
                    day = 25;
                } else if (days.get(x).charAt(y)=='T'){
                    day = 26;
                } else if (days.get(x).charAt(y)=='W'){
                    day = 27;
                } else if (days.get(x).charAt(y)=='R'){
                    day = 28;
                } else if (days.get(x).charAt(y)=='F'){
                    day = 29;
                }
                WeekViewEvent weekEvent = new WeekViewEvent(0, coursenames.get(x), newYear, newMonth, day, Integer.parseInt(times.get(x).substring(0, times.get(x).indexOf(":"))), 05, newYear, newMonth, day, Integer.parseInt(times.get(x).substring(0, times.get(x).indexOf(":"))) + 1, 55);
                if (x < 4){
                    schedule1.add(weekEvent);
                } else if (x >= 4 && x < 8){
                    schedule2.add(weekEvent);
                } else if (x >= 8 && x < 12){
                    schedule3.add(weekEvent);
                }
                /*if (x == 4 || x == 8) {
                    Log.e("log_tag_colorchange",Integer.toString(color));
                    color++;
                    weekEvent1.setColor(color);
                }*/
                weekEventsList.add(weekEvent);
            }
        }
        return weekEventsList;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login_sched, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings_sched) {
            return true;
        } else if (id == R.id.schedule1) {
            for (int x = 0; x < schedule1.size(); x++){
                schedule2.get(x).setColor(1);
                schedule1.get(x).setColor(0);
                schedule3.get(x).setColor(1);
            }
        } else if (id == R.id.schedule2) {
            for (int x = 0; x < schedule2.size(); x++){
                schedule2.get(x).setColor(0);
                schedule1.get(x).setColor(1);
                schedule3.get(x).setColor(1);
            }
        } else if (id == R.id.schedule3) {
            for (int x = 0; x < schedule2.size(); x++){
                schedule2.get(x).setColor(1);
                schedule1.get(x).setColor(1);
                schedule3.get(x).setColor(0);
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

class connectToMySql extends AsyncTask<List<String>, Void, ArrayList[]> {

    ArrayList<String> course1 = new ArrayList<>();
    ArrayList<String> course2 = new ArrayList<>();
    ArrayList<String> course3 = new ArrayList<>();
    ArrayList<String> course4 = new ArrayList<>();

    @Override
    protected ArrayList[] doInBackground(List<String>... params){
        ArrayList[] data = new ArrayList[]{course1,course2,course3,course4};

        InputStream is = null;
        String result = "";

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://6984cbbd.ngrok.io/ScheduleManager/retrieve_course_data.php");
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (Exception e) {
            Log.e("log_tag_error","Failed to reach retrieve_course_data.php" + e.toString());
        }

        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                sb.append(line + "/n");
            }
            is.close();

            result = sb.toString();
        }
        catch(Exception e){
            Log.e("log_tag2", "Error converting result".toString());
        }

        try{
            Log.e("log_tag_result",result.toString());
            JSONArray jArray = new JSONArray(result);
            Log.e("log_tag_json",jArray.toString());

            for(int i = 0; i < jArray.length(); i++){
                JSONObject json = jArray.getJSONObject(i);
                course1.add(json.getString("course1"));
                course2.add(json.getString("course2"));
                course3.add(json.getString("course3"));
                course4.add(json.getString("course4"));
            }


        }
        catch(Exception e){
            Log.e("log_tag3", "Error Parsing Data: " + e.toString());
        }


        return data;
    }
}