package com.aariyan.linxtimeandbilling.Activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.aariyan.linxtimeandbilling.Adapter.JobAdapter;
import com.aariyan.linxtimeandbilling.Adapter.TimingAdapter;
import com.aariyan.linxtimeandbilling.Database.DatabaseAdapter;
import com.aariyan.linxtimeandbilling.MainActivity;
import com.aariyan.linxtimeandbilling.Model.PostingModel;
import com.aariyan.linxtimeandbilling.Model.TimingModel;
import com.aariyan.linxtimeandbilling.Network.APIs;
import com.aariyan.linxtimeandbilling.Network.ApiClient;
import com.aariyan.linxtimeandbilling.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class JobsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    DatabaseAdapter databaseAdapter;
    private List<TimingModel> list = new ArrayList<>();
    private JobAdapter adapter;

    private List<PostingModel> posting = new ArrayList<>();

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
        adapter = new JobAdapter(JobsActivity.this, list);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        formatData(list);
    }

    private void formatData(List<TimingModel> list) {
        posting.clear();
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).getStatus().equals("OPEN")) {
                posting.add(new PostingModel(
                        "" + list.get(i).getDescription(),
                        ""+System.currentTimeMillis(),
                        "" + list.get(i).getCustomerName(),
                        "",
                        "1642320000000",
                        "1642336200000",
                        "" + list.get(i).getTotalTime(),
                        "420",
                        "" + list.get(i).getUID(),
                        "0",
                        //"LL" + list.get(i).getWorkType(),
                        "" + list.get(i).getBillableTime(),
                        "",
                        "7"
                ));
            }
        }

        if (posting.size() > 0) {
            StringRequest mStringRequest = new StringRequest(
                    Request.Method.POST,
                    "http://102.37.0.48/TandB/postjobsnew.php",
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //Log.d("FEEDBACK", response);
                            Toast.makeText(JobsActivity.this, "Posted successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ) {
                @Override
                public byte[] getBody() throws AuthFailureError {
                    String jsonString = new Gson().toJson(posting).toString();
                    return jsonString.getBytes();
                }
            };
            Volley.newRequestQueue(JobsActivity.this).add(mStringRequest);
        } else {
            Toast.makeText(JobsActivity.this, "No completed jobs found!", Toast.LENGTH_SHORT).show();
        }


    }

    private void initUI() {
        recyclerView = findViewById(R.id.recyclerViews);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}