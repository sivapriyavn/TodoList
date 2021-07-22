package com.example.todoapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Adapter.AdapterClass;
import com.example.todoapp.Utils.DatabseHandler;
import com.example.todoapp.tasksample.TodoSample;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView recyclerViewtask;
    private AdapterClass taskadapterClass;
    public DatabseHandler databseHandler;

    private FloatingActionButton fab;


    private List<TodoSample> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();


        databseHandler=new DatabseHandler(this);
        databseHandler.openDatabase();

        recyclerViewtask=findViewById(R.id.recycle);
        recyclerViewtask.setLayoutManager(new LinearLayoutManager(this));

        taskadapterClass=new AdapterClass(databseHandler,MainActivity.this);
        recyclerViewtask.setAdapter(taskadapterClass);


        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerTouchClass(taskadapterClass));
        itemTouchHelper.attachToRecyclerView(recyclerViewtask);
        fab=findViewById(R.id.fabbtn);


        taskList=databseHandler.getAllTask();
        Collections.reverse(taskList);
        taskadapterClass.setTasks(taskList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddnewTask.newInsance().show(getSupportFragmentManager(),AddnewTask.TAG);
            }
        });

        /*TodoSample task=new TodoSample();
        task.setTask("This is a sample task");
        task.setStatus(0);
        task.setId(1);

        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);
        taskList.add(task);

        taskadapterClass.setTasks(taskList);*/

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList=databseHandler.getAllTask();
        Collections.reverse(taskList);
        taskadapterClass.setTasks(taskList);
        taskadapterClass.notifyDataSetChanged();
    }
}