package com.sekhon.jason.photogallery.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sekhon.jason.photogallery.mydb.*;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class CameraHelper extends AppCompatActivity {

    public static final int CAMERA_REQUEST_CODE = 1;
    private String currentPhotoPath = null;
    private int currentPhotoIndex = 0;
    private Context context;

    public DateFormat sDateFormat;
    public DateFormat sDateParse;

    private PhotoGalleryDBHelper dbHelper;
    private SQLiteDatabase db;

    private LocationManager lm;
    private double longitude;
    private double latitude;

    private static class QueryParams {
        String selection;
        String[] selectionArgs;

        QueryParams(String selection, String[] selectionArgs) {
            this.selection = selection;
            this.selectionArgs = selectionArgs;
        }
    }

    public CameraHelper(Context context, Activity a) {
        sDateParse = new SimpleDateFormat("yyyyMMdd");
        sDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        this.context = context;
        dbHelper = new PhotoGalleryDBHelper(context);
        db = dbHelper.getWritableDatabase();
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
        } else {
            ActivityCompat.requestPermissions(a,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    public int getCurrentPhotoIndex() {
        return currentPhotoIndex;
    }

    public void setCurrentPhotoIndex(int currentPhotoIndex) {
        this.currentPhotoIndex = currentPhotoIndex;
    }

    public String getCurrentPhotoPath() {
        return currentPhotoPath;
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    private class FetchQuery extends AsyncTask<QueryParams, Void, Cursor>{

        @Override
        protected Cursor doInBackground(QueryParams... params) {
            Cursor c = db.query(PhotoGalleryDBBuilder.PhotoGalleryEntry.TABLE_NAME,
                    null, params[0].selection, params[0].selectionArgs, null,
                    null, null);
            return c;
        }
    }

    public ArrayList<String> populateGallery(Date minDate, Date maxDate, double minlon, double minlat, double maxlon, double maxlat, String caption){
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath(), "/Android/data/com.sekhon.jason.photogallery/files/Pictures");
        ArrayList<String> photoGallery = new ArrayList<String>();
        File[] fList = file.listFiles();
        if (fList != null) {
            if(minDate != null && maxDate != null) {
                String selection = PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_DATE + " BETWEEN date(?) AND date(?) AND "
                        + PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_LATITUDE + " BETWEEN ? AND ? AND "
                        + PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_LONGITUDE + " BETWEEN ? AND ? AND "
                        + "instr(" + PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_CAPTION + ", ?) > 0";
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String[] selectionArgs = { df.format(minDate), df.format(maxDate), minlat+"", maxlat+"", minlon+"", maxlon+"", caption };
                QueryParams qp = new QueryParams(selection, selectionArgs);
                FetchQuery fetchTask = new FetchQuery();
                try {
                    Cursor c = fetchTask.execute(qp).get();
                    while(c.moveToNext()) {
                        photoGallery.add(c.getString(c.getColumnIndexOrThrow(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_PATH)));
                    }
                    c.close();
                    return photoGallery;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else{
                String selection = PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_LATITUDE + " BETWEEN ? AND ? AND "
                    + PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_LONGITUDE + " BETWEEN ? AND ? AND "
                    + "instr(" + PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_CAPTION + ", ?) > 0";
                String[] selectionArgs = { minlat+"", maxlat+"", minlon+"", maxlon+"", caption };
                QueryParams qp = new QueryParams(selection, selectionArgs);
                FetchQuery fetchTask = new FetchQuery();
                try {
                    Cursor c = fetchTask.execute(qp).get();
                    while(c.moveToNext()) {
                        photoGallery.add(c.getString(c.getColumnIndexOrThrow(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_PATH)));
                    }
                    c.close();
                    return photoGallery;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }
        return photoGallery;
    }

    public void displayPhoto(String path, ImageView iv, TextView imageDate, TextView imageLon, TextView imageLat, EditText imageCaption) {
        iv.setImageBitmap(BitmapFactory.decodeFile(path));
        String date = "";
        String lon = "";
        String lat = "";
        String caption = "";
        String[] selectionArgs = {path};
        if (path != null) {
            QueryParams qp = new QueryParams(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_PATH + " = ?", selectionArgs);
            FetchQuery fetchTask = new FetchQuery();
            try {
                Cursor c = fetchTask.execute(qp).get();
                while (c.moveToNext()) {
                    date = c.getString(c.getColumnIndexOrThrow(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_DATE));
                    lon = c.getString(c.getColumnIndexOrThrow(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_LONGITUDE));
                    lat = c.getString(c.getColumnIndexOrThrow(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_LATITUDE));
                    caption = c.getString(c.getColumnIndexOrThrow(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_CAPTION));
                    imageDate.setText(date);
                    imageLon.setText(String.format("Lon: %s", lon));
                    imageLat.setText(String.format("Lat: %s", lat));
                    imageCaption.setText(caption);
                }
                c.close();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    public void takePicture(View v, Activity a) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(a.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile(a);
            } catch (IOException ex) {
                Log.d("FileCreation", "Failed");
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.sekhon.jason.photogallery.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                a.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    public void saveCaption(View v, String caption){
        String[] selectionArgs = {currentPhotoPath};
        ContentValues values = new ContentValues();
        values.put(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_CAPTION, caption);
        db.update(PhotoGalleryDBBuilder.PhotoGalleryEntry.TABLE_NAME,
                values,
                PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_PATH + " = ?",
                selectionArgs);
    }

    public File createImageFile(Activity a) throws IOException {
        String imageFileName = "JPEG_";
        File dir = a.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", dir );
        currentPhotoPath = image.getAbsolutePath();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = df.format(new Date());
        ContentValues values = new ContentValues();
        values.put(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_DATE, currentDate);
        values.put(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_LONGITUDE, longitude);
        values.put(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_LATITUDE, latitude);
        values.put(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_CAPTION, "Caption");
        values.put(PhotoGalleryDBBuilder.PhotoGalleryEntry.COLUMN_NAME_PATH, currentPhotoPath);
        db.insert(PhotoGalleryDBBuilder.PhotoGalleryEntry.TABLE_NAME, null, values);
        return image;
    }

    public ArrayList<String> filterSortGallery(String startDate, String endDate, double minlon,
                                               double minlat, double maxlon, double maxlat, String caption, TextView filterDate){
        if(startDate != null) {
            try {
                Date parsedStartDate = new SimpleDateFormat("yyyy/MM/dd").parse(startDate);
                Date parsedEndDate = new SimpleDateFormat("yyyy/MM/dd").parse(endDate);
                filterDate.setText(String.format("%s\n%s", startDate, endDate));
                return populateGallery(parsedStartDate, parsedEndDate, minlon, minlat, maxlon, maxlat, caption);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return populateGallery(null, null, minlon, minlat, maxlon, maxlat, caption);
    }
}
