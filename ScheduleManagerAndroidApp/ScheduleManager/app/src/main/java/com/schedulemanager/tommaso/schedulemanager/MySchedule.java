package com.schedulemanager.tommaso.schedulemanager;

import android.app.Application;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MySchedule extends ActionBarActivity {


    String[] currentDate = new String[5];
    private Integer start = 0;
    private WeekView mWeekView;
    private List<WeekViewEvent> weekEventsList;
    private ArrayList<String[]> data;
    private ArrayList<String> coursenames = new ArrayList<>();
    private ArrayList<String> days  = new ArrayList<>();
    private ArrayList<String> times  = new ArrayList<>();
    private ArrayList<String> professors = new ArrayList<>();
    /*String[] schedule1; String[] schedule11;
    String[] schedule2; String[] schedule12;
    String[] schedule3; String[] schedule13;
    String[] schedule4; String[] schedule14;
    String[] schedule5; String[] schedule15;
    String[] schedule6; String[] schedule16;
    String[] schedule7; String[] schedule17;
    String[] schedule8; String[] schedule18;
    String[] schedule9; String[] schedule19;
    String[] schedule10; String[] schedule20;*/
    private int color = 3;
    private Button logOut;
    Intent logOutIntent;
    Bundle logOutBooleanBundle;
    int numSchedules = 0;
    int numCourses = 0;
    MyApplication myapp = new MyApplication();
    //Intent intent1 = new Intent(this, MySchedule.class);
    int scheduleVar;
    Calendar cal = Calendar.getInstance();
    private int daysPerMonth;
    private int monthChangeCounter = 0;
    private int firstMonday = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_schedule);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Get a reference for the week view in the layout.
        logOut = (Button) findViewById(R.id.button5);
        mWeekView = (WeekView) findViewById(R.id.weekView);
        setTitle("MySchedules");
        logOutIntent = new Intent(this,LoginActivity.class);
        logOutBooleanBundle = new Bundle();
        mWeekView.setNumberOfVisibleDays(5);
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
        Log.e("current time", cal.getTime().toString());

// Set an action when any event is clicked.
        mWeekView.setOnEventClickListener(new WeekView.EventClickListener() {
            @Override
            public void onEventClick(WeekViewEvent event, RectF eventRect) {

            }
        });

        mWeekView.setEmptyViewClickListener(new WeekView.EmptyViewClickListener() {
            @Override
            public void onEmptyViewClicked(Calendar time) {
                Log.e("emptyview",time.toString());
                //WeekViewEvent event = new WeekViewEvent("My Ass",time, );

            }
        });

