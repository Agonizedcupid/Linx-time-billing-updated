package com.aariyan.linxtimeandbilling;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aariyan.linxtimeandbilling.Activity.HomeActivity;
import com.aariyan.linxtimeandbilling.Adapter.UserListAdapter;
import com.aariyan.linxtimeandbilling.Database.DatabaseAdapter;
import com.aariyan.linxtimeandbilling.Interface.Authentication;
import com.aariyan.linxtimeandbilling.Model.CustomerModel;
import com.aariyan.linxtimeandbilling.Model.UserListModel;
import com.aariyan.linxtimeandbilling.Network.APIs;
import com.aariyan.linxtimeandbilling.Network.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Authentication {

    private RecyclerView recyclerView;
    private UserListAdapter adapter;

    private List<UserListModel> list = new ArrayList<>();

    DatabaseAdapter databaseAdapter;

    private View view;
    public static BottomSheetBehavior behavior;
    private EditText passwordField;
    private Button logInBtn;

    private TextView userName;

    private ProgressBar progressBar;

    //Customers:
    private List<CustomerModel> listOfCustomer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("User Login");

        databaseAdapter = new DatabaseAdapter(MainActivity.this);

        intiUI();
    }

    private void getCustomers() {
        APIs apIs = ApiClient.getClient().create(APIs.class);
        Call<ResponseBody> call = apIs.getCustomers("7");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONArray root = new JSONArray(response.body().string());
                    for (int i = 0; i < root.length(); i++) {
                        JSONObject single = root.getJSONObject(i);
                        String strCustName = single.getString("strCustName");
                        String strCustDesc = single.getString("strCustDesc");
                        String Uid = single.getString("Uid");

                        CustomerModel model = new CustomerModel(strCustName, strCustDesc, Uid);
                        listOfCustomer.add(model);
                    }
                    InsertCustomerOnBackground insertCustomerOnBackground = new InsertCustomerOnBackground(listOfCustomer);
                    insertCustomerOnBackground.execute();

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MainActivity.this, "" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intiUI() {

        progressBar = findViewById(R.id.progressbar);

        userName = findViewById(R.id.useNames);
        view = findViewById(R.id.bottomSheet);
        behavior = BottomSheetBehavior.from(view);
        passwordField = findViewById(R.id.passwordField);
        logInBtn = findViewById(R.id.logInBtn);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //list = databaseAdapter.getUserData();

        checkOfflineData();
    }

    private void checkOfflineData() {
        list.clear();
        list = databaseAdapter.getUserData();
        if (list.size() <= 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("No offline data found!");
            builder.setMessage("Do you want to sync data from server?");
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progressBar.setVisibility(View.VISIBLE);
                    getData();
                    //list.clear();
                    //list = databaseAdapter.getUserData();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            checkOfflineData();
                        }
                    }, 2000);
                    //checkOfflineData();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.create();
            builder.show();
            list.clear();
        } else {
            list.clear();
            list = databaseAdapter.getUserData();
            adapter = new UserListAdapter(MainActivity.this, list, this);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        //list.clear();
    }

    private void getData() {
        getCustomers();
        APIs apIs = ApiClient.getClient().create(APIs.class);
        Call<ResponseBody> call = apIs.getUsers("7");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONArray root = new JSONArray(response.body().string());
                    for (int i = 0; i < root.length(); i++) {
                        JSONObject singleObject = root.getJSONObject(i);
                        String uID = singleObject.getString("uID");
                        String strPinCode = singleObject.getString("strPinCode");
                        String strName = singleObject.getString("strName");
                        String intCompanyID = singleObject.getString("intCompanyID");

                        //UserListModel model = new UserListModel(uID, strPinCode, strName, intCompanyID);
                        //list.add(model);

                        long id = databaseAdapter.insertUserData(uID, strPinCode, strName, intCompanyID);
                    }
//                    adapter = new UserListAdapter(MainActivity.this, list);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(String name, String pinCode, String id) {
        userName.setText(String.format("Enter pin for %s", name));
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String pin = passwordField.getText().toString().trim();
                if (pin.equals(pinCode)) {
                    Toast.makeText(MainActivity.this, "Log In Success!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, HomeActivity.class)
                            .putExtra("name", name)
                            .putExtra("id", id)
                    );
                } else {
                    Toast.makeText(MainActivity.this, "Wrong Credential!", Toast.LENGTH_SHORT).show();
                }
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                passwordField.setText("");
            }
        });
    }


    public class InsertCustomerOnBackground extends AsyncTask<List<CustomerModel>, Integer, Long> {
        List<CustomerModel> list;

        public InsertCustomerOnBackground(List<CustomerModel> list) {
            this.list = list;
        }

        @Override
        protected Long doInBackground(List<CustomerModel>... lists) {

            for (int i = 0; i < list.size(); i++) {
                CustomerModel model = list.get(i);
                long id = databaseAdapter.insertCustomerData(model.getStrCustName(), model.getStrCustDesc(), model.getUid());
                Log.d("INSERT_STATUS", "" + id);
            }

            return (long) 1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if (aLong == 1) {
                Toast.makeText(MainActivity.this, "Customer synced finished!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}