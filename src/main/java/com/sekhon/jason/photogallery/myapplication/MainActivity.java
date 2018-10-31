package com.sekhon.jason.photogallery.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sekhon.jason.photogallery.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int SEARCH_ACTIVITY_REQUEST_CODE = 0;
    static final int CAMERA_REQUEST_CODE = 1;
    //
    private ArrayList<String> photoGallery;
    //
    private TextView imageDate;
    private TextView filterDate;
    private EditText imageCaption;
    private TextView imageLon;
    private TextView imageLat;
    private ImageView iv;
    private CameraHelper cameraHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //CameraHelper
        cameraHelper = new CameraHelper(this, MainActivity.this);
        //UI elements
        Button btnLeft = (Button)findViewById(R.id.btnLeft);
        Button btnRight = (Button)findViewById(R.id.btnRight);
        Button btnFilter = (Button)findViewById(R.id.btnFilter);
        Button btnCamera = (Button)findViewById(R.id.btnCamera);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        imageDate = (TextView)findViewById(R.id.imageDate);
        filterDate = (TextView)findViewById(R.id.filterDate);
        imageCaption = (EditText) findViewById(R.id.caption);
        imageLon = (TextView) findViewById(R.id.lon);
        imageLat = (TextView) findViewById(R.id.lat);
        iv = (ImageView) findViewById(R.id.ivMain);

        //Listeners
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnSave.setOnClickListener(saveListener);
        btnFilter.setOnClickListener(filterListener);
        btnCamera.setOnClickListener(cameraListener);
        //
        photoGallery = cameraHelper.populateGallery(null, null,-180,-90, 180, 90, "");
        if (photoGallery.size() > 0)
            cameraHelper.setCurrentPhotoPath(photoGallery.get(cameraHelper.getCurrentPhotoIndex()));
        cameraHelper.displayPhoto(cameraHelper.getCurrentPhotoPath(), iv, imageDate, imageLon, imageLat, imageCaption);
    }

    private View.OnClickListener filterListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, SearchActivity.class);
            startActivityForResult(i, SEARCH_ACTIVITY_REQUEST_CODE);
        }
    };

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        public void onClick(View v) {
            cameraHelper.takePicture(v, MainActivity.this);
        }
    };

    private View.OnClickListener saveListener = new View.OnClickListener() {
        public void onClick(View v) {
            cameraHelper.saveCaption(v, imageCaption.getText().toString());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick( View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                cameraHelper.setCurrentPhotoIndex(cameraHelper.getCurrentPhotoIndex()-1);
                break;
            case R.id.btnRight:
                cameraHelper.setCurrentPhotoIndex(cameraHelper.getCurrentPhotoIndex()+1);
                break;
            default:
                break;
        }
        if (photoGallery.size() > 0) {
            if (cameraHelper.getCurrentPhotoIndex() < 0)
                cameraHelper.setCurrentPhotoIndex(0);
            if (cameraHelper.getCurrentPhotoIndex() >= photoGallery.size())
                cameraHelper.setCurrentPhotoIndex(photoGallery.size() - 1);

            cameraHelper.setCurrentPhotoPath(photoGallery.get(cameraHelper.getCurrentPhotoIndex()));
            cameraHelper.displayPhoto(cameraHelper.getCurrentPhotoPath(), iv, imageDate, imageLon, imageLat, imageCaption);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                photoGallery = cameraHelper.filterSortGallery(
                    data.getStringExtra("STARTDATE"),
                    data.getStringExtra("ENDDATE"),
                    data.getDoubleExtra("MINLON", -180),
                    data.getDoubleExtra("MINLAT", -90),
                    data.getDoubleExtra("MAXLON", 180),
                    data.getDoubleExtra("MAXLAT", 90),
                    data.getStringExtra("CAPTION"),
                    filterDate);
                cameraHelper.setCurrentPhotoIndex(0);
                if (photoGallery.size() > 0) {
                    cameraHelper.setCurrentPhotoPath(photoGallery.get(cameraHelper.getCurrentPhotoIndex()));
                    cameraHelper.displayPhoto(cameraHelper.getCurrentPhotoPath(), iv, imageDate, imageLon, imageLat, imageCaption);
                } else {
                    iv.setImageBitmap(null);
                    imageCaption.setText("");
                    imageDate.setText("");
                    imageLat.setText("");
                    imageLon.setText("");
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                photoGallery = cameraHelper.populateGallery(null, null, -180, -90, 180, 90, "");
                cameraHelper.setCurrentPhotoIndex(0);
                cameraHelper.setCurrentPhotoPath(photoGallery.get(cameraHelper.getCurrentPhotoIndex()));
                cameraHelper.displayPhoto(cameraHelper.getCurrentPhotoPath(), iv, imageDate, imageLon, imageLat, imageCaption);
            }
        }
    }
}
