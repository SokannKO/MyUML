package net.skanny.myuml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class MyUML_BoardEditActivity extends Activity implements OnClickListener {

    EditText beNo, beId, beTitle, beContent, bePw;
    Button btnModify, btnDelete;
    int b_no;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myuml_board_edit);
        beNo = (EditText)findViewById(R.id.beNo);

        beId = (EditText)findViewById(R.id.beId);
        bePw = (EditText)findViewById(R.id.bePw);
        beTitle= (EditText)findViewById(R.id.beTitle);
        beContent = (EditText)findViewById(R.id.beContent);

        btnModify = (Button)findViewById(R.id.btnModify);
        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnModify.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            return;
        }

        b_no = extras.getInt("b_no");

        beNo.setText("" + b_no);


        //ClassPath c = new ClassPath();
        //URL = c.url;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        URL = pref.getString("SPurl", "10.0.0.2");

        ReadData task1 = new ReadData();
        task1.execute(new String[]{"http://" + URL + "/se2/board3/index.php?format=json&id=" + b_no});


    }


    ClassBoard product = new ClassBoard();

    private class ReadData extends AsyncTask<String, Void, Boolean>{

        private ProgressDialog dialog = new ProgressDialog(MyUML_BoardEditActivity.this);
        private String error;

        InputStream is1;
        String text = "";

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Reading Data...");
            dialog.show();
        }


        @Override
        protected Boolean doInBackground(String... urls) {
            for(String url: urls){
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpPost post = new HttpPost(url);
                    HttpResponse response = client.execute(post);
                    is1 = response.getEntity().getContent();

                } catch (ClientProtocolException e) {
                    error = "ClientProtocolException: " + e.getMessage();
                    return false;
                } catch (IOException e) {
                    error = "ClientProtocolException: " + e.getMessage();
                }

            }

            BufferedReader reader;

            try {
                reader = new BufferedReader(new InputStreamReader(is1 ,"iso-8859-1"), 8);
                String line = null;

                while ((line = reader.readLine()) != null) {
                    text += line + "\n";
                }

                is1.close();

            } catch (UnsupportedEncodingException e) {
                error = "Unsupport Encoding: " + e.getMessage();
            } catch (IOException e) {
                error = "Error IO: " + e.getMessage();
            }

            try {
                JSONArray jArray = new JSONArray(text);
                for(int i=0; i<jArray.length(); i++){
                    JSONObject json = jArray.getJSONObject(i);

                    product.setNo(json.getInt("b_no"));
                    product.setId(json.getString("b_id"));
                    product.setTitle(json.getString("b_title"));
                    product.setContent(json.getString("b_content"));



                }
            } catch (JSONException e) {
                error = "Error Convert to JSON or Error JSON Format: " + e.getMessage();
            }


            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(dialog.isShowing()){
                dialog.dismiss();
            }

            if(result == false){
                Toast.makeText(MyUML_BoardEditActivity.this, error, Toast.LENGTH_LONG).show();
            }
            else{
                beTitle.setText(product.getTitle());
                beContent.setText(product.getContent());
                beId.setText(product.getId());
            }
        }

    }//End of private class ReadData

    String updateTrigger = "";

    @Override
    public void onClick(View sender) {

        String enteredPassword = bePw.getText().toString();
        String enteredSid = beId.getText().toString();
        if(!enteredPassword.equals(enteredSid)){
            Toast.makeText(MyUML_BoardEditActivity.this, "Confirm Password again", Toast.LENGTH_LONG).show();
            return;
        }

        if(sender.getId() == R.id.btnModify){
            UpdateData taskUpdate = new UpdateData();
            updateTrigger = "Modify";
            taskUpdate.execute(new String[]{"http://"+URL+"/se2/board3/write_update.php"});
            Intent in = new Intent(this, MyUML_BoardActivity.class);
            startActivity(in);
        }
        else if(sender.getId() == R.id.btnDelete){
            Builder msgBox = new AlertDialog.Builder(this);
            msgBox.setTitle("Delete Confirmation");
            msgBox.setMessage("Are you sure to delete it?");
            msgBox.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub

                }
            });
            msgBox.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    UpdateData taskUpdate = new UpdateData();
                    updateTrigger = "Delete";
                    taskUpdate.execute(new String[]{"http://" + URL + "/se2/board3/delete_update.php"});
                    Intent in = new Intent(MyUML_BoardEditActivity.this, MyUML_BoardActivity.class);
                    startActivity(in);
                }
            });
            msgBox.show();


        }



    }

    private class UpdateData extends AsyncTask<String, Void, Boolean>{
        private ProgressDialog dialog = new ProgressDialog(MyUML_BoardEditActivity.this);
        private String error;

        String text = "";

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Editing Data...");
            dialog.show();
        }

        InputStream is1;
        @Override
        protected Boolean doInBackground(String... urls) {
            for(String url: urls){
                try {
                    ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
                    pairs.add(new BasicNameValuePair("btnSubmit", updateTrigger));

                    pairs.add(new BasicNameValuePair("bno", beNo.getText().toString()));
                    pairs.add(new BasicNameValuePair("bID", beId.getText().toString()));
                    pairs.add(new BasicNameValuePair("bPassword", bePw.getText().toString()));
                    pairs.add(new BasicNameValuePair("bTitle", beTitle.getText().toString()));
                    pairs.add(new BasicNameValuePair("bContent", beContent.getText().toString()));


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
                Toast.makeText(MyUML_BoardEditActivity.this, error, Toast.LENGTH_LONG).show();
            }
            else{
                if(is1 == null){
                    Toast.makeText(MyUML_BoardEditActivity.this, "Sending Wrong Parameters", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(MyUML_BoardEditActivity.this, "Edit Success", Toast.LENGTH_LONG).show();

                }


            }
        }
    }

}
