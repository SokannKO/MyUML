package net.skanny.myuml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.TextView;
import android.widget.Toast;

public class MyUML_BoardViewActivity extends Activity implements OnClickListener {

    TextView bvNo, bvTitle, bvId, bvContent;
    Button btnEdit;
    int b_no;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myuml_board_view);
        bvNo = (EditText)findViewById(R.id.bvNo);

        bvId= (TextView)findViewById(R.id.bvId);
        bvTitle= (TextView)findViewById(R.id.bvTitle);
        bvContent = (TextView)findViewById(R.id.bvContent);


        btnEdit = (Button)findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if(extras == null){
            return;
        }

        b_no = extras.getInt("b_no");

        bvNo.setText("" + b_no);


        //ClassPath c = new ClassPath();
        //URL = c.url;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        URL = pref.getString("SPurl", "10.0.0.2");

        ReadData task1 = new ReadData();
        task1.execute(new String[]{"http://" + URL + "/se2/board3/index.php?format=json&id=" + b_no});



    }


    ClassBoard product = new ClassBoard();

    private class ReadData extends AsyncTask<String, Void, Boolean>{

        private ProgressDialog dialog = new ProgressDialog(MyUML_BoardViewActivity.this);
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
                    product.setTitle(json.getString("b_title"));
                    product.setContent(json.getString("b_content"));
                    product.setId(json.getString("b_id"));


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
                Toast.makeText(MyUML_BoardViewActivity.this, error, Toast.LENGTH_LONG).show();
            }
            else{
                bvTitle.setText(product.getTitle());
                bvContent.setText(product.getContent());
                bvId.setText(product.getId());
            }
        }

    }//End of private class ReadData

    String updateTrigger = "";

    @Override
    public void onClick(View sender) {

        if(sender.getId() == R.id.btnEdit){

            int b_no = product.getNo();

            Intent in = new Intent(this, MyUML_BoardEditActivity.class);
            in.putExtra("b_no", b_no);
            startActivity(in);
        }

    }

}
