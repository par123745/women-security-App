package com.isha.miniproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class Service extends android.app.Service implements LocationListener {
    LocationManager lm;
    double a,b,c;
    List<String> list;
    double latt,longi;
   String user;
   Context co;



    @Override
    public IBinder onBind(Intent intent) {
        return null;

    }

    @Override
    public void onCreate() {

        startMyservice();
        super.onCreate();
        lm=(LocationManager)getSystemService(LOCATION_SERVICE);
        Criteria c=new Criteria();
        c.setAccuracy(Criteria.ACCURACY_COARSE);
        StringBuffer sb =new StringBuffer();
        list =lm.getProviders(c,true);
        if(list.isEmpty()){
          //  tp.setText("NO PROVIDER");
        }
        else{
            for(String x:list){
                sb.append(x).append(",");
                lm.requestSingleUpdate(x,this,null);
            }
          //  tp.setText("TOTAL PROVIDER="+sb);
        }
        a= SystemClock.uptimeMillis();
        SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
       user=sp.getString("username","");
    }

    private void startMyservice() {
        SharedPreferences sp=getSharedPreferences("location", Context.MODE_PRIVATE);
        SharedPreferences.Editor spe= sp.edit();
        spe.putString("lat",""+latt);
        spe.putString("long",""+longi);
        spe.commit();
        Handler h=new Handler(Looper.getMainLooper());
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
               Toast.makeText(Service.this, "Running", Toast.LENGTH_SHORT).show();
                RequestQueue rq= Volley.newRequestQueue(Service.this);
                String url="http://malnirisha.in.net/project/upload.php?lat="+latt+"&long="+longi+"&user="+user;
                StringRequest sr=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Service.this, "No Internet", Toast.LENGTH_SHORT).show();

                    }
                });
                sr.setShouldCache(false);
                sr.setRetryPolicy(new DefaultRetryPolicy(20*1000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rq.add(sr);
                startMyservice();
            }
        },10000);
    }

    @Override
    public void onLocationChanged(Location location) {
        //lat.setText("Latitude"+location.getLatitude());
        //lng.setText("Longitude"+location.getLongitude());
        //acc.setText("ACCURACY-"+location.getAccuracy());
        //cp.setText("Current provider-"+location.getProvider());
        b=SystemClock.uptimeMillis();
        c=(b-a)/1000;
        //time.setText("Time-"+c);
        latt=location.getLatitude();
        longi=location.getLongitude();
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

    public double getlatitude(){
        return latt;
    }

    public double getlongitude(){
        return longi;
    }
}
