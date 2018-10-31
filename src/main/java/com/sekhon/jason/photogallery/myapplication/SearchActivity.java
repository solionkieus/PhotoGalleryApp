package com.sekhon.jason.photogallery.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.sekhon.jason.photogallery.R;

import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {

    private EditText fromDate;
    private EditText toDate;
    private EditText minLat;
    private EditText maxLat;
    private EditText minLon;
    private EditText maxLon;
    private EditText caption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        fromDate = (EditText) findViewById(R.id.search_fromDate);
        toDate   = (EditText) findViewById(R.id.search_toDate);
        minLat   = (EditText) findViewById(R.id.minLat);
        maxLat   = (EditText) findViewById(R.id.maxLat);
        minLon   = (EditText) findViewById(R.id.minLon);
        maxLon   = (EditText) findViewById(R.id.maxLon);
        caption  = (EditText) findViewById(R.id.caption);
        new DateInputMask(fromDate);
        new DateInputMask(toDate);
    }

    public void cancel(final View v) {
        finish();
    }

    public void search(final View v) {
        double minLonTemp;
        double minLatTemp;
        double maxLonTemp;
        double maxLatTemp;
        Intent i = new Intent();
        minLonTemp = (minLon.getText().toString().equals("")) ? -180 : Double.parseDouble(minLon.getText().toString());
        minLatTemp = (minLat.getText().toString().equals("")) ? -90 : Double.parseDouble(minLat.getText().toString());
        maxLonTemp = (maxLon.getText().toString().equals("")) ? 180 : Double.parseDouble(maxLon.getText().toString());
        maxLatTemp = (maxLat.getText().toString().equals("")) ? 90 : Double.parseDouble(maxLat.getText().toString());
        i.putExtra("STARTDATE", fromDate.getText().toString());
        i.putExtra("ENDDATE", toDate.getText().toString());
        i.putExtra("MINLON", minLonTemp);
        i.putExtra("MAXLON", maxLonTemp);
        i.putExtra("MINLAT", minLatTemp);
        i.putExtra("MAXLAT", maxLatTemp);
        i.putExtra("CAPTION", "" + caption.getText().toString());
        setResult(RESULT_OK, i);
        finish();
    }
}
