package net.skanny.myuml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyUML_BoardActivity extends Activity implements OnItemClickListener {

    Button btnWrite;
    ListView lvBOARD;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myuml_board);
        lvBOARD = (ListView)findViewById(R.id.lvBOARD);
        lvBOARD.setOnItemClickListener(this);


        //ClassPath c = new ClassPath();
        //URL = c.url;

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        URL = pref.getString("SPurl", "10.0.0.2");

        TextView list_ip = (TextView)findViewById(R.id.list_ip);
        list_ip.setText(URL);

        ReadData task1 = new ReadData();
        task1.execute(new String[]{"http://"+URL+"/se2/board3/index.php?format=json"});

        btnWrite = (Button) findViewById(R.id.btnWrite);

        btnWrite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching create new product activity
                Intent i = new Intent(getApplicationContext(), MyUML_BoardWriteActivity.class);
                startActivity(i);

            }
        });
    }


    ArrayList<ClassBoard> listBOARD;
    ProductArrayAdapter adapter;

    private class ReadData extends AsyncTask<String, Void, Boolean>{

        private ProgressDialog dialog = new ProgressDialog(MyUML_BoardActivity.this);
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
                    return false;
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

            listBOARD = new ArrayList<ClassBoard>();

            try {
                JSONArray jArray = new JSONArray(text);
                for(int i=0; i<jArray.length(); i++){
                    JSONObject json = jArray.getJSONObject(i);

                    ClassBoard product = new ClassBoard();
                    product.setNo(json.getInt("b_no"));
                    product.setTitle(json.getString("b_title"));
                    product.setContent(json.getString("b_content"));
                    product.setId(json.getString("b_id"));

                    listBOARD.add(product);
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
                Toast.makeText(MyUML_BoardActivity.this, error, Toast.LENGTH_LONG).show();
            }
            else{
                adapter = new ProductArrayAdapter(
                        MyUML_BoardActivity.this,
                        R.layout.myuml_board_list,
                        listBOARD);
                lvBOARD.setAdapter(adapter);
            }
        }

    }//End of private class ReadData

    private class ProductArrayAdapter extends ArrayAdapter<ClassBoard>{

        Context context;
        int resource;
        List<ClassBoard> proList;

        public ProductArrayAdapter(Context context, int resource,
                                   List<ClassBoard> proList) {
            super(context, resource, proList);
            this.context = context;
            this.resource = resource;
            this.proList = proList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItem = convertView;

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            listItem = inflater.inflate(resource, parent, false);


            TextView liTitle = (TextView) listItem.findViewById(R.id.liTitle);
            TextView liContent = (TextView) listItem.findViewById(R.id.liContent);
            TextView liId = (TextView) listItem.findViewById(R.id.liId);

            ClassBoard pro = listBOARD.get(position);


            liTitle.setText(pro.getTitle());
            liContent.setText(pro.getContent());
            liId.setText(pro.getId());

            return listItem;
        }

    }//end of private class ProductArrayAdapter



    @Override
    public void onItemClick(AdapterView<?> parent, View clickedView, int pos, long id) {
        ClassBoard clickedProduct = (ClassBoard) adapter.getItem(pos);

        int b_no = clickedProduct.getNo();

        Intent in = new Intent(this, MyUML_BoardViewActivity.class);

        in.putExtra("b_no", b_no);
        startActivity(in);

    }

}
