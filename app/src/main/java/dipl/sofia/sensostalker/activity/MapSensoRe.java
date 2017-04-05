package dipl.sofia.sensostalker.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
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

import static java.util.concurrent.TimeUnit.SECONDS;

//import dipl.sofia.sensostalker.R;

public class MapSensoRe extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapSensoRe.class.getSimpleName();
    private GoogleMap mMap;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    Marker marker;
    private SQLiteHandler db;
    private SessionManager session;

    public int tempeh;
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
        setContentView(R.layout.activity_map_sensore);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        //  Log.d(TAG, "8ermokrasia11: " + tempeh);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();





    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // S_O=true;
        Fortwsh(new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                Log.d(TAG, "8ermokrasia11: " + result);
                tempeh = Integer.parseInt(result);
            }

            @Override
            public void onFail(String msg) {
                tempeh = 15;
                Toast.makeText(getApplicationContext(), "Connection error: " + msg, Toast.LENGTH_LONG).show();
            }
        });
        // Add a marker in Panepisthmio Patrwn and move the camera
        beepForAnHour1();

    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        scheduler.shutdown();
    }


    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        beepForAnHour1();
    }

    public void beepForAnHour1() {
        final Runnable beeper = new Runnable() {
            public void run() {
               // mMap.clear();
                Fortwsh(new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.d(TAG, "8ermokrasia11: " + result);
                        tempeh = Integer.parseInt(result);
                    }

                    @Override
                    public void onFail(String msg) {
                        tempeh = 15;
                        Toast.makeText(getApplicationContext(), "Connection error: " + msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        final ScheduledFuture<?> beeperHandle =
                scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
        scheduler.schedule(new Runnable() {
            public void run() { beeperHandle.cancel(true); }
        }, 60 * 60, SECONDS);
    }
    private void moveToCurrentLocation(LatLng currentLocation) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
        // Zoom in, animating the camera.
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);


    }


    public void Fortwsh(final VolleyCallback callback) {
        String tag_string_req = "req_update";
        // pDialog.show();


        JsonObjectRequest jsarRequest = new JsonObjectRequest
                (AppConfig.URL_MARKCOL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        int temph = 5;
                        try {
                            temph = response.getInt("temperature");
                            // Log.d(TAG, "Marker 8ermokrasia: " + response.toString());
                            //Log.d(TAG, "8ermokrasia13: " + response.getString("temperature"));

                            callback.onSuccess(response.getString("temperature"));
                            LatLng panep = new LatLng(38.283689, 21.789079);
                            moveToCurrentLocation(panep);

                            //http://stackoverflow.com/questions/708012/how-to-declare-global-variables-in-android
                           // Log.d(TAG, "8ermokrasia12: " + temph);
                            if (temph <= 39) {
                                if (marker != null) {
                                    marker.remove();
                                }
                                //xrwmata pou mporw na xrhsimopoihsw     HUE_AZURE      HUE_BLUE            HUE_CYAN            HUE_GREEN            HUE_MAGENTA            HUE_ORANGE            HUE_RED           HUE_ROSE       HUE_VIOLET         HUE_YELLOW
                                marker =  mMap.addMarker(new MarkerOptions().position(panep).title("Ο αισθητήρας είναι συνδεδεμένος!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(panep));
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Intent intent = new Intent(MapSensoRe.this, BasicScr.class);
                                        startActivity(intent);


                                    }
                                });
                            } else {
                                if (marker != null) {
                                    marker.remove();
                                }
                                //xrwmata pou mporw na xrhsimopoihsw     HUE_AZURE      HUE_BLUE            HUE_CYAN            HUE_GREEN            HUE_MAGENTA            HUE_ORANGE            HUE_RED           HUE_ROSE       HUE_VIOLET         HUE_YELLOW
                              marker =  mMap.addMarker(new MarkerOptions().position(panep).title("ΠΡΟΣΟΧΗ ΥΨΗΛΕΣ ΘΕΡΜΟΚΡΑΣΙΕΣ!").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_heat)));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(panep));
                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                    @Override
                                    public void onInfoWindowClick(Marker marker) {
                                        Intent intent = new Intent(MapSensoRe.this, BasicScr.class);
                                        startActivity(intent);


                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //callback.onFail(e.toString());
                        }
                        // tempeh = response.getInt();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Map Error: " + error.getMessage());
                        Toast.makeText(getApplicationContext(),
                                "Connection Error:" + error.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });
        //  Toast.makeText(getApplicationContext(),tempes.toString(), Toast.LENGTH_LONG).show();

// Access the RequestQueue through your singleton class.

        AppController.getInstance().addToRequestQueue(jsarRequest, tag_string_req);
        // Log.d(TAG, "8ermokrasia14: " + tempeh);

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MapSensoRe Page")

                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public interface VolleyCallback {
        void onSuccess(String result);

        void onFail(String msg);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                // User chose the "Settings" item, show the app settings UI...
                Intent remot75 = new Intent(MapSensoRe.this, LoggedInActivity.class);
                startActivityForResult(remot75, 0);
                return true;

            case R.id.action_about:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent remot65 = new Intent(MapSensoRe.this, AboutActivity.class);
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


    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MapSensoRe.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return true;
    }

}
