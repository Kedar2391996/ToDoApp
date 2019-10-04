package com.example.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class AddTaskActivity extends AppCompatActivity {
        Toolbar toolbar;
        EditText tasknm;
        EditText taskdes;
        TextView taskdate;
        TextView tasktime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        tasknm=findViewById(R.id.taskname);
        taskdes=findViewById(R.id.description);
        taskdate=findViewById(R.id.date);
        tasktime=findViewById(R.id.time);
        toolbar=findViewById(R.id.addtask_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
    public void save(View view){
        DBHandler db=new DBHandler(this);
        String tnm=tasknm.getText().toString();
        String tdate=taskdate.getText().toString();
        String time=tasktime.getText().toString();
        String des=taskdes.getText().toString();
        if(tnm.isEmpty()) {
            tasknm.setError("Set Name");
        }

      else if(tdate.isEmpty()){
            taskdate.setError("Set Date");
        }

     else if(time.isEmpty()){
            tasktime.setError("Set time");
        }
     else if(des.isEmpty()){
         taskdes.setError("Set Description");
        }
      else{
          boolean isinserted=db.insertData(tnm,tdate,time,des);
          if(isinserted==true){
              Toast.makeText(AddTaskActivity.this,"data inserted",Toast.LENGTH_SHORT).show();
          }
          else {
              Toast.makeText(AddTaskActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
          }
        }
    }

    public void openCalendar(View view){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        taskdate.setText(day + "/" + (month+1) + "/" + year);
                    }
                },year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    public void openClock(View view){
        Calendar calendar = Calendar.getInstance();
      int  hour = calendar.get(Calendar.HOUR_OF_DAY);
      int  minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                Calendar datetime = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minutes);
                if(datetime.getTimeInMillis()>=calendar.getTimeInMillis()){
                    int hour = hourOfDay % 12;
                tasktime.setText(hour + ":" + minutes);
                }else{
                    Toast.makeText(AddTaskActivity.this,"Invalid Time", Toast.LENGTH_LONG).show();
                }
            }
        },hour,minute ,true);
        timePickerDialog.show();
    }

}
