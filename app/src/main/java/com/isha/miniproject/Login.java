package com.isha.miniproject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class Login extends AppCompatActivity {
    EditText t1,t2;
    ProgressDialog pd;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        t1=findViewById(R.id.editText2);
        t2=findViewById(R.id.editText3);
        pd=new ProgressDialog(Login.this);
        SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
        status=sp.getInt("status",0);
        if(status==1){
            Intent i = new Intent(Login.this,Main2Activity.class);
            startActivity(i);
            finish();
        }



    }

    public void login(View view) {
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.show();;

        final String username=t1.getText().toString();
        String password=t2.getText().toString();

        RequestQueue rq= Volley.newRequestQueue(Login.this);
        String url="http://malnirisha.in.net/project/login.php?user="+username+"&pass="+password;
        StringRequest sr=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Login.this, response, Toast.LENGTH_SHORT).show();
                if(response.trim().equals("1")){

                    SharedPreferences sp=getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor spe= sp.edit();
                    spe.putInt("status",1);
                    spe.putString("username",username);
                    spe.commit();
                    Intent i = new Intent(Login.this,Main2Activity.class);
                    startActivity(i);
                    finish();
                }
                else{
                //    Toast.makeText(Login.this, "Login Incorrect", Toast.LENGTH_SHORT).show();
                }

                pd.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "No Internet", Toast.LENGTH_SHORT).show();
                pd.dismiss();

            }
        });
        sr.setShouldCache(false);
        sr.setRetryPolicy(new DefaultRetryPolicy(20*1000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rq.add(sr);

    }

    public void go(View view) {
        Intent i = new Intent(Login.this,MainActivity.class);
        startActivity(i);
    }
}
