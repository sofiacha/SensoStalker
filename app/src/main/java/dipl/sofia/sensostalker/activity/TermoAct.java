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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import dipl.sofia.sensostalker.R;
import dipl.sofia.sensostalker.app.AppConfig;
import dipl.sofia.sensostalker.app.AppController;
import dipl.sofia.sensostalker.helper.SQLiteHandler;
import dipl.sofia.sensostalker.helper.SessionManager;

public class TermoAct extends AppCompatActivity {

    LineGraphSeries<DataPoint> seriesavtem,seriestemD, sertempdate, seriestem;
    private SQLiteHandler db;
    private SessionManager session;
    ArrayList<String> temps;
    private static final String TAG = TermoAct.class.getSimpleName();
    static int megthd, meg;
    private final Handler mHandler = new Handler();
    private Runnable mTimer4, mTimer5, mTimer6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_termo);

        Button bt_more_temph = (Button) findViewById(R.id.bt_more_temph);
        bt_more_temph.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent remot15 = new Intent(TermoAct.this, DetailsTemph.class);
                startActivityForResult(remot15, 0);
            }
        });

        Button btnmore = (Button) findViewById(R.id.bt_more);
        btnmore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent remot14 = new Intent(TermoAct.this, DetailsActivity.class);
                startActivityForResult(remot14, 0);
            }
        });

        temps = new ArrayList<String>();

        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        } else {
            Fortwsh_grafikwn();
        }


    }

    private void Fortwsh_grafikwn() {
        GraphView grtempdate = (GraphView) findViewById(R.id.graphtempdate);
        sertempdate = new LineGraphSeries<DataPoint>(GeneratetemphDate());
        grtempdate.addSeries(sertempdate);
        grtempdate.getViewport().setScrollable(true);
        grtempdate.getViewport().setScalable(true);
        grtempdate.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
         grtempdate.getGridLabelRenderer().setNumHorizontalLabels(5);
        sertempdate.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series sertempdate, DataPointInterface dataPoint) {
                Toast.makeText(TermoAct.this, "Η θερμοκρασία στο συγκεκριμένο σημείο είναι: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });
        grtempdate.setTitle("Θερμοκρασία ανά λεπτό");
        grtempdate.setTitleColor(R.color.colorPrimaryDark);

        GraphView graphtemp = (GraphView) findViewById(R.id.graphtemp);
        seriestem = new LineGraphSeries<DataPoint>(GeneratetemphData());
        graphtemp.addSeries(seriestem);
        seriestem.setTitle("Μέση θερμοκρασία");
        seriestem.setDrawDataPoints(true);
        graphtemp.getViewport().setScrollable(true);
        graphtemp.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));
        graphtemp.getGridLabelRenderer().setNumHorizontalLabels(3);
        seriestem.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series seriestem, DataPointInterface dataPoint) {
                Toast.makeText(TermoAct.this, "Η θερμοκρασία στο συγκεκριμένο σημείο είναι: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });
        seriestemD = new LineGraphSeries<DataPoint>(GeneratetemphdData());
        seriestemD.setColor(Color.RED);
        graphtemp.addSeries(seriestemD);
        seriestemD.setTitle("Απόκλιση");
        seriestemD.setDrawDataPoints(true);
             seriestemD.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series seriestemD, DataPointInterface dataPoint) {
                Toast.makeText(TermoAct.this, "Η απόκλιση στο συγκεκριμένο σημείο είναι: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
            }
        });
        graphtemp.setTitle("Μέση θερμοκρασία και απόκλιση ανά ώρα");
        graphtemp.setTitleColor(R.color.colorPrimaryDark);
        graphtemp.getLegendRenderer().setVisible(true);
        graphtemp.getLegendRenderer().setBackgroundColor(Color.TRANSPARENT);
        graphtemp.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        GraphView graphavtemp = (GraphView) findViewById(R.id.graphavtemp);
        seriesavtem = new LineGraphSeries<DataPoint>(GenerateavtempData());
        seriesavtem.setDrawDataPoints(true);
        seriesavtem.setDataPointsRadius(6);
        graphavtemp.addSeries(seriesavtem);
        graphavtemp.getViewport().setXAxisBoundsManual(true);
        graphavtemp.getViewport().setMinX(0);
        graphavtemp.getViewport().setMaxX(12);
        graphavtemp.getViewport().setYAxisBoundsManual(true);
        graphavtemp.getViewport().setMinY(18);
        graphavtemp.getViewport().setMaxY(35);
        graphavtemp.getViewport().setScrollable(true);
        graphavtemp.getViewport().setScalable(true);
        seriesavtem.setOnDataPointTapListener(new OnDataPointTapListener() {
            @Override
            public void onTap(Series seriesavtem, DataPointInterface dataPoint) {
                Toast.makeText(TermoAct.this, "To σημείο είναι το: " + dataPoint, Toast.LENGTH_SHORT).show();
            }
        });
        graphavtemp.setTitle("Μέση Θερμοκρασία ανά απόκλιση");
        graphavtemp.setTitleColor(R.color.colorPrimaryDark);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer4);
        mHandler.removeCallbacks(mTimer5);
        mHandler.removeCallbacks(mTimer6);
        super.onPause();
    }

    static int[] avg, thd;
    static int[] di, id1d;
    static Date[] da;
    static Timestamp[] tiTH, tiTH1;

    private DataPoint[] GeneratetemphDate() {
        String tag_string_req = "req_thdate";
        JsonArrayRequest jsarRequestthd = new JsonArrayRequest(AppConfig.URL_TEMP,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        megthd = response.length();
                        thd = new int[megthd];
                        id1d = new int[megthd];
                        tiTH = new Timestamp[megthd];
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jo1 = response.getJSONObject(i);
                                int tempd = jo1.getInt("temperature");
                                String tim = jo1.getString("timestemh");
                                int senidd = jo1.getInt("idsens");
                                Timestamp ts = Timestamp.valueOf(tim);
                                //Converting to dip unit
                                thd[i] = tempd;
                                id1d[i] = senidd;
                                tiTH[i] = ts;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsarRequestthd, tag_string_req);
        DataPoint[] values5 = new DataPoint[megthd];
        for (int j = 0; j < megthd; j++) {
            DataPoint v5 = new DataPoint(tiTH[j], thd[j]);
            values5[j] = v5;
        }

        return values5;
    }

    private DataPoint[] GeneratetemphData() {
        fortwsht_avg();
        DataPoint[] values2 = new DataPoint[meg];
        for (int j = 0; j < meg; j++) {
            DataPoint v2 = new DataPoint(tiTH1[j],avg[j]);
            values2[j] = v2;
        }
        return values2;
    }

    private DataPoint[] GeneratetemphdData() {
        DataPoint[] values2 = new DataPoint[meg];
        for (int j = 0; j < meg; j++) {
            DataPoint v2 = new DataPoint(tiTH1[j],di[j]);
            values2[j] = v2;
        }
        return values2;
    }


    private void fortwsht_avg(){
        String tag_string_req = "req_update";
        JsonArrayRequest jsarRequestth = new JsonArrayRequest(AppConfig.URL_SHOW,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        meg = response.length();
                        avg = new int[meg];
                        di = new int[meg];
                        tiTH1 = new Timestamp[meg];
                        da = new Date[meg];
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jo = response.getJSONObject(i);
                                int avertemp = jo.getInt("avertemp");
                                String day = jo.getString("day");
                                String hour = jo.getString("hour");
                                int div = jo.getInt("divergence");
                                Timestamp ts1 = Timestamp.valueOf(day + " " +hour);
                                avg[i] = avertemp;
                                di[i] = div;
                                tiTH1[i] = ts1;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        // Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsarRequestth, tag_string_req);
    }


    private DataPoint[] GenerateavtempData() {
        int temp1, temp2;
        for (int i = 0; i < meg - 1; i++) {
            for (int j = 1; j < meg - i; j++) {
                if (di[j - 1] > di[j]) {
                    temp1 = di[j - 1];
                    temp2 = avg[j-1];
                    di[j - 1] = di[j];
                    avg[j-1] = avg[j];
                    di[j] = temp1;
                    avg[j] = temp2;
                }
            }
        }
        DataPoint[] values = new DataPoint[meg];
        for (int j = 0; j < meg; j++) {
            DataPoint v = new DataPoint(di[j], avg[j]);
            values[j] = v;
        }
        return values;
    }

    @Override
    public void onResume() {
        super.onResume();
        mTimer4 = new Runnable() {
            @Override
            public void run() {
                seriestem.resetData(GeneratetemphData());
                seriestemD.resetData(GeneratetemphdData());
                mHandler.postDelayed(this, 10000);
            }
        };

        mHandler.postDelayed(mTimer4, 300);
        mTimer5 = new Runnable() {
            @Override
            public void run() {
                sertempdate.resetData(GeneratetemphDate());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer5, 300);
        mTimer6 = new Runnable() {
            @Override
            public void run() {
                seriesavtem.resetData(GenerateavtempData());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer6, 1000);
      }

    private void logoutUser() {
        session.setLogin(false);
        db.deleteUsers();
        // Launching the login activity
        Intent intent = new Intent(TermoAct.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                // User chose the "Account" item, show the AcoountActivity UI...
                Intent remot775 = new Intent(TermoAct.this, LoggedInActivity.class);
                startActivityForResult(remot775, 0);
                return true;

            case R.id.action_about:
                // User chose the "About" item, show AboutActivity
                Intent remot85 = new Intent(TermoAct.this, AboutActivity.class);
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
