package com.aariyan.linxtimeandbilling.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.aariyan.linxtimeandbilling.Adapter.TimingAdapter;
import com.aariyan.linxtimeandbilling.Database.DatabaseAdapter;
import com.aariyan.linxtimeandbilling.Interface.DeleteTiming;
import com.aariyan.linxtimeandbilling.MainActivity;
import com.aariyan.linxtimeandbilling.Model.CustomerModel;
import com.aariyan.linxtimeandbilling.Model.TimingModel;
import com.aariyan.linxtimeandbilling.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements DeleteTiming {

    private List<CustomerModel> list = new ArrayList<>();
    private Spinner spinner;

    private ImageView addTime, removeTime, jobs, infoList;
    private static String customerName = "";
    private RecyclerView recyclerView;
    private static String userName = "";

    DatabaseAdapter databaseAdapter;

    TimingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getIntent() != null) {
            userName = getIntent().getStringExtra("name");
            setTitle(String.format("Hello %s", userName));
        }
        databaseAdapter = new DatabaseAdapter(HomeActivity.this);
        list = databaseAdapter.getAlLCustomer();

        initUI();

        //loadData();
    }

    private void loadData() {
        List<TimingModel> list = databaseAdapter.getTiming(userName, customerName);
        //Toast.makeText(HomeActivity.this, "Size: " + list.size(), Toast.LENGTH_SHORT).show();
        adapter = new TimingAdapter(HomeActivity.this, list, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void initUI() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addTime = findViewById(R.id.addTime);
        removeTime = findViewById(R.id.removeTime);
        jobs = findViewById(R.id.jobs);
        infoList = findViewById(R.id.list);

        jobs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,JobsActivity.class));
            }
        });

        spinner = findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CustomerModel> adapter = new ArrayAdapter<CustomerModel>(this, android.R.layout.simple_spinner_item, list);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                customerName = adapterView.getItemAtPosition(i).toString();
                loadData();
                //Toast.makeText(HomeActivity.this, "" + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddTimeActivity.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("customerName", customerName);
                startActivity(intent);
            }
        });
    }

    @Override
    public void deleteTiming(String userName, String customerName, int id) {
        databaseAdapter.deleteTiming(userName, customerName,id);
        loadData();
    }
}