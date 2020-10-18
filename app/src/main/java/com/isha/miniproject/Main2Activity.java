package com.isha.miniproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        checkPermissions();
        Intent i=new Intent(Main2Activity.this,Service.class);
        startService(i);


    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(Main2Activity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(Main2Activity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return;
        }
    }

    public void log(View view) {
        SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor spe= sp.edit();
      spe.clear();
        spe.commit();

            Intent i = new Intent(Main2Activity.this,Login.class);
            startActivity(i);
            finish();

    }

    public void send(View view) {

        SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
        String user=sp.getString("username","null");

        SharedPreferences sp1=getSharedPreferences("location", Context.MODE_PRIVATE);
        final String lat=sp1.getString("lat","0");
        final String lon=sp1.getString("long","0");

        RequestQueue rq= Volley.newRequestQueue(Main2Activity.this);
        String url="http://malnirisha.in.net/project/get.php?user="+user;
        StringRequest sr=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            //    Toast.makeText(Main2Activity.this, ""+response, Toast.LENGTH_SHORT).show();
                String sms="I am in trouble, my location is "+lat+","+lon+" Please help me !!";
                SmsManager smsManager =SmsManager.getDefault();
                smsManager.sendTextMessage(response,null,sms,null,null);
                Toast.makeText(Main2Activity.this, "Sent", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Main2Activity.this, "No Internet", Toast.LENGTH_SHORT).show();

            }
        });
        sr.setShouldCache(false);
        sr.setRetryPolicy(new DefaultRetryPolicy(20*1000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);

    }
}
