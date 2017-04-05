package dipl.sofia.sensostalker.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dipl.sofia.sensostalker.R;
import dipl.sofia.sensostalker.app.AppConfig;
import dipl.sofia.sensostalker.app.AppController;
import dipl.sofia.sensostalker.helper.SQLiteHandler;
import dipl.sofia.sensostalker.helper.SessionManager;

public class UpdateLogIn extends AppCompatActivity {

   private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnUpdate;
    private EditText UpFullName;
    private EditText UpPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_update_login);

        btnUpdate = (Button) findViewById(R.id.btnupdate);
        UpFullName = (EditText) findViewById(R.id.upname);
        UpPassword = (EditText) findViewById(R.id.uppassword);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetails();

        String nameemf = user.get("name");
        final String emailemf = user.get("email");

        UpFullName.setText(nameemf);

        // Login button Click Event
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String upname = UpFullName.getText().toString().trim();
                String uppassword = UpPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!(uppassword.isEmpty())) {
                    // update user

                    updateUser(emailemf, upname, uppassword);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter password or else don't update!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

    }



    private void updateUser(final String email1, final String name1, final String password1) {
        // Tag used to cancel the request
        String tag_string_req = "req_update";

        pDialog.setMessage("Updating ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_UP, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Update Response: " + response.toString());
                hideDialog();

                //Toast.makeText(getApplicationContext(),"whhhhoooooo", Toast.LENGTH_LONG).show();
                try {

                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    String str = String.valueOf(error);

                   // Toast.makeText(getApplicationContext(), str , Toast.LENGTH_LONG).show();
                    if (!error) {
                       // session.setLogin(true);
                        // User successfully stored in MySQL
                        // Now store the user in sqlite
                        String uid1 = jObj.getString("uid");

                        JSONObject user = jObj.getJSONObject("user");

                        String name1 = user.getString("name");
                        String email1 = user.getString("email");
                      //  String password1 = user.getString("password");
                       // String updated_at = user.getString("updated_at");

                       // Toast.makeText(getApplicationContext(), name1 , Toast.LENGTH_LONG).show();

                        db.UpdUser(name1, email1, uid1);

                        Toast.makeText(getApplicationContext(), "User successfully updated!", Toast.LENGTH_LONG).show();

                        // Launch loggedin activity
                        Intent intent = new Intent(
                                UpdateLogIn.this,
                                LoggedInActivity.class);
                        startActivity(intent);
                        finish();
                    } else {

                        // Error occurred in registration. Get the error
                        // message
                        String errorMsg1 = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                               " Error occurred in update, " + errorMsg1, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Update Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                       "Connection error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name1);
                params.put("email", email1);
                params.put("password", password1);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(UpdateLogIn.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
