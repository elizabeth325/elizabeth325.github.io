// SettingsActivity.java
package com.example.elizabethwalko_eventtracking;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar redSeekBar, greenSeekBar, blueSeekBar;
    private View colorPreview;
    private Button saveButton, backButton;
    private int red = 0, green = 0, blue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Utils.applyPrimaryColor(this);

        redSeekBar = findViewById(R.id.red_seekbar);
        greenSeekBar = findViewById(R.id.green_seekbar);
        blueSeekBar = findViewById(R.id.blue_seekbar);
        colorPreview = findViewById(R.id.color_preview);
        saveButton = findViewById(R.id.save_button);
        backButton = findViewById(R.id.back_button);


        redSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        greenSeekBar.setOnSeekBarChangeListener(colorChangeListener);
        blueSeekBar.setOnSeekBarChangeListener(colorChangeListener);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("theme_color", Color.rgb(red, green, blue));
                editor.apply();
                finish();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    private SeekBar.OnSeekBarChangeListener colorChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == R.id.red_seekbar) {
                red = progress;
            } else if (seekBar.getId() == R.id.green_seekbar) {
                green = progress;
            } else if (seekBar.getId() == R.id.blue_seekbar) {
                blue = progress;
            }
            colorPreview.setBackgroundColor(Color.rgb(red, green, blue));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };
}