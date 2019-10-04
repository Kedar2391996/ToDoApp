package com.example.todo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText update_nm;
    EditText update_des;
    TextView update_date;
    TextView update_time;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        update_nm=findViewById(R.id.update_name);
        update_des=findViewById(R.id.update_description);
        update_date=findViewById(R.id.update_date);
        update_time=findViewById(R.id.update_time);
        toolbar=findViewById(R.id.item_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        id=getIntent().getExtras().getInt("id");
        String nm=getIntent().getExtras().getString("name");
        String date=getIntent().getExtras().getString("date");
        String time=getIntent().getExtras().getString("time");
        String description=getIntent().getExtras().getString("description");
        update_nm.setText(nm);
        update_date.setText(date);
        update_time.setText(time);
        update_des.setText(description);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void update(View view){
        DBHandler dbHandler=new DBHandler(this);
        String nm=update_nm.getText().toString();
        String date=update_date.getText().toString();
        String time=update_time.getText().toString();
        String description=update_des.getText().toString();
        if(nm.isEmpty()){
            update_nm.setError("Set Name");
        }
        else if(date.isEmpty()){
            update_date.setError("Set Date");
        }
        else if(time.isEmpty()){
            update_time.setError("Set Time");
        }
        else if(description.isEmpty()){
            update_des.setError("Set Description");
        }
        else {
            dbHandler.updateData(id,update_nm.getText().toString(),update_date.getText().toString(),update_time.getText().toString(),update_des.getText().toString());
            Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
        }

    }
    public void openCalendar(View view){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(EditActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        update_date.setText(day + "/" + (month+1) + "/" + year);
                    }
                },year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    public void openClock(View view){
        Calendar calendar = Calendar.getInstance();
        int  hour = calendar.get(Calendar.HOUR_OF_DAY);
        int  minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(EditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                Calendar datetime = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                datetime.set(Calendar.MINUTE, minutes);
                if(datetime.getTimeInMillis()>=calendar.getTimeInMillis()){
                    int hour = hourOfDay % 12;
                    update_time.setText(hour + ":" + minutes);
                }else{
                    Toast.makeText(EditActivity.this,"Invalid Time", Toast.LENGTH_LONG).show();
                }
            }
        },hour,minute ,true);
        timePickerDialog.show();
    }

}
