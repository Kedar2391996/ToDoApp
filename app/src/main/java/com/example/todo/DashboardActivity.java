package com.example.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    DashboardActivity activity;
    Toolbar dashboard_toolbar;
    RecyclerView rv_dashboard;
    FloatingActionButton fab_dashboard;
    DBHandler dbHandler;
    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        rv_dashboard = findViewById(R.id.rv_dashboard);
        rv_dashboard.setLayoutManager(new LinearLayoutManager(activity));
        fab_dashboard = findViewById(R.id.fab_dashboard);
        t1=findViewById(R.id.text1);
        setSupportActionBar(dashboard_toolbar);
        setTitle("Dashboard");
        activity = this;
         dbHandler=new DBHandler(this);
        //DashboardAdapter adapter=new DashboardAdapter(dbHandler.getAlltask(),activity);
      //  rv_dashboard.setAdapter(adapter);

    }
    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }

    public void getSpeechInput(View view){
        int x=10;
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        if(intent.resolveActivity(getPackageManager())!=null)
        {
            startActivityForResult(intent,10);
        }
        else
        {
            Toast.makeText(this,"not supported",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case 10:
                if(resultCode== RESULT_OK && data !=null)
                {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    t1.setText(result.get(0));
                    String s=result.get(0);
                    if(s.equals("add task"))
                    {
                        Intent x= new Intent(this,AddTaskActivity.class);
                        startActivity(x);
                    }
                    else
                    {
                        t1.setText("match not found");
                    }

                }
                break;
        }
    }
    public void refreshList() {
        rv_dashboard.setAdapter(new DashboardAdapter(dbHandler.getAlltask(),activity));
    }
    public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
        List<Task> list;
        DashboardActivity activity;
        public DashboardAdapter(List<Task> list,DashboardActivity activity) {
            this.list = list;
            this.activity=activity;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context=viewGroup.getContext();
            LayoutInflater inflater=LayoutInflater.from(context);
            View tv=inflater.inflate(R.layout.rv_child_dashboard,viewGroup,false);
            ViewHolder viewHolder=new ViewHolder(tv);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder viewHolder,final int i) {
            viewHolder.todotask.setText(list.get(i).getId()+list.get(i).getTname()+list.get(i).getDate()
                    +list.get(i).getTime()+list.get(i).getDescription());
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu menu=new PopupMenu(activity,viewHolder.imageView);
                    menu.inflate(R.menu.rv_menu_dashboard);
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.delete:
                                    AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                                    builder.setTitle("Are You Sure");
                                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.dbHandler.deleteData(list.get(i).getId());
                                            activity.refreshList();
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });
                                    builder.show();
                                    break;
                                case R.id.edit:
                                    Intent intent=new Intent(activity,EditActivity.class);
                                    intent.putExtra("id",list.get(i).getId());
                                    intent.putExtra("name",list.get(i).getTname());
                                    intent.putExtra("date",list.get(i).getDate());
                                    intent.putExtra("time",list.get(i).getTime());
                                    intent.putExtra("description",list.get(i).getDescription());
                                    startActivity(intent);
                                    break;
                            }
                            return true;
                        }
                    });
                    menu.show();
                }

            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView todotask;
            ImageView imageView;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                todotask=itemView.findViewById(R.id.tv_todo_name);
                imageView=itemView.findViewById(R.id.iv_menu);
            }
        }
    }

}
