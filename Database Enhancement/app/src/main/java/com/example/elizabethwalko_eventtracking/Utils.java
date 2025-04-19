// Utils.java
package com.example.elizabethwalko_eventtracking;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class Utils {
    public static <Toolbar> void applyPrimaryColor(AppCompatActivity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        int themeColor = preferences.getInt("theme_color", Color.WHITE);


        // Apply the color to the background
        View rootView = activity.findViewById(android.R.id.content);
        if (rootView != null) {
            rootView.setBackgroundColor(themeColor);
        }
    }
}
