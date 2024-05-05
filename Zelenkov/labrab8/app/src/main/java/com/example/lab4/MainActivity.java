package com.example.lab4;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String JSON_URL = "https://api.jsonserve.com/v2YaMf";
    private ListView listView;
    private ProgressBar progressBar;
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        progressBar = findViewById(R.id.progressBar);
        adapter = new ListViewAdapter(MainActivity.this, R.layout.row, new ArrayList<>());
        listView.setAdapter(adapter);

        Button btnLoadData = findViewById(R.id.btnLoadData);
        btnLoadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                loadJSONFromURL(JSON_URL, new VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<JSONObject> listItems) {
                        progressBar.setVisibility(View.INVISIBLE);
                        adapter.updateData(listItems);
                    }
                });
            }
        });


        Button btnClearData = findViewById(R.id.btnDelete);
        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear(); // Очистить данные адаптера
                adapter.notifyDataSetChanged(); // Обновить список
            }
        });
    }

    private void loadJSONFromURL(String url, final VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(EncodingToUTF8(response));
                            JSONArray jsonArray = object.getJSONArray("users");
                            ArrayList<JSONObject> listItems = getArrayListFromJSONArray(jsonArray);
                            callback.onSuccess(listItems);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error", error.toString());
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }



    private ArrayList<JSONObject> getArrayListFromJSONArray(JSONArray jsonArray) {
        ArrayList<JSONObject> aList = new ArrayList<>();
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    aList.add(jsonArray.getJSONObject(i));
                }
            }
        } catch (JSONException js) {
            js.printStackTrace();
        }
        return aList;
    }

    public static String EncodingToUTF8(String response) {
        try {
            byte[] code = response.toString().getBytes("ISO-8859-1");
            response = new String(code, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return response;
    }



    public interface VolleyCallback {
        void onSuccess(ArrayList<JSONObject> listItems);
    }

    private class ListViewAdapter extends ArrayAdapter<JSONObject> {
        private Context context;
        private int resource;
        private ArrayList<JSONObject> items;

        public ListViewAdapter(Context context, int resource, ArrayList<JSONObject> items) {
            super(context, resource, items);
            this.context = context;
            this.resource = resource;
            this.items = items;
        }

        public void updateData(ArrayList<JSONObject> items) {
            this.items.clear();
            this.items.addAll(items);
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(resource, parent, false);
            }

            TextView textViewName = convertView.findViewById(R.id.textViewName);
            TextView textViewEmail = convertView.findViewById(R.id.textViewEmail);
            Button btnDetails = convertView.findViewById(R.id.btnDetails);

            try {
                JSONObject item = items.get(position);
                String name = item.getString("name");
                String email = item.getString("email");

                textViewName.setText(name);
                textViewEmail.setText(email);

                btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject selectedItem = items.get(position);
                        try {
                            showDetailsDialog(selectedItem);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return convertView;
        }


        private void showDetailsDialog(JSONObject item) throws JSONException {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            dialogBuilder.setTitle("Подробная информация");

            String id = item.getString("id");
            String name = item.getString("name");
            String email = item.getString("email");
            String gender = item.getString("gender");
            JSONObject contact = item.getJSONObject("contact");
            String mobile = contact.getString("mobile");
            String home = contact.getString("home");
            String office = contact.getString("office");

            String formattedText = "id: " + id + "\n"
                    + "name: " + name + "\n"
                    + "email: " + email + "\n"
                    + "gender: " + gender + "\n"
                    + "mobile: " + mobile + "\n"
                    + "home: " + home + "\n"
                    + "office: " + office;

            TextView textView = new TextView(context);
            textView.setText(formattedText);

            dialogBuilder.setView(textView);

            dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        }
    }
}