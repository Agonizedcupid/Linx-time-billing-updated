package com.aariyan.linxtimeandbilling.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aariyan.linxtimeandbilling.Database.DatabaseAdapter;
import com.aariyan.linxtimeandbilling.MainActivity;
import com.aariyan.linxtimeandbilling.R;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddTimeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    int day, month, year;
    String date = "";

    int hour, minute;
    private static String amPm = "";

    private String startTimeDateStr = "", startTimeClockStr = "", endTimeDateStr = "OPEN", endTimeClockStr = "";
    private ImageView startTimeDate, startTimeClock, endTimeDate, endTimeClock, billableClock;
    private TextView totalTime, billableTime, startTimeText, endTimeText;
    private String spinnerSelection;
    private EditText descriptionOfWork;
    private Button saveBtn, ignoreBtn;

    private static String fDate, fTime, sDate, sTime;

    private static String userName = "", customerName = "";

    private String[] monthName = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

    private TimePickerDialog timePickerDialog;
    private String clicked = "";

    public static DecimalFormat df = new DecimalFormat("#.##");
    private static long totalMinutes;

    String billableT = "";

    DatabaseAdapter databaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time);

        databaseAdapter = new DatabaseAdapter(AddTimeActivity.this);

        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH);
        year = calendar.get(Calendar.YEAR);

        if (getIntent() != null) {
            setTitle(String.format("Hello %s", getIntent().getStringExtra("name")));

            userName = getIntent().getStringExtra("name");
            customerName = getIntent().getStringExtra("customerName");
        }

        initUI();
    }

    private void initUI() {

        billableClock = findViewById(R.id.billableClock);
        startTimeDate = findViewById(R.id.startTimeDate);
        startTimeClock = findViewById(R.id.startTimeClock);
        endTimeDate = findViewById(R.id.endTimeDate);
        endTimeClock = findViewById(R.id.endTimeClock);
        startTimeText = findViewById(R.id.startTimeText);
        endTimeText = findViewById(R.id.endTimeText);
        totalTime = findViewById(R.id.totalTime);
        billableTime = findViewById(R.id.billableTime);
        descriptionOfWork = findViewById(R.id.workDescription);
        saveBtn = findViewById(R.id.saveTime);
        ignoreBtn = findViewById(R.id.ignoreTime);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (endTimeDateStr.equals("OPEN")) {
                    //Save the value on database:
                    saveSQLite(endTimeDateStr);
                } else {
                    //Mark the job is done:
                    showTheAlertDialog();
                }
            }
        });

        startTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker("start");
            }
        });
        startTimeClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWatch("start");
            }
        });

        endTimeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(startTimeText.getText().toString())) {
//                    startTimeText.setError("Select the start time first!");
//                    startTimeText.requestFocus();

                    Toast.makeText(AddTimeActivity.this, "Select the start time first!", Toast.LENGTH_SHORT).show();

                    return;
                }
                showDatePicker("end");
            }
        });
        endTimeClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(startTimeText.getText().toString())) {
                    //startTimeText.setError("Select the start time first!");
                    //startTimeText.requestFocus();
                    Toast.makeText(AddTimeActivity.this, "Select the start time first!", Toast.LENGTH_SHORT).show();
                    return;
                }
                showWatch("end");
            }
        });

        billableClock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editBillableTime();
                billableT = "billable";
            }
        });


        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.workType, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerSelection = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void editBillableTime() {
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
        timePickerDialog.show();
    }

    private void showTheAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTimeActivity.this);
        builder.setTitle("Confirm");
        builder.setMessage("Do you wish to mark this job as completed?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                startActivity(new Intent(AddTimeActivity.this, HomeActivity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
                dialogInterface.cancel();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                saveSQLite(endTimeText.getText().toString());
                finish();
                //startActivity(new Intent(AddTimeActivity.this, HomeActivity.class));
                dialogInterface.cancel();
            }
        });

        builder.create();
        builder.show();
    }

    private void saveSQLite(String endTime) {
        long id = databaseAdapter.insertTimingData(userName, customerName, "" +
                        startTimeText.getText().toString(), billableTime.getText().toString(),
                "" + endTime, totalTime.getText().toString(), spinnerSelection,
                descriptionOfWork.getText().toString());

        Log.d("TIME_TESTING", "\n UserName: " + userName + "\n CustomerName: " + customerName +
                "\n startDate: " + startTimeText.getText().toString() + "\n endDate: " + endTime + "\n totalTime: " + totalTime.getText().toString()
                + "\n billableTime: " + billableTime.getText().toString() + "\n KM: " + spinnerSelection + "\n description: " + descriptionOfWork.getText().toString());

        if (id > 0) {
            Toast.makeText(AddTimeActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddTimeActivity.this, HomeActivity.class)
                    .putExtra("name", userName)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            //finish();
        } else {
            Toast.makeText(AddTimeActivity.this, "Facing error to save!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showWatch(String clickeds) {

        clicked = clickeds;
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, this, hour, minute, true);
        timePickerDialog.show();

//        TimePickerDialog dialog = new TimePickerDialog(AddTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker timePicker, int i, int i1) {
//                hour = i;
//                time = i1;
//
////                if (hour >= 12) {
////                    amPm = "PM";
////                } else {
////                    amPm = "AM";
////                }
//
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(0, 0, 0, hour, time);
//
//                String time = DateFormat.format("hh:mm", calendar).toString();
//
//                //String taskTime = time + " " + amPm;
//                String taskTime = time;
//                if (clicked.equals("start")) {
//                    startTimeClockStr = time;
//                    startTimeText.setText(String.format("%s", startTimeDateStr + " " + startTimeClockStr));
//                } else {
//                    endTimeClockStr = time;
//                    if (endTimeDateStr.equals("OPEN")) {
//                        endTimeDateStr = "";
//                    }
//                    endTimeText.setText(String.format("%s", endTimeDateStr + " " + endTimeClockStr));
//                }
//
//                Toast.makeText(AddTimeActivity.this, "You've selected " + taskTime, Toast.LENGTH_SHORT).show();
//            }
//        //}, 24, 0, true);
//        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
//        //dialog.updateTime(hour, time);
//        dialog.show();
    }

    private void showDatePicker(String clicked) {
        datePickerDialog = new DatePickerDialog(AddTimeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                int j = i1 + 1;

                //date = i + "-" + j + "-" + i2;
                date = i2 + " " + monthName[j - 1] + " " + i;
                //2022-1-15
                Log.d("TEST_DATE", date);
                if (clicked.equals("start")) {
                    startTimeDateStr = date;
                    fDate = i1 + "/" + i2 + "/" + i;
                    startTimeText.setText(String.format("%s", startTimeDateStr + " " + startTimeClockStr));
                } else {
                    endTimeDateStr = date;
                    if (endTimeDateStr.equals("OPEN")) {
                        endTimeDateStr = "";
                    }
                    sDate = i1 + "/" + i2 + "/" + i;
                    endTimeText.setText(String.format("%s", endTimeDateStr + " " + endTimeClockStr));
                }

                Toast.makeText(AddTimeActivity.this, "You've selected " + date, Toast.LENGTH_LONG).show();
                //Toast.makeText(AddProperty.this, "" + availableStatus, Toast.LENGTH_SHORT).show();

            }
        }, day, month, year);

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        String time = i + ":" + i1;

        if (billableT.equals("billable")) {
            String t = String.valueOf(i*60 + i1);
            billableTime.setText(t);
        } else if (clicked.equals("start")) {
            startTimeClockStr = time;
            fTime = startTimeClockStr;
            startTimeText.setText(String.format("%s", startTimeDateStr + " " + startTimeClockStr));
        } else {
            endTimeClockStr = time;
            if (endTimeDateStr.equals("OPEN")) {
                endTimeDateStr = "";
            }
            sTime = endTimeClockStr;
            calculateTotalTime();
            endTimeText.setText(String.format("%s", endTimeDateStr + " " + endTimeClockStr));
        }
    }

    private void calculateTotalTime() {
        //Finding total time:
//        String[] startTime = startTimeClockStr.split(":");
//        String[] endTime = endTimeClockStr.split(":");
//
//        int hour = Integer.parseInt(endTime[0]) - Integer.parseInt(startTime[0]);
//        int minutes = Integer.parseInt(endTime[1]) - Integer.parseInt(startTime[1]);
//        if (minute < 0) {
//            minute = minutes * (-1);
//        }
//
//        int totalTimes = hour * 60 + minutes;
//        totalTime.setText("" + totalTimes);

        String firstDateTime = fDate + " " + fTime;
        String secondDateTime = sDate + " " + sTime;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(firstDateTime);
            d2 = format.parse(secondDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //in milliseconds
        assert d2 != null;
        assert d1 != null;
        long diff = d2.getTime() - d1.getTime();

        long diffDays = diff / (24 * 60 * 60 * 1000);
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        totalMinutes = (diffDays * 24 * 60 + diffMinutes + diffHours * 60);

        totalTime.setText("" + totalMinutes);
        billableTime.setText("" + df.format((totalMinutes / 60)));
    }
}