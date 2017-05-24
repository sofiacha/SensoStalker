package dipl.sofia.sensostalker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import dipl.sofia.sensostalker.R;
import dipl.sofia.sensostalker.app.AppConfig;
import dipl.sofia.sensostalker.app.AppController;
import dipl.sofia.sensostalker.helper.SQLiteHandler;
import dipl.sofia.sensostalker.helper.SessionManager;
import static java.util.concurrent.TimeUnit.*;

public class BasicScr extends AppCompatActivity implements View.OnTouchListener  {
    private SQLiteHandler db;
    private SessionManager session;
    private TextView txtLumos, btntemp, btnhm, txtFws;
    private static final String TAG = BasicScr.class.getSimpleName();
    static int hm, thp, lumos;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_basic_scr);


        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }
        txtLumos = (TextView) findViewById(R.id.textView7);
        txtFws = (TextView) findViewById(R.id.textView8);
         btntemp = (TextView)findViewById(R.id.textView11);
         btnhm = (TextView) findViewById(R.id.textView9);
            setLumos();
            setTem1();
            setHum1();
        beepForAnHour();

    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        scheduler.shutdown();
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        beepForAnHour();
    }


    public void beepForAnHour() {
        final Runnable beeper = new Runnable() {
            public void run() {
                setLumos();
                setTem1();
                setHum1(); }
        };
        final ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(beeper, 5, 5, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() { beeperHandle.cancel(true); }
        }, 60 * 60, SECONDS);
    }
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(BasicScr.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void setHum1() {
        String tag_string_req = "req_update";
        JsonObjectRequest jsarh1Request = new JsonObjectRequest
                (AppConfig.URL_HUM1, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                      hm = 5;
                        try {
                                hm = response.getInt("humidity");
                               Log.d(TAG, "hm: " + response.getString("humidity"));
                            String mpla0 = response.getString("humidity");
                            btnhm.setText( mpla0 + "%");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "lumos Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsarh1Request, tag_string_req);
        btnhm.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        btnhm.setOnTouchListener(this);

    }

    private void setTem1() {
        String tag_string_req = "req_update";
        JsonObjectRequest jsart1Request = new JsonObjectRequest
                (AppConfig.URL_TEM1, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        thp = 5;
                        try {
                            thp = response.getInt("temperature");
                            String mpla1 = response.getString("temperature");
                            final String DEGREE  = "\u00b0";
                            if (thp <= 35) {
                                btntemp.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                            }
                            else{
                                btntemp.setTextColor(Color.RED);
                            }
                            btntemp.setText( mpla1 + DEGREE + "C");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "lumos Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });

        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsart1Request, tag_string_req);

        btntemp.setOnTouchListener(this);
    }

    private void setLumos() {
    String tag_string_req = "req_update";
    JsonObjectRequest jsarlRequest = new JsonObjectRequest
            (AppConfig.URL_LUMOS, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {

                    try {
                        lumos = response.getInt("light");

                        Log.d(TAG, "fws: " + lumos);
                        if (lumos <= 10) {
                            txtLumos.setText("Το φως είναι κλειστό ");
                            txtFws.setText("Η φωτεινότητα είναι πολύ χαμηλή");


                        } else {
                            txtLumos.setText("Το φως είναι ανοιχτό ");
                            if (lumos <= 300){
                                txtFws.setText("Η φωτεινότητα είναι χαμηλή");
                            }
                            else{
                                txtFws.setText("Η φωτεινότητα είναι υψηλή");
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "lumos Error: " + error.getMessage());
                    Toast.makeText(getApplicationContext(),
                            "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();

                }
            });

    // Access the RequestQueue through your singleton class.
    AppController.getInstance().addToRequestQueue(jsarlRequest, tag_string_req);
        txtFws.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        txtFws.setOnTouchListener(this);
}

    @Override
    public boolean onTouch(View v, MotionEvent event) {
       if(v == txtFws){
           if (lumos <= 10) {

               Toast.makeText(BasicScr.this, "Το φως είναι κλειστό ", Toast.LENGTH_SHORT).show();

           } else {
               Toast.makeText(BasicScr.this, "Το φως είναι ανοιχτό ", Toast.LENGTH_SHORT).show();
           }

       }
        else if(v ==btntemp){
           Intent i = new Intent(getApplicationContext(), TermoAct.class);
           startActivity(i);
       }
        else{
           Intent i = new Intent(getApplicationContext(), InfoSensoRem.class);
           startActivity(i);
       }
        return false;
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                // User chose the "Account" item, show the AcoountActivity UI...
                Intent remot775 = new Intent(BasicScr.this, LoggedInActivity.class);
                startActivityForResult(remot775, 0);
                return true;

            case R.id.action_about:
                // User chose the "About" item, show AboutActivity
                Intent remot85 = new Intent(BasicScr.this, AboutActivity.class);
                startActivityForResult(remot85, 0);
                return true;

            case R.id.action_logout:
                logoutUser();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

}
