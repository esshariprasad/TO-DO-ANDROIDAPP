package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class EditTaskActivity extends AppCompatActivity {

    EditText editTask;
    Button updateBtn;
    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        /* Reference to views in our layout */
        editTask = findViewById(R.id.editTask);
        updateBtn = findViewById(R.id.updateBtn);
        //User to understand which screen they are in the application
        getSupportActionBar().setTitle("Edit ToDo item Screen");
        editTask.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_TEXT));
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            //When user is done editing we can click on Update button
            public void onClick(View v) {
            //Create an intent  which hold the results
                Intent intent = new Intent();
                // Pass the data(results of editing)
                intent.putExtra(MainActivity.KEY_ITEM_TEXT,editTask.getText().toString());
                intent.putExtra(MainActivity.KEY_ITEM_POSITION,getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                //Set the result of the intent
                setResult(RESULT_OK, intent);
                //Finish the activity ,Close the screen and go back
                finish();
                Toast update = makeText(getApplicationContext(), "Item in the list updated successfully!!", LENGTH_SHORT);
                update.setGravity(Gravity.CENTER, 0, 0);
                update.show();
            }
        });


    }
}