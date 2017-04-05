package dipl.sofia.sensostalker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

//import com.google.android.gms.fitness.data.DataPoint;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;
//import com.google.android.gms.fitness.data.DataPoint;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import dipl.sofia.sensostalker.R;
import dipl.sofia.sensostalker.app.AppConfig;
import dipl.sofia.sensostalker.app.AppController;
import dipl.sofia.sensostalker.helper.SQLiteHandler;
import dipl.sofia.sensostalker.helper.SessionManager;

import static android.app.PendingIntent.getActivity;

public class InfoSensoRem extends AppCompatActivity {

    LineGraphSeries<DataPoint> seriesdhum, serieshum, sertempdate, serhumdate, seriestem, seriesavhum;
    private SQLiteHandler db;
    private SessionManager session;
    ArrayList<String> hums;
    private static final String TAG = InfoSensoRem.class.getSimpleName();
    static int megth, megthd, meghumd, meg, megah, megh;
    private final Handler mHandler = new Handler();
    private Runnable mTimer1, mTimer2, mTimer3, mTimer4;
    private Timestamp ta, tt;

//    final java.text.DateFormat dateTimeFormatter = DateFormat.getTimeFormat(this);
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_info_senso_rem);


        Button bt_more_humh = (Button) findViewById(R.id.bt_more_humh);
        bt_more_humh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log.d("DEBUG", "button11 pressed");
                Intent remot16 = new Intent(InfoSensoRem.this, DetHumh.class);
                startActivityForResult(remot16, 0);
            }
        });


        Button bt_more_hum = (Button) findViewById(R.id.bt_more_hum);
        bt_more_hum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Log.d("DEBUG", "button11 pressed");
                Intent remot17 = new Intent(InfoSensoRem.this, DetHum.class);
                startActivityForResult(remot17, 0);
            }
        });
        hums = new ArrayList<String>();

        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        } else {
            Fortwsh_grafikwn();
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void Fortwsh_grafikwn() {

        GraphView grhumdate = (GraphView) findViewById(R.id.graphumdate);
        serhumdate = new LineGraphSeries<DataPoint>(GeneratehumhDate());
        grhumdate.addSeries(serhumdate);
        grhumdate.getViewport().setScrollable(true);
        grhumdate.getViewport().setScalable(true);
        grhumdate.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
       grhumdate.getGridLabelRenderer().setNumHorizontalLabels(5);
        serhumdate.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series serhumdate, DataPointInterface dataPoint) {
                Toast.makeText(InfoSensoRem.this, "Η υγρασία στο συγκεκριμένο σημείο είναι: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });
        grhumdate.setTitle("Υγρασία ανά λεπτό");
      //  grhumdate.setTitleTextSize(60);
        grhumdate.setTitleColor(R.color.colorPrimaryDark);




        GraphView graphhum = (GraphView) findViewById(R.id.graphhum);
        serieshum = new LineGraphSeries<DataPoint>(GeneratehumhData());
        graphhum.addSeries(serieshum);
        serieshum.setTitle("Mέση υγρασία");
        serieshum.setDrawDataPoints(true);
        graphhum.getViewport().setScrollable(true);
        graphhum.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graphhum.getGridLabelRenderer().setNumHorizontalLabels(3);
        serieshum.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series serieshum, DataPointInterface dataPoint) {
                Toast.makeText(InfoSensoRem.this, "Η μέση υγρασία στο συγκεκριμένο σημείο είναι: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });
        seriesdhum = new LineGraphSeries<DataPoint>(GeneratehumhdData());
        seriesdhum.setColor(Color.RED);
        seriesdhum.setTitle("Απόκλιση");
        graphhum.addSeries(seriesdhum);
        seriesdhum.setDrawDataPoints(true);
        seriesdhum.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series seriesdhum, DataPointInterface dataPoint) {
                Toast.makeText(InfoSensoRem.this, "Η απόκλιση στο συγκεκριμένο σημείο είναι: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });
       // graphhum.setTitleTextSize(R.dimen.activity_horizontal_margin);
        graphhum.setTitleColor(R.color.colorPrimaryDark);
        graphhum.getLegendRenderer().setVisible(true);
        graphhum.getLegendRenderer().setBackgroundColor(Color.TRANSPARENT);
        graphhum.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graphhum.setTitle("Μέση υγρασία και απόκλιση ανά ώρα");



        GraphView graphavhum = (GraphView) findViewById(R.id.graphavhum);
        seriesavhum = new LineGraphSeries<DataPoint>(GenerateavhumData());
        seriesavhum.setDrawDataPoints(true);
        seriesavhum.setDataPointsRadius(6);
        graphavhum.addSeries(seriesavhum);
        graphavhum.getViewport().setScrollable(true);
        graphavhum.getViewport().setScalable(true);
        graphavhum.getViewport().setXAxisBoundsManual(true);
        graphavhum.getViewport().setMinX(4);
        graphavhum.getViewport().setMaxX(20);
        graphavhum.getViewport().setYAxisBoundsManual(true);
        graphavhum.getViewport().setMinY(50);
        graphavhum.getViewport().setMaxY(65);
        seriesavhum.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series seriesavhum, DataPointInterface dataPoint) {
                Toast.makeText(InfoSensoRem.this, "To σημείο είναι το: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
        graphavhum.setTitle("Μέση υγρασία ανά απόκλιση");
      //  graphavhum.setTitleTextSize(60);
        graphavhum.setTitleColor(R.color.colorPrimaryDark);
    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(InfoSensoRem.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                serieshum.resetData(GeneratehumhData());
                seriesdhum.resetData(GeneratehumhdData());
                mHandler.postDelayed(this, 500);
            }
        };
        mHandler.postDelayed(mTimer1, 300);

        mTimer2 = new Runnable() {
            @Override
            public void run() {
                serhumdate.resetData(GeneratehumhDate());
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(mTimer2, 300);

        mTimer3 = new Runnable() {
            @Override
            public void run() {
                seriesavhum.resetData(GenerateavhumData());
                mHandler.postDelayed(this, 300);
            }
        };

        mHandler.postDelayed(mTimer3, 1000);
    }
  /*  @Override
    public void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                sertempdate.resetData(GeneratetemphDate());
                serhumdate.resetData(GeneratehumhDate());
                seriesavtem.resetData(GenerateavtempData());
                seriesavhum.resetData(GenerateavhumData());
                //serieshum.resetData(GeneratehumhData());
               // seriestem.resetData(GeneratetemphData());
                mHandler.post(this);
                //mHandler.postDelayed(this, 100);
            }
        };
        mHandler.postDelayed(mTimer1, 100);

    } */

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        mHandler.removeCallbacks(mTimer2);
        mHandler.removeCallbacks(mTimer3);
        super.onPause();
    }

    static int[] avg, thd, th, hum, humd, hdi, avgh;
    static int[] di, id1, idhum1d, id1d, idhum1;
      static Time[] ti;
     static Date[] da, hda;
    static Timestamp[] tiTH, tiHD, tiTH1, tiTH2;


    private DataPoint[] GeneratehumhDate() {
        String tag_string_req = "req_humhdate";
        JsonArrayRequest jsarRequesthd = new JsonArrayRequest(AppConfig.URL_HUMH,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        meghumd = response.length();
                        humd = new int[meghumd];
                        idhum1d = new int[meghumd];
                        tiHD = new Timestamp[meghumd];
                        //tiTH= new Time[meg];
                        //daTH = new Date[meg];
                        for (int i = 0; i < response.length(); i++) {

                            try {
                                JSONObject jo6 = response.getJSONObject(i);
                                int hud = jo6.getInt("humidity"); // + "     " + jo.getString("hour") + "     " + jo.getString("day") + "     " + jo.getString("divergence") +  "     " + jo.getString("idsens");
                                String timd = jo6.getString("tumitimeh");
                                int senridd = jo6.getInt("idsens");
                                //Converting to dip unit
                                Timestamp hd = Timestamp.valueOf(timd);
                                humd[i] = hud;
                                idhum1d[i] = senridd;
                                tiHD[i] = hd;

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "HumhDate Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsarRequesthd, tag_string_req);

        DataPoint[] values6 = new DataPoint[meghumd];

        for (int j = 0; j < meghumd; j++) {
            DataPoint v6 = new DataPoint(tiHD[j], humd[j]);
            values6[j] = v6;
        }

        return values6;
    }



    private DataPoint[] GenerateavhumData() {
      //  for_avhum();
        DataPoint[] values4 = new DataPoint[megah];

        int temp1, temp2;
        for (int i = 0; i < megah - 1; i++) {

            for (int j = 1; j < megah - i; j++) {
                if (hdi[j - 1] > hdi[j]) {
                    temp1 = hdi[j - 1];
                    temp2 = avgh[j-1];
                    hdi[j - 1] = hdi[j];
                    avgh[j-1] = avgh[j];
                    hdi[j] = temp1;
                    avgh[j] = temp2;
                }
            }
        }

        for (int j = 0; j < megah; j++) {

            DataPoint v4 = new DataPoint(hdi[j], avgh[j]);
            values4[j] = v4;
        }

        return values4;
    }




    private DataPoint[] GeneratehumhData() {
            for_avhum();
            DataPoint[] values3 = new DataPoint[megah];
              for (int j = 0; j < megah; j++) {
            DataPoint v3 = new DataPoint(tiTH2[j], avgh[j]);
            values3[j] = v3;
        }

        return values3;
    }


    private DataPoint[] GeneratehumhdData() {
        for_avhum();
        DataPoint[] values3 = new DataPoint[megah];
        int temp4;
     /*   for (int i = 0; i < megh - 1; i++) {

            for (int j = 1; j < megh - i; j++) {
                if (hum[j - 1] > hum[j]) {
                    temp4 = hum[j - 1];
                    hum[j - 1] = hum[j];
                    hum[j] = temp4;
                }
            }
        }
*/
        for (int j = 0; j < megah; j++) {
            DataPoint v3 = new DataPoint(tiTH2[j], hdi[j]);
            values3[j] = v3;
        }

        return values3;
    }

    private void for_avhum(){
      String tag_string_req = "req_update";


      JsonArrayRequest jsarRequest = new JsonArrayRequest(AppConfig.URL_HUM,
              new Response.Listener<JSONArray>() {

                  @Override
                  public void onResponse(JSONArray response) {

                      megah = response.length();
                      avgh = new int[megah];
                      hdi = new int[megah];
                      tiTH2 = new Timestamp[megah];
                      for (int i = 0; i < response.length(); i++) {

                          try {
                              JSONObject jo4 = response.getJSONObject(i);
                              int averhum = jo4.getInt("humidit");
                              String day = jo4.getString("hdate");
                              String hour = jo4.getString("hhour");
                              int hdiv = jo4.getInt("divergence");
                              Timestamp ts2 = Timestamp.valueOf(day + " " +hour);
                              avgh[i] = averhum;
                              hdi[i] = hdiv;
                              tiTH2[i] = ts2;
                          } catch (JSONException e) {
                              e.printStackTrace();
                          }
                      }

                  }
              }, new Response.ErrorListener() {

          @Override
          public void onErrorResponse(VolleyError error) {
              Log.e(TAG, "Humidity Error: " + error.getMessage());
              Toast.makeText(getApplicationContext(),
                      "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
              //  hideDialog();

          }
      });
      // Access the RequestQueue through your singleton class.
      AppController.getInstance().addToRequestQueue(jsarRequest, tag_string_req);

  }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                // User chose the "Account" item, show the AcoountActivity UI...
                Intent remot775 = new Intent(InfoSensoRem.this, LoggedInActivity.class);
                startActivityForResult(remot775, 0);
                return true;

            case R.id.action_about:
                // User chose the "About" item, show AboutActivity
                Intent remot85 = new Intent(InfoSensoRem.this, AboutActivity.class);
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
