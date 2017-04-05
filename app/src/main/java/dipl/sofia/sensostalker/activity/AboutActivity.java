package dipl.sofia.sensostalker.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import dipl.sofia.sensostalker.R;

import static android.R.attr.button;

public class AboutActivity extends AppCompatActivity {

    Button buttto, butttto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
         //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_about);


        buttto = (android.widget.Button) findViewById(R.id.butttton);
        buttto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewrt) {

                String url = "https://gr.linkedin.com/in/sofia-chatzopoulou-04976a104";
                Intent intwe = new Intent(Intent.ACTION_VIEW);
                intwe.setData(Uri.parse(url));
                startActivity(intwe);
                // Log.d("DEBUG", "butttton pressed");
                // Intent intwe = new Intent(AboutActivity.this, LoginActivity.class);
                // startActivityForResult(intwe,0);
            }
        });


        butttto = (android.widget.Button) findViewById(R.id.buttttton);
        butttto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewrtg) {

                String urlg = "http://athos.ceid.upatras.gr/garofalakis/";
                Intent intwge = new Intent(Intent.ACTION_VIEW);
                intwge.setData(Uri.parse(urlg));
                startActivity(intwge);
                // Log.d("DEBUG", "butttton pressed");
                // Intent intwe = new Intent(AboutActivity.this, LoginActivity.class);
                // startActivityForResult(intwe,0);
            }
        });

    }



}
