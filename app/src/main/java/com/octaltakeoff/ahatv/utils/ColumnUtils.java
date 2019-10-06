package com.octaltakeoff.ahatv.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

/**
 * This class calculates the number of items in a columns with  given width of the item
 */


public class ColumnUtils extends AppCompatActivity {



    public static int calculateNoOfColumns(Context context){

        int itemWidth;
        int orientation = context.getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            itemWidth = 104;

        }else{
            itemWidth = 120;
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int)(((dpWidth / itemWidth) + 0.5) -1);
        return noOfColumns;
    }

    public static int calculateNoOfColumns2(Context context){

        int itemWidth = 65;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) Math.floor(dpWidth / itemWidth);
        return noOfColumns;
    }

    public static int calculateIconWidth(Context context, int columns) {
        int margin = 16;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        int width = (int) Math.floor(((dpWidth/columns)-(margin))*displayMetrics.density);
        //int width = 136;
        return width;
    }
}
