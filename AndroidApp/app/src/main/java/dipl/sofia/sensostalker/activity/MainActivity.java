package dipl.sofia.sensostalker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import dipl.sofia.sensostalker.R;


public class MainActivity extends AppCompatActivity {

    android.widget.Button but1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);
        onButtonClicked();

    }

    private void onButtonClicked() {
        but1 = (android.widget.Button) findViewById(R.id.Button11);
        but1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View vi2) {
                Log.d("DEBUG", "button11 pressed");
                Intent remot11 = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(remot11,0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_Login:
                // User chose the "Settings" item, show the app settings UI...
                Intent remot75 = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(remot75,0);
                return true;

            case R.id.action_about:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent remot55 = new Intent(MainActivity.this, AboutActivity.class);
                startActivityForResult(remot55,0);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
