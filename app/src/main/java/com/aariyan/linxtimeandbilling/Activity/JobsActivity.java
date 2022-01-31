package com.aariyan.linxtimeandbilling.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.aariyan.linxtimeandbilling.Adapter.JobAdapter;
import com.aariyan.linxtimeandbilling.Adapter.TimingAdapter;
import com.aariyan.linxtimeandbilling.Database.DatabaseAdapter;
import com.aariyan.linxtimeandbilling.Model.TimingModel;
import com.aariyan.linxtimeandbilling.R;

import java.util.ArrayList;
import java.util.List;

public class JobsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseAdapter databaseAdapter;
    private List<TimingModel> list = new ArrayList<>();
    private JobAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs);
        databaseAdapter = new DatabaseAdapter(JobsActivity.this);
        initUI();

        loadData();
    }

    private void loadData() {
        list = databaseAdapter.getAllJob();
        adapter = new JobAdapter(JobsActivity.this,list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerViews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}