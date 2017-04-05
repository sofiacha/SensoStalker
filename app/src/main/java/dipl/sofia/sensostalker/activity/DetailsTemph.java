package dipl.sofia.sensostalker.activity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup.LayoutParams;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import dipl.sofia.sensostalker.R;
import dipl.sofia.sensostalker.app.AppConfig;
import dipl.sofia.sensostalker.app.AppController;
import dipl.sofia.sensostalker.helper.SQLiteHandler;
import dipl.sofia.sensostalker.helper.SessionManager;

public class DetailsTemph extends AppCompatActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    ArrayList<String> tempes;
    ArrayAdapter<String> adapter;
    TableLayout ta1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.table_layout);
        ta1=(TableLayout)findViewById(R.id.t1);
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());
        if (!session.isLoggedIn()) {
            logoutUser();
        }

        tempes = new ArrayList<String>();
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ...");
        pDialog.setCancelable(false);

        fillCountryTable("Temperature", "TimeStamp", "Sensor ID");

        //    adapter = new ArrayAdapter(this, R.layout.table_layout, R.id.temp, tempes);
        //    setListAdapter(adapter);
        //     String jh = "Average     Time     Date     Divergence     IdSensor";
        //      tempes.add(jh);
        Fortwsh();
        //fillCountryTable();


    }

    private void Fortwsh() {
        String tag_string_req = "req_update";
         pDialog.show();


        JsonArrayRequest jsarRequest = new JsonArrayRequest( AppConfig.URL_TEMP,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "Update Response: " + response.toString());
                        //hideDialog();
                        // Toast.makeText(getApplicationContext(),"whhhhoooooo", Toast.LENGTH_LONG).show();

                        for (int i = 0; i < response.length(); i++) {



                            try {
                                JSONObject jo = response.getJSONObject(i);
                                String temp = jo.getString("temperature"); // + "     " + jo.getString("hour") + "     " + jo.getString("day") + "     " + jo.getString("divergence") +  "     " + jo.getString("idsens");
                                String tim = jo.getString("timestemh");
                                String senid = jo.getString("idsens");
                               // String div = jo.getString("divergence");
                               // String senid = jo.getString("idsens");
                                //Converting to dip unit

                                fillCountryTable(temp, tim, senid);

                                //String hour = ;
                                //                         tableLayout.addView(createTableRow(adapter.getItem(i));
                                // tempes.add(avertemp);
                                // Toast.makeText(getApplicationContext(),tempes.toString(), Toast.LENGTH_LONG).show();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                          pDialog.dismiss();
                         //adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();
                //  hideDialog();

            }
        });
        //  Toast.makeText(getApplicationContext(),tempes.toString(), Toast.LENGTH_LONG).show();

// Access the RequestQueue through your singleton class.
        AppController.getInstance().addToRequestQueue(jsarRequest, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(DetailsTemph.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    void fillCountryTable(String avertemp, String day, String hour) {
        int dip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                (float) 1, getResources().getDisplayMetrics());
        TableRow row;
        TextView t1, t2, t3, t4, t5;
        row = new TableRow(this);

        t1 = new TextView(this);
        t2 = new TextView(this);
        t3 = new TextView(this);
      // t4 = new TextView(this);
      //  t5 = new TextView(this);

        t1.setText(avertemp);
        t2.setText(day);
        t3.setText(hour);
     //   t4.setText(div);
       // t5.setText(senid);

        t1.setTypeface(null, 1);
        t2.setTypeface(null, 1);
        t3.setTypeface(null, 1);
     //   t4.setTypeface(null, 1);
      //  t5.setTypeface(null, 1);

        t1.setTextSize(15);
        t2.setTextSize(15);
        t3.setTextSize(15);
      //  t4.setTextSize(15);
      //  t5.setTextSize(15);

        t1.setWidth(110 * dip);
        t2.setWidth(200 * dip);
        t3.setWidth(100 * dip);
      //  t4.setWidth(100 * dip);
     //   t5.setWidth(100 * dip);

        t1.setPadding(10*dip, 0, 0, 0);

        row.addView(t1);
        row.addView(t2);
        row.addView(t3);
      //  row.addView(t4);
      //  row.addView(t5);

        ta1.addView(row, new TableLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                // User chose the "Settings" item, show the app settings UI...
                Intent remot75 = new Intent(DetailsTemph.this, LoggedInActivity.class);
                startActivityForResult(remot75, 0);
                return true;

            case R.id.action_about:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent remot65 = new Intent(DetailsTemph.this, AboutActivity.class);
                startActivityForResult(remot65, 0);
                return true;

            case R.id.action_logout:
                logoutUser();

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
        // return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

}