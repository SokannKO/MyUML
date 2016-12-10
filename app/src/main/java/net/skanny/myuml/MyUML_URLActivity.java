package net.skanny.myuml;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;

public class MyUML_URLActivity extends Activity {

    EditText addURL;
    Button btnAdd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myuml_url);

        addURL = (EditText)findViewById(R.id.addURL);
        btnAdd = (Button) findViewById(R.id.btnAdd2);


        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity

                savePreferences();
                Intent in = new Intent(getApplicationContext(), MyUMLActivity.class);
                startActivity(in);

            }
        });

    }

    // call
    public void getPreferences(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        pref.getString("SPurl", "");
    }

    // save
    public void savePreferences(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("SPurl", addURL.getText().toString());
        editor.commit();
    }

    // delete
    public void removePreferences(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("hi");
        editor.commit();
    }

    // delete all
    public void removeAllPreferences(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}