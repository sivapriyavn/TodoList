package com.example.todoapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.example.todoapp.Adapter.AdapterClass;
import com.example.todoapp.Utils.DatabseHandler;
import com.example.todoapp.tasksample.TodoSample;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddnewTask extends BottomSheetDialogFragment {
    public static final String TAG="ActionBottondialog";

    private EditText newText;
    private Button savebtnnew,deletebtn;
    private DatabseHandler databseHandler;

    private AdapterClass adapter;


    public static AddnewTask newInsance()
    {
        return new AddnewTask();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL,R.style.DialogStyle);


    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.newtask,container,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;

    }



    @Override
    public void onViewCreated(@NonNull View view,@Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newText= Objects.requireNonNull(getView()).findViewById(R.id.edittask);
        savebtnnew=getView().findViewById(R.id.btnnewtask);
        deletebtn=getView().findViewById(R.id.deletebtn);

        databseHandler=new DatabseHandler(getActivity());
        databseHandler.openDatabase();

        boolean isUpdate=false;
        final Bundle bundle=getArguments();

        if (bundle!=null)
        {
            isUpdate=true;
            String netext=bundle.getString("task");
            newText.setText(netext);
            assert netext!=null;
            if (netext.length()>0)
            {
                savebtnnew.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.design_default_color_primary_dark));
            }}
        databseHandler=new DatabseHandler((getActivity()));
        databseHandler.openDatabase();
            newText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (s.toString().equals(""))
                    {
                        savebtnnew.setEnabled(false);
                        savebtnnew.setTextColor(Color.GRAY);
                    }
                    else {
                        savebtnnew.setEnabled(true);
                        savebtnnew.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()),R.color.design_default_color_on_primary));

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            boolean finalIsUpdate = isUpdate;
            savebtnnew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text=newText.getText().toString();
                    if (finalIsUpdate){
                        databseHandler.updateTask(bundle.getInt("id"),text);
                    }
                    else {
                        TodoSample task=new TodoSample();
                        task.setTask(text);
                        task.setStatus(0);
                        databseHandler.insertTask(task);
                    }
                    dismiss();
                }
            });
            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
                    builder.setTitle("Delete Task");
                    builder.setMessage("Are you sure you want to delete this Task?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //adapter.deleteItem(position);
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            });
        }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Activity activity=getActivity();
        if (activity instanceof DialogCloseListener)
        {
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }




}
