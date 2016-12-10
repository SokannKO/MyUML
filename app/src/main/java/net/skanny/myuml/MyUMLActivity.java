package net.skanny.myuml;

/**
 * Created by Sokann on 2016-04-23.
 */
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyUMLActivity extends Activity{

	Button btnSetURL;
	Button btnNOW;
	Button btnTP;
	Button btnBOARD;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myuml);

		// Buttons
		btnNOW = (Button) findViewById(R.id.btnNOW);
		btnTP = (Button) findViewById(R.id.btnTP);
		btnSetURL = (Button) findViewById(R.id.btnSetURL);
		btnBOARD = (Button) findViewById(R.id.btnBOARD);
		// view products click event

		btnNOW.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All products Activity
				Intent i = new Intent(getApplicationContext(), MyUML_NOWActivity.class);
				startActivity(i);
			}
		});

		btnBOARD.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All products Activity
				Intent i = new Intent(getApplicationContext(), MyUML_BoardActivity.class);
				startActivity(i);
			}
		});

		btnTP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching All products Activity
				Intent i = new Intent(getApplicationContext(), MyUML_TPActivity.class);
				startActivity(i);
			}
		});

		btnSetURL.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching create new product activity
				Intent i = new Intent(getApplicationContext(), MyUML_URLActivity.class);
				startActivity(i);
			}
		});
	}
}