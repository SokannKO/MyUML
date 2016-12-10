package net.skanny.myuml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyUML_BoardWriteActivity extends Activity implements OnClickListener {

    EditText addTitle, addId, addContent, addPw;
    Button btnAdd;
    int b_no;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myuml_board_write);
//        etId = (EditText)findViewById(R.id.etId);

        addId = (EditText)findViewById(R.id.addId);
        addPw = (EditText)findViewById(R.id.addPw);
        addTitle = (EditText)findViewById(R.id.addTitle);
        addContent = (EditText)findViewById(R.id.addContent);


        btnAdd = (Button)findViewById(R.id.btnAdd1);

        btnAdd.setOnClickListener(this);

        //ClassPath c = new ClassPath();
        //URL = c.url;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        URL = pref.getString("SPurl", "10.0.0.2");

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            return;
        }

        b_no = extras.getInt("b_no");
//        etId.setText("" + m_idx);

    }

    ClassBoard product = new ClassBoard();



    String updateTrigger = "";

    @Override
    public void onClick(View sender) {
        /*
        String enteredPassword = addPass.getText().toString();
        String enteredSid = addSid.getText().toString();
        if(!enteredPassword.equals(enteredSid)){
            Toast.makeText(MyUML_BoardWriteActivity.this, "Confirm Student ID again", Toast.LENGTH_LONG).show();
            return;
        }
        */
        if(sender.getId() == R.id.btnAdd1){
            AddData taskUpdate = new AddData();
            updateTrigger = "Add";
            taskUpdate.execute(new String[]{"http://"+URL+"/se2/board3/write_update.php"});
            Intent in = new Intent(this, MyUML_BoardActivity.class);
            startActivity(in);
        }


    }

    private class AddData extends AsyncTask<String, Void, Boolean>{
        private ProgressDialog dialog = new ProgressDialog(MyUML_BoardWriteActivity.this);
        private String error;

        String text = "";

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Saving Data...");
            dialog.show();
        }

        InputStream is1;
        @Override
        protected Boolean doInBackground(String... urls) {
            for(String url: urls){
                try {
                    ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("btnSubmit", updateTrigger));
                    pairs.add(new BasicNameValuePair("bTitle", addTitle.getText().toString()));
                    pairs.add(new BasicNameValuePair("bID", addId.getText().toString()));
                    pairs.add(new BasicNameValuePair("bContent", addContent.getText().toString()));
                    pairs.add(new BasicNameValuePair("bPassword", addPw.getText().toString()));

                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    post.setEntity(new UrlEncodedFormEntity(pairs));
                    HttpResponse response = client.execute(post);
                    is1 = response.getEntity().getContent();

                } catch (ClientProtocolException e) {
                    error = "ClientProtocolException: " + e.getMessage();
                    return false;
                } catch (IOException e) {
                    error = "ClientProtocolException: " + e.getMessage();
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }


            if(result == false){
                Toast.makeText(MyUML_BoardWriteActivity.this, error, Toast.LENGTH_LONG).show();
            }
            else{
                if(is1 == null){
                    Toast.makeText(MyUML_BoardWriteActivity.this, "Sending Wrong Parameters", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MyUML_BoardWriteActivity.this, "Save Success", Toast.LENGTH_LONG).show();

                }
            }
        }
    }

}