// The week view has infinite scrolling horizontally. We have to provide the events of a
// month every time the month changes on the week view.
        /*MonthLoader.MonthChangeListener mMonthChangeListener = new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                List<WeekViewEvent> events = getEvents(newYear, newMonth);
                return events;
            }
        };*/

        mWeekView.setMonthChangeListener(new MonthLoader.MonthChangeListener() {
            @Override
            public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
                // Populate the week view with some events.
                try {
                    data = new connectToMySql().execute().get();
                } catch (Exception e) {
                    Log.e("log_tag", "error in connect");
                }
                /*schedule1 = data[0]; schedule6 = data[5]; schedule11 = data[10]; schedule16 = data[15];
                schedule2 = data[1]; schedule7 = data[6]; schedule12 = data[11]; schedule17 = data[16];
                schedule3 = data[2]; schedule8 = data[7]; schedule13 = data[12]; schedule18 = data[17];
                schedule4 = data[3]; schedule9 = data[8]; schedule14 = data[13]; schedule19 = data[18];
                schedule5 = data[4]; schedule10 = data[9]; schedule15 = data[14]; schedule20 = data[19];*/

                for (int x = 0; x < data.size(); x++){
                    if(data.get(x)[0] != null){
                        numSchedules = numSchedules + 1;
                    }
                }


                for (int x = 0; x < data.get(0).length; x++){
                    //Log.e("log_proof",data.get(0)[x]);
                    if (data.get(0)[x].length() > 6) {
                        numCourses = numCourses + 1;
                    }
                }

                if (start == 1){
                    numCourses = myapp.getNumCourses();
                    numSchedules = myapp.getNumSchedules();
                } else if (start == 0){
                    myapp.setNumCourses(numCourses);
                    myapp.setNumSchedules(numSchedules);
                }

                start = 1;

                Log.e("numSchedules", String.valueOf(numSchedules));
                Log.e("numCourses", String.valueOf(numCourses));
                Log.e("monthCounter",String.valueOf(monthChangeCounter));

                for (int x = 0; x < numSchedules; x++) {
                    for (int y = 0; y < numCourses; y++) {
                        String[] singleCourse = data.get(x)[y].split("/");
                        //Log.e("singleCourse", singleCourse[0].toString());
                        coursenames.add(singleCourse[0]);
                        days.add(singleCourse[1]);
                        times.add(singleCourse[2]);
                        professors.add(singleCourse[3]);
                    }
                }
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
        scheduleVar = ((MyApplication) getApplicationContext()).getSchedVar();
        if (newYear < 2016 || scheduleVar == -1) {
            Log.e("nothing", "month12happened/initialLoad");
            return weekEventsList;
        } else {
            if (newMonth == 2) {
                daysPerMonth = 29;
            } else if (newMonth == 4 || newMonth == 6 || newMonth == 9 || newMonth == 11) {
                daysPerMonth = 30;
            } else {
                daysPerMonth = 31;
            }
            weekEventsList = new ArrayList<WeekViewEvent>();
            int start = 0;
            int end = coursenames.size() / numSchedules;
            Log.e("schedVar", String.valueOf(scheduleVar));
            if (scheduleVar != -1 && scheduleVar <= numSchedules) {
                start = numCourses * scheduleVar;
                end = start + numCourses;
                setTitle("Schedule_" + String.valueOf(scheduleVar + 1));
            } else if (scheduleVar > numSchedules) {
                Toast.makeText(getApplicationContext(), "You don't have these many possible schedules!", Toast.LENGTH_LONG).show();
            }
            Log.e("start", String.valueOf(start));
            Log.e("end", String.valueOf(end));
            currentDate = cal.getTime().toString().split(" ");
            int currentDay = Integer.valueOf(currentDate[2]);
            Log.e("month", String.valueOf(newMonth));
            //TODO: how to make events repeated depending on week day, not on day number?
            Log.e("firstMonday", String.valueOf(firstMonday));
            for (int x = start; x < end; x++) {
                Log.e("dayline", days.get(x).toString());
                for (int z = 0; z < 4; z++) {
                    for (int y = 0; y < days.get(x).length(); y++) {
                        int day = 0;
                        if (days.get(x).charAt(y) == 'M') {
                            day = firstMonday + (7 * z);
                            /*if (day > 22 && daysPerMonth == 29) {
                                newFirstMonday = day + 7 - daysPerMonth;
                            } else if (day > 23 && daysPerMonth == 30) {
                                newFirstMonday = day + 7 - daysPerMonth;
                            } else if (day > 24 && daysPerMonth == 31) {
                                newFirstMonday = day + 7 - daysPerMonth;
                            }*/
                        } else if (days.get(x).charAt(y) == 'T') {
                            day = firstMonday + 1 + (7 * z);
                        } else if (days.get(x).charAt(y) == 'W') {
                            day = firstMonday + 2 + (7 * z);
                        } else if (days.get(x).charAt(y) == 'R') {
                            day = firstMonday + 3 + (7 * z);
                        } else if (days.get(x).charAt(y) == 'F') {
                            day = firstMonday + 4 + (7 * z);
                        }
                        Log.e("day", String.valueOf(day));
                        //Log.e("day", String.valueOf(day));
                        WeekViewEvent weekEvent = new WeekViewEvent(0, coursenames.get(x) + "\n" + professors.get(x) + "\n" + times.get(x), newYear, newMonth, day, Integer.parseInt(times.get(x).substring(0, times.get(x).indexOf(":"))), 05, newYear, newMonth, day, Integer.parseInt(times.get(x).substring(0, times.get(x).indexOf(":"))), 55);
                    /*if (x == 4 || x == 8) {
                        Log.e("log_tag_colorchange",Integer.toString(color));
                        color++;
                        weekEvent1.setColor(color);
                    }*/
                        weekEventsList.add(weekEvent);
                    }
                }
            }
            firstMonday = firstMonday + 28 - daysPerMonth;
            monthChangeCounter = monthChangeCounter + 1;
            return weekEventsList;

        }
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
            scheduleVar = 0;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule2) {
            scheduleVar = 1;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule3) {
            scheduleVar = 2;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule4) {
            scheduleVar = 3;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule5) {
            scheduleVar = 4;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule6) {
            scheduleVar = 5;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule7) {
            scheduleVar = 6;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule8) {
            scheduleVar = 7;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule9) {
            scheduleVar = 8;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule10) {
            scheduleVar = 9;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule11) {
            scheduleVar = 10;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule12) {
            scheduleVar = 11;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule13) {
            scheduleVar = 12;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule14) {
            scheduleVar = 13;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule15) {
            scheduleVar = 14;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule16) {
            scheduleVar = 15;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule17) {
            scheduleVar = 16;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule18) {
            scheduleVar = 17;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule19) {
            scheduleVar = 18;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        } else if (id == R.id.schedule20) {
            scheduleVar = 19;
            ((MyApplication) getApplicationContext()).setSchedVar(scheduleVar);
            finish();
            startActivity(getIntent());
        }


        return super.onOptionsItemSelected(item);
    }
}

class connectToMySql extends AsyncTask<List<String>, Void, ArrayList<String[]>> {

    String[] schedule1 = new String[9]; String[] schedule11 = new String[9];
    String[] schedule2 = new String[9]; String[] schedule12 = new String[9];
    String[] schedule3 = new String[9]; String[] schedule13 = new String[9];
    String[] schedule4 = new String[9]; String[] schedule14 = new String[9];
    String[] schedule5 = new String[9]; String[] schedule15 = new String[9];
    String[] schedule6 = new String[9]; String[] schedule16 = new String[9];
    String[] schedule7 = new String[9]; String[] schedule17 = new String[9];
    String[] schedule8 = new String[9]; String[] schedule18 = new String[9];
    String[] schedule9 = new String[9]; String[] schedule19 = new String[9];
    String[] schedule10 = new String[9]; String[] schedule20 = new String[9];

    @Override
    protected ArrayList<String[]> doInBackground(List<String>... params){
        ArrayList<String[]> data = new ArrayList<String[]>();
        data.add(schedule1); data.add(schedule2);
        data.add(schedule3);
        data.add(schedule4);
        data.add(schedule5); data.add(schedule6); data.add(schedule7); data.add(schedule8);
        data.add(schedule9);
        data.add(schedule10);
        data.add(schedule11); data.add(schedule12);
        data.add(schedule13); data.add(schedule14); data.add(schedule15); data.add(schedule16);
        data.add(schedule17); data.add(schedule18); data.add(schedule19); data.add(schedule20);

        InputStream is = null;
        String result = "";

        try {
            URL url = new URL("http://128.61.104.186/ScheduleManager/retrieve_course_data.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            is = conn.getInputStream();
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
            //Log.e("log_tag_result",result.toString());
            JSONArray jArray = new JSONArray(result);
            //Log.e("log_tag_json",jArray.toString());

            for(int i = 0; i < jArray.length(); i++){
                for(int y = 1; y < 10; y++) {
                    JSONObject json = jArray.getJSONObject(i);
                    //Log.e("jsonObject", json.toString());
                    //Log.e("log_tag", json.getString("course" + String.valueOf(y)));
                    data.get(i)[y-1] = json.getString("course" + String.valueOf(y));
                }
            }


        }
        catch(Exception e){
            Log.e("log_tag3", "Error Parsing Data: " + e.toString());
        }


        return data;
    }
}