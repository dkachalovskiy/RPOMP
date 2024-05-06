package com.example.rpo_lab2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Display;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Button backBut;

    private Button saveBut;
    private ArrayList<Model>modelArrayList;
    private MyApi myApi;
    private ListView lv;
    private int numbElements;
    private String BaseUrl="https://jsonplaceholder.typicode.com/";

    Custom custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        backBut = (Button) findViewById(R.id.back);
        saveBut = (Button) findViewById(R.id.save);

        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {finish();}
        });

        saveBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                String jsonString = gson.toJson(modelArrayList);

                try(FileOutputStream fileOutputStream =
                            getApplicationContext().openFileOutput("data.txt", Context.MODE_PRIVATE)) {
                    fileOutputStream.write(jsonString.getBytes());
                    // View - Tool Windows - device File Explorer --- data/data/[название_пакета_приложения]/files
                    toastHandler("Data saved in data.txt", "Succes");

                } catch (Exception e) {
                    toastHandler(e.getMessage(), "Exception");
                }
            }
        });

        lv=findViewById ( R.id.lv );
        modelArrayList=new ArrayList<> ();

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Intent res = result.getData();
                    String idValue = res.getStringExtra("idvalue");
                    String titleValue = res.getStringExtra("titlevalue");
                    String bodyValue = res.getStringExtra("bodyvalue");

                    for(Model m : modelArrayList) {
                        if(m.getId().equals(idValue)) {
                            m.setTitle(titleValue);
                            m.setBody(bodyValue);
                        }
                    }

                    custom.notifyDataSetChanged();
                });


        Bundle args = getIntent().getExtras();
        if(args != null){
            if (args.get("datasize") != null) {
                numbElements = Integer.parseInt(args.getString("datasize"));
                displayRetrofitData(numbElements);
            } else {
            }
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,int position, long id) {
                Model item = (Model) custom.getItem(position);

                Intent intent = new Intent(MainActivity.this, DetailActivity.class);

                if(item != null) {
                    String title = item.getTitle();

                    intent.putExtra("id", item.getId());
                    intent.putExtra("title", item.getTitle());
                    intent.putExtra("body", item.getBody());

                    activityResultLauncher.launch(intent);
                }
                else System.out.println("NULL OBJECT");
            }
        });
    }

    private void displayRetrofitData(int numbElements) {
        Retrofit retrofit=new Retrofit.Builder ()
                .baseUrl ( BaseUrl )
                .addConverterFactory ( GsonConverterFactory.create () )
                .build ();
        myApi=retrofit.create ( MyApi.class );
        Call<ArrayList<Model>>arrayListCall=myApi.callModel ();
        arrayListCall.enqueue ( new Callback<ArrayList<Model>> () {
            @Override
            public void onResponse(Call<ArrayList<Model>> call, Response<ArrayList<Model>> response) {
                modelArrayList.addAll(response.body().subList(0, numbElements));

                custom=new Custom(modelArrayList,MainActivity.this,R.layout.singleview);
                lv.setAdapter ( custom );
            }

            @Override
            public void onFailure(Call<ArrayList<Model>> call, Throwable t) {
                Toast.makeText ( MainActivity.this, "Failed to load data", Toast.LENGTH_SHORT ).show ();
            }
        } );
    }

    private void toastHandler(String message, String title){
        AlertDialog.Builder alertbox = new AlertDialog.Builder(MainActivity.this);
        alertbox.setTitle(title);
        alertbox.setMessage(message);
        alertbox.setCancelable(true);

        alertbox.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });

        AlertDialog alertbox2 = alertbox.create();
        alertbox2.show();
    }
}