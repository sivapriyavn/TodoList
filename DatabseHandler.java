package com.example.todoapp.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import com.example.todoapp.tasksample.TodoSample;

import java.util.ArrayList;
import java.util.List;

public class DatabseHandler extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String NAME="todoDatabase";
    private static final String TODO_TABLE="todo";
    private static final String ID="id";
    private static final String TASK="task";
    private static final String STATUS="status";
    private static final String CREATE_TODO_TABLE="CREATE TABLE "+TODO_TABLE+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                    +TASK+" TEXT, "+STATUS+" INTEGER) ";
    private SQLiteDatabase database;

    public DatabseHandler(Context context) {
        super(context, NAME, null,VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TODO_TABLE);
        onCreate(db);

    }
    public void openDatabase()
    {
        database=this.getWritableDatabase();
    }

    public void insertTask(TodoSample task)
    {
        ContentValues cv=new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS,0);
        database.insert(TODO_TABLE,null,cv);
    }

    public List<TodoSample> getAllTask()
    {
        List<TodoSample> taskList=new ArrayList<>();
        Cursor cur=null;
        database.beginTransaction();
        try
        {
            cur=database.query(TODO_TABLE,null,null,null,null,null,null,null);
            if (cur!=null)
            {
                if (cur.moveToFirst())
                {
                    do {
                        TodoSample task=new TodoSample();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }while(cur.moveToNext());
                }
            }
        }finally {
            database.endTransaction();
            assert cur!=null;
            cur.close();

        }
        return taskList;

    }
    public void updateStatus(int id, int status)
    {
        ContentValues cv=new ContentValues();
        cv.put(STATUS,status);
        database.update(TODO_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task)
    {
        ContentValues cv=new ContentValues();
        cv.put(TASK,task);
        database.update(TODO_TABLE,cv,ID+"=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id)
    {
        database.delete(TODO_TABLE,ID+"=?", new String[]{String.valueOf(id)});
    }

}
