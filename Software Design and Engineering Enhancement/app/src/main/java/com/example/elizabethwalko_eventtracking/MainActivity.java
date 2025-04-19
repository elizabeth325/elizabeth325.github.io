// MainActivity.java
package com.example.elizabethwalko_eventtracking;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

//class to handle main activity
public class MainActivity extends AppCompatActivity {
    //instance variables
    DatabaseHelper db;
    TableLayout eventTable;
    EditText eventNameEditText, eventDateEditText;
    Button addButton, updateButton, deleteButton, settingsButton;
    int selectedEventId = -1;

    //onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_event);

        Utils.applyPrimaryColor(this);


        db = new DatabaseHelper(this);
        eventTable = findViewById(R.id.event_table);
        eventNameEditText = findViewById(R.id.event_name);
        eventDateEditText = findViewById(R.id.event_date);
        addButton = findViewById(R.id.add_event_button);
        updateButton = findViewById(R.id.update_event_button);
        deleteButton = findViewById(R.id.delete_event_button);
        settingsButton = findViewById(R.id.settings_button);


        populateEventTable();

        //add event button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameEditText.getText().toString();
                String eventDate = eventDateEditText.getText().toString();
                //insert event into database
                if (db.insertEvent(eventName, eventDate)) {
                    Toast.makeText(MainActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                    populateEventTable();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add event", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //update event button click listener
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEventId != -1) {
                    String eventName = eventNameEditText.getText().toString();
                    String eventDate = eventDateEditText.getText().toString();
                    //update event in database
                    if (db.updateEvent(selectedEventId, eventName, eventDate)) {
                        Toast.makeText(MainActivity.this, "Event updated", Toast.LENGTH_SHORT).show();
                        populateEventTable();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to update event", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Select an event to update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //delete event button click listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedEventId != -1) {
                    //delete event from database
                    if (db.deleteEvent(selectedEventId)) {
                        Toast.makeText(MainActivity.this, "Event deleted", Toast.LENGTH_SHORT).show();
                        populateEventTable();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Select an event to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //settings button click listener
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    //method to ensure that the primary color is applied when the activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        Utils.applyPrimaryColor(this);
    }

    //method to populate event table
    private void populateEventTable() {
        eventTable.removeAllViews();

        // Add header row
        TableRow headerRow = new TableRow(this);
        TextView headerEventName = new TextView(this);
        TextView headerEventDate = new TextView(this);

        headerEventName.setText("Event Name");
        headerEventName.setPadding(8, 8, 8, 8);
        headerEventName.setTextSize(25);
        headerEventName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        headerEventDate.setText("Event Date");
        headerEventDate.setPadding(8, 8, 8, 8);
        headerEventDate.setTextSize(25);
        headerEventDate.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

        headerRow.addView(headerEventName);
        headerRow.addView(headerEventDate);
        eventTable.addView(headerRow);

        // Add event rows
        Cursor cursor = db.getAllEvents();
        if (cursor.getCount() == 0) {
            return;
        }

        // Loop through all rows and add to table
        while (cursor.moveToNext()) {
            TableRow row = new TableRow(this);
            TextView eventName = new TextView(this);
            TextView eventDate = new TextView(this);

            final int id = cursor.getInt(0);
            eventName.setText(cursor.getString(1));
            eventName.setPadding(8, 8, 8, 8);
            eventName.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            eventDate.setText(cursor.getString(2));
            eventDate.setPadding(8, 8, 8, 8);
            eventDate.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            row.addView(eventName);
            row.addView(eventDate);

            // Set on click listener for each row
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedEventId = id;
                    eventNameEditText.setText(eventName.getText().toString());
                    eventDateEditText.setText(eventDate.getText().toString());
                }
            });

            eventTable.addView(row);
        }
        cursor.close();
    }
}