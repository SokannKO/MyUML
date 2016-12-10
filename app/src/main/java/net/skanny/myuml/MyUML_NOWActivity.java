package net.skanny.myuml;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MyUML_NOWActivity extends Activity {

    EditText view_udc_status;
    TextView view_udc_name;
    String URL;

    EditText addURL;
    Button btnRefresh1, btnRefresh2, btnRefresh3 ;
    ListView list_view_dining, list_view_parking, list_view_recctr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.myuml_now);

        addURL = (EditText)findViewById(R.id.addURL);
        btnRefresh1 = (Button) findViewById(R.id.now_refresh1);
        btnRefresh2 = (Button) findViewById(R.id.now_refresh2);
        btnRefresh3 = (Button) findViewById(R.id.now_refresh3);

        //view_udc_name = (TextView)findViewById(R.id.view_udc_name);
        //view_udc_status = (EditText)findViewById(R.id.view_udc_status);
        list_view_dining = (ListView)findViewById(R.id.list_view_dining);
        list_view_parking = (ListView)findViewById(R.id.list_view_parking);
        list_view_recctr = (ListView)findViewById(R.id.list_view_recctr);

        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec;

        tabSpec = tabHost.newTabSpec("dining");
        tabSpec.setContent(R.id.tab1);
        tabSpec.setIndicator("Dining");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("parking");
        tabSpec.setContent(R.id.tab2);
        tabSpec.setIndicator("Parking");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("recreation");
        tabSpec.setContent(R.id.tab3);
        tabSpec.setIndicator("Rec center");
        tabHost.addTab(tabSpec);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        URL = pref.getString("SPurl", "10.0.0.2");

        ReadData task1 = new ReadData();
        task1.execute(new String[]{"http://"+URL+"/se2/now_update_json.php"});

        btnRefresh1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), MyUML_NOWActivity.class);
                startActivity(i);
            }
        });
        btnRefresh2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), MyUML_NOWActivity.class);
                startActivity(i);
            }
        });
        btnRefresh3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching All products Activity
                Intent i = new Intent(getApplicationContext(), MyUML_NOWActivity.class);
                startActivity(i);
            }
        });

    }

    ArrayList<ClassDining> listDining;
    ArrayList<ClassParking> listParking;
    ArrayList<ClassRecctr> listRecctr;

    ProductArrayAdapterDining adapterDining;
    ProductArrayAdapterParking adapterParking;
    ProductArrayAdapterRecctr adapterRecctr;

    private class ReadData extends AsyncTask<String, Void, Boolean> {

        private ProgressDialog dialog = new ProgressDialog(MyUML_NOWActivity.this);
        private String error;

        InputStream is1;
        String text = "";

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading Data...");
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

            listDining = new ArrayList<ClassDining>();
            listParking = new ArrayList<ClassParking>();
            listRecctr = new ArrayList<ClassRecctr>();

            try {

                JSONArray jArray = new JSONArray(text);
                for(int i=0; i<jArray.length(); i++) {
                    JSONObject json = jArray.getJSONObject(i);

                    ClassDining dining = new ClassDining();
                    ClassParking parking = new ClassParking();
                    ClassRecctr recctr = new ClassRecctr();

                    /*
                    dining.setUDC(json.getString("udc"));
                    dining.setICC(json.getString("icc"));
                    dining.setCCF(json.getString("ccf"));
                    dining.setHNE(json.getString("hne"));

                    parking.setNORTH(json.getString("north"));
                    parking.setSOUTH(json.getString("south"));

                    recctr.setCRC(json.getString("crc"));
                    recctr.setRVV(json.getString("rvv"));
                    recctr.setBSK(json.getString("bsk"));
                    recctr.setTRK(json.getString("trk"));
                    recctr.setRQ1(json.getString("rq1"));
                    recctr.setRQ2(json.getString("rq2"));
                    recctr.setSQC(json.getString("sqc"));
                    //product.setSTATUS(json.getString("StatusString"));
                    */

                    if(json.getInt("type") == 1){
                        dining.setName(json.getString("name"));
                        dining.setStatus(json.getString("status"));
                        listDining.add(dining);
                    }
                    if(json.getInt("type") == 2){
                        parking.setName(json.getString("name"));
                        parking.setStatus(json.getString("status"));
                        listParking.add(parking);
                    }
                    if(json.getInt("type") == 3){
                        recctr.setName(json.getString("name"));
                        recctr.setStatus(json.getString("status"));
                        listRecctr.add(recctr);
                    }
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
                Toast.makeText(MyUML_NOWActivity.this, error, Toast.LENGTH_LONG).show();
            }
            else{
                adapterDining = new ProductArrayAdapterDining(MyUML_NOWActivity.this,R.layout.myuml_now_list,listDining);
                list_view_dining.setAdapter(adapterDining);
                adapterParking = new ProductArrayAdapterParking(MyUML_NOWActivity.this,R.layout.myuml_now_list,listParking);
                list_view_parking.setAdapter(adapterParking);
                adapterRecctr = new ProductArrayAdapterRecctr(MyUML_NOWActivity.this,R.layout.myuml_now_list,listRecctr);
                list_view_recctr.setAdapter(adapterRecctr);

                //view_udc_status.setText(product.getSTATUS());
            }
        }

    }//End of private class ReadData

    private class ProductArrayAdapterDining extends ArrayAdapter<ClassDining> {

        Context context;
        int resource;
        List<ClassDining> proListDining;

        public ProductArrayAdapterDining(Context context, int resource,
                                   List<ClassDining> proListDining) {
            super(context, resource, proListDining);
            this.context = context;
            this.resource = resource;
            this.proListDining = proListDining;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItem = convertView;

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            listItem = inflater.inflate(resource, parent, false);


            TextView liName = (TextView) listItem.findViewById(R.id.liName);
            TextView liStatus = (TextView) listItem.findViewById(R.id.liStatus);

            ClassDining pro = listDining.get(position);

            liName.setText(pro.getName());
            liStatus.setText("Now " + pro.getStatus());

            return listItem;
        }
    }//end of private class ProductArrayAdapter

    private class ProductArrayAdapterParking extends ArrayAdapter<ClassParking> {

        Context context;
        int resource;

        List<ClassParking> proListParking;

        public ProductArrayAdapterParking(Context context, int resource,
                                   List<ClassParking> proListParking) {
            super(context, resource, proListParking);
            this.context = context;
            this.resource = resource;
            this.proListParking = proListParking;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItem = convertView;

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            listItem = inflater.inflate(resource, parent, false);

            TextView liName = (TextView) listItem.findViewById(R.id.liName);
            TextView liStatus = (TextView) listItem.findViewById(R.id.liStatus);

            ClassParking pro = listParking.get(position);

            liName.setText(pro.getName());
            liStatus.setText(pro.getStatus());

            return listItem;
        }
    }//end of private class ProductArrayAdapter
    private class ProductArrayAdapterRecctr extends ArrayAdapter<ClassRecctr> {

        Context context;
        int resource;

        List<ClassRecctr> proListRecctr;

        public ProductArrayAdapterRecctr(Context context, int resource,
                                   List<ClassRecctr> proListRecctr) {
            super(context, resource, proListRecctr);
            this.context = context;
            this.resource = resource;
            this.proListRecctr = proListRecctr;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View listItem = convertView;

            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            listItem = inflater.inflate(resource, parent, false);

            TextView liName = (TextView) listItem.findViewById(R.id.liName);
            TextView liStatus = (TextView) listItem.findViewById(R.id.liStatus);

            ClassRecctr pro = listRecctr.get(position);

            liName.setText(pro.getName());
            liStatus.setText("Now " + pro.getStatus());



            return listItem;
        }
    }//end of private class ProductArrayAdapter
}