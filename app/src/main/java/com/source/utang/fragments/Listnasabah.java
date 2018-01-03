package com.source.utang.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.source.utang.R;
import com.source.utang.Utang;
import com.source.utang.adapters.RecyclerViewAdapter;
import com.source.utang.config.Basic;
import com.source.utang.config.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chandra on 02/01/2018.
 */

public class Listnasabah extends Fragment {
    ListView list;

    public Listnasabah() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.listdata, container, false);
        //ReadUtangTask m= (ReadUtangTask) new ReadUtangTask().execute();
        //list = (ListView) view.findViewById(R.id.listview_data);
        bindListView();
        return view;
    }
    public void bindListView(){
        new ReadUtangTask(getActivity(), list).execute("");
    }
    class ReadUtangTask extends AsyncTask<String, Void, String> {
        ListView mListView;
        ProgressDialog pDialog;
        Activity mContex;
        public ReadUtangTask(Activity activity, ListView listView) {
            this.mListView=listView;
            this.mContex=activity;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContex);
            pDialog.setMessage("Mohon Tunggu..");
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... sText) {
            String returnResult = null;
            try {
                returnResult = getData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return returnResult;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            if (result.equalsIgnoreCase("Exception Caught")) {
                Log.d("RESULT API", "data:" + String.valueOf(result) + ":END");
                Toast.makeText(this.mContex, "Unable to connect to server,please check your internet connection!", Toast.LENGTH_LONG).show();
            }else if (result.equalsIgnoreCase("no results")) {
                Toast.makeText(this.mContex, "Data empty", Toast.LENGTH_LONG).show();
            } else {
                JSONObject data = null;
                try {
                    data = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("Array data JSON REC", String.valueOf(result));
                try {
                    JSONArray debt;
                    debt = data.getJSONArray("data");
                    String[] nasabah_id = new String[debt.length()];
                    String[] nasabah_name = new String[debt.length()];
                    String[] phone = new String[debt.length()];
                    String[] alamat = new String[debt.length()];
                    String[] email = new String[debt.length()];
                    String[] utang = new String[debt.length()];
                    String[] user_id = new String[debt.length()];
                    Log.d("Array data cams rec", String.valueOf(debt));


                    Log.d("Array data locs", String.valueOf(data));
                    for (int i = 0; i < debt.length(); i++) {
                        JSONObject c = debt.getJSONObject(i);
                        nasabah_id[i] = c.getString("nasabah_id");
                        nasabah_name[i] = c.getString("nasabah_name");
                        phone[i] = c.getString("phone");
                        alamat[i] = c.getString("alamat");
                        email[i] = c.getString("email");
                        utang[i] = c.getString("utang");
                        user_id[i] = c.getString("user_id");
                    }

                    RecyclerViewAdapter adapter = new RecyclerViewAdapter(this.mContex,nasabah_id,nasabah_name,phone);
                    RecyclerView myView =  (RecyclerView) mContex.findViewById(R.id.recyclerview);
                    myView.setHasFixedSize(true);
                    myView.setAdapter(adapter);
                    LinearLayoutManager llm = new LinearLayoutManager(this.mContex);
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    myView.setLayoutManager(llm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        private String getData() throws JSONException {
            Basic config = new Basic();
            String url = config.getNASABAH();
            JSONParser jParser = new JSONParser();
            List<NameValuePair> parameter = new ArrayList<NameValuePair>();
            parameter.add(new BasicNameValuePair("un", "0"));
            parameter.add(new BasicNameValuePair("pass", "0"));

            Log.d("URL ONLINE", url);
            JSONObject json = jParser.makeHttpRequest(url, "POST", parameter);
            if(String.valueOf(json).equals("null")){
                return "Exception Caught";
            }else {
                Log.d("RESULT DATA", String.valueOf(json));
                return String.valueOf(json);
            }
        }

    }

}
