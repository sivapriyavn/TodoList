package com.example.todoapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.AddnewTask;
import com.example.todoapp.MainActivity;
import com.example.todoapp.R;
import com.example.todoapp.Utils.DatabseHandler;
import com.example.todoapp.tasksample.TodoSample;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {

    private List<TodoSample> todoSampleList;
    private MainActivity activity;
    private DatabseHandler db;

//    public AdapterClass(DatabseHandler db, MainActivity activity)
//    {
//        this.db=db;
//        this.activity=activity;
//    }

    public AdapterClass(DatabseHandler db,MainActivity activity) {
        this.db=db;
              this.activity=activity;

    }


    public ViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View itemviewed= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tsk_todo,parent,false);
        return new ViewHolder(itemviewed);
    }

    public void onBindViewHolder(final ViewHolder viewHolder, int position)
    {
        db.openDatabase();
        final TodoSample todoSample=todoSampleList.get(position);
        viewHolder.checktask.setText(todoSample.getTask());
        viewHolder.checktask.setChecked(toBoolean(todoSample.getStatus()));
        viewHolder.checktask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    db.updateStatus(todoSample.getId(),1);
                }
                else {
                    db.updateStatus(todoSample.getId(),0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoSampleList.size();
    }

    public void setTasks(List<TodoSample> todoSampleList)
    {
        this.todoSampleList=todoSampleList;
        notifyDataSetChanged();
    }


    private boolean toBoolean(int n)
    {
        return n!=0;
    }

    public void editItem(int position)
    {
        TodoSample item=todoSampleList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddnewTask fragment = new AddnewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddnewTask.TAG);


    }
    public Context getContext() {
        return activity;
    }
    public void deleteItem(int position)
    {
        TodoSample item=todoSampleList.get(position);
        db.deleteTask(item.getId());
        todoSampleList.remove(position);
        notifyItemRemoved(position);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox checktask;

        ViewHolder(View view)
        {
            super(view);
            checktask=view.findViewById(R.id.checkbox);

        }
    }

}
