package com.isha.miniproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    EditText t1,t2,t3,t4,t5;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t1=findViewById(R.id.editText);
        t2=findViewById(R.id.editText2);
        t3=findViewById(R.id.editText3);
        t4=findViewById(R.id.editText4);
        t5=findViewById(R.id.editText5);
        pd=new ProgressDialog(MainActivity.this);

    }

    public void insert(View view) {
        pd.setMessage("Please wait");
        pd.setCancelable(true);
        pd.show();;
        String name=t1.getText().toString();
        String username=t2.getText().toString();
        String password=t3.getText().toString();
        String mobile=t4.getText().toString();
        String emergency=t5.getText().toString();
        RequestQueue rq= Volley.newRequestQueue(MainActivity.this);
        String url="http://malnirisha.in.net/project/insert.php?user="+username+"&nm="+name+"&emer="+emergency+"&mob="+mobile+"&pass="+password;
        StringRequest sr=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();
                if(response.trim().equals("0")){
                    Toast.makeText(MainActivity.this, "Username Exist", Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
                else if(response.trim().equals("1")){
                    Intent i = new Intent(MainActivity.this,Login.class);
                    startActivity(i);
                    pd.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
                pd.dismiss();

            }
        });
        sr.setShouldCache(false);
        sr.setRetryPolicy(new DefaultRetryPolicy(20*1000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);
    }


}
