package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tooltip.Tooltip;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int REQ_CODE = 35;
    List<String> items;

    FloatingActionButton addBtn;
    MainActionAdapter adapter;
    EditText editItem;
    RecyclerView itemList;
    ImageButton hintBtn;


    //public static boolean
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.addBtn);
        editItem = findViewById(R.id.editItem);
        itemList = findViewById(R.id.itemList);
        hintBtn = findViewById(R.id.hintBtn);
        readItems();

        Tooltip tooltip1 = new Tooltip.Builder(hintBtn).setText("Swipe=Delete | Tap=Edit").setGravity(Gravity.START).show();

        MainActionAdapter.OnClickListener onClickListener = new MainActionAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity", "One click at the position" + position);
                // Create a new activity
                Intent i = new Intent(MainActivity.this, EditTaskActivity.class);
                // Pass relevant edited data to the activity
                i.putExtra(KEY_ITEM_TEXT, items.get(position));
                i.putExtra(KEY_ITEM_POSITION, position);
                // Tell the system to display the activity
                startActivityForResult(i, REQ_CODE);
            }
        };


        adapter = new MainActionAdapter(items, onClickListener);
        itemList.setAdapter(adapter);
        itemList.setLayoutManager(new LinearLayoutManager(this));
        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(itemList);


        // Example for Set On Click Listener
        addBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoString = editItem.getText().toString();
                //Add an item to the model
                items.add(todoString);
                //Notify the adapter that an item is added
                adapter.notifyItemInserted(items.size() - 1);
                editItem.setText("");
                Toast add = makeText(getApplicationContext(), "Item added to the list !!", LENGTH_SHORT);
                add.setGravity(Gravity.CENTER, 0, 0);
                add.show();
                writeItems();
            }
        });
    }

    //Function to Handle the result of main activity

    //@SuppressLint("MissingSuperCall")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQ_CODE) {
            //To retrieve the updated text value
            String itemText = data.getStringExtra(KEY_ITEM_TEXT);
            //Extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            //Update the model at the right position with the new item
            items.set(position, itemText);
            // Notify the adapter
            adapter.notifyDataSetChanged();
            //Persist the changes
            writeItems();
        } else {
            Log.w("MainActivity", "Call to onActivityResult method");
        }
    }

    // Method to get the list of ToDo items
    private File getData() {
        return new File(getFilesDir(), "ToDo.txt");
    }

    //Method to read every item in the file. This loads the item each time the line is read
    //readItems will be called onlyonce when the app starts up
    private void readItems() {
        try {
            items = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getData(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Handling error in reading items", e);
            items = new ArrayList<>();
        }
    }

    //Method to save the item by writing into the file
    //writeItems will be called whenever we make change to the ToDoList
    private void writeItems() {
        try {
            FileUtils.writeLines(getData(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error Handling in writing items", e);
        }
    }

    //Delete an item from the list with swipe LEFT or RIGHT
    ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            //Remove the object from the list
            items.remove(viewHolder.getAdapterPosition());
            //Notify the adapter on the changes
            adapter.notifyDataSetChanged();
            // Toast message for the user to understand the action performed
            Toast remove = makeText(getApplicationContext(), "Item removed from the list !!", LENGTH_SHORT);
            remove.setGravity(Gravity.CENTER, 0, 0);
            remove.show();
           // int position = viewHolder.getAdapterPosition();
            //adapter.notifyItemRemoved(position);
        }
    };

}


