package com.example.lab8_2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseDatabase database;
    int initialID = 0;
    DatabaseReference databaseReference;
    private TextView taskTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance("https://appliedseng-lab8-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("tasks");

        // Initialize TextView
        taskTextView = findViewById(R.id.taskTextView);

        // Call method to fetch data
        fetchTasks();
    }

    public void fetchTasks() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder tasksText = new StringBuilder();

                // Iterate through all tasks in the database
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String task = snapshot.child("description").getValue(String.class);
                    if (task != null) {
                        tasksText.append(task).append("\n");
                    }
                }

                // Update the TextView with the retrieved tasks
                taskTextView.setText(tasksText.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching data", databaseError.toException());
            }
        });
    }

    public void addTask(View view) {
        // Read the txt from the input text
        @SuppressLint("WrongViewCast")
        EditText taskEditText =findViewById(R.id.taskDescription);
        String taskDescription = taskEditText.getText().toString(); initialID ++;
        Task newTask = new Task (String.valueOf(initialID), taskDescription);

        // Login to Firebase project and get instance of the DB and point to the root node of the DB
        database = FirebaseDatabase.getInstance("https://appliedseng-lab8-default-rtdb.firebaseio.com/");

        // Set reference to the Tasks table
        databaseReference = database.getReference("tasks");
        databaseReference.child(newTask.toString()).setValue(newTask);
    }
}