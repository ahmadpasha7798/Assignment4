package com.example.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterUser extends AppCompatActivity {

    private String apiPath = "http://192.168.0.106/restexample/RestController.php?";
    private JSONArray restulJsonArray;
    private User user=null;
    String passwd;

    DBHelper db=new DBHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        final EditText id=findViewById(R.id.id_txt);
        final EditText pwd=findViewById(R.id.password_txt);
        final EditText pwd2=findViewById(R.id.password_txt2);
        Button btn= findViewById(R.id.log_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(pwd.getText().toString().isEmpty()||pwd2.getText().toString().isEmpty()||id.getText().toString().isEmpty())) {
                    if (pwd.getText().toString().equals(pwd2.getText().toString())) {
                        passwd=pwd.getText().toString();
                        GetUserData(id.getText().toString());
                        /*if (db.chk_user(id.getText().toString())) {
                            if (!db.chk_account(id.getText().toString())) {
                                String role;
                                if (db.chk_user_stu(id.getText().toString())) {
                                    role = "Student";
                                } else {
                                    role = db.getDesignation(id.getText().toString());
                                }
                                db.insert_user(id.getText().toString(), pwd.getText().toString(), role);
                                //Intent intent = new Intent(RegisterUser.this, Login_Activity.class);
                                //startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterUser.this, "Already Registered!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterUser.this, "Password Should Be Same!", Toast.LENGTH_SHORT).show();
                        }*/
                    } else {
                        Toast.makeText(RegisterUser.this, "Enter Same Password in both boxes!", Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(RegisterUser.this,"Empty Feilds", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void GetUserData(final String ID) {
        String url=apiPath+"func=getUser"+"&id="+ID;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject resultJsonObject = new JSONObject(response);

                    restulJsonArray = resultJsonObject.getJSONArray("output");
                    //Toast.makeText(MainActivity.this,restulJsonArray.toString(),Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = restulJsonArray.getJSONObject(0);

                    String id=jsonObject.getString("id");
                    if(id.equals("empty")){
                        GetStuData(ID);
                    }
                    else{
                        Toast.makeText(RegisterUser.this,"User Already Registered",Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(MainActivity.this,obj.S_date,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterUser.this," User No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }


    private void GetStuData(final String roll) {
        String url=apiPath+"func=getStudent"+"&id="+roll;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject resultJsonObject = new JSONObject(response);

                    restulJsonArray = resultJsonObject.getJSONArray("output");
                    //Toast.makeText(MainActivity.this,restulJsonArray.toString(),Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = restulJsonArray.getJSONObject(0);

                    String id=jsonObject.getString("rollno");
                    if(id.equals("empty")){
                        GetStaffData(roll);

                    }
                    else{
                        user=new User();
                        user.id=jsonObject.getString("rollno");
                        user.pwd=passwd;
                        user.role="Student";

                        InsertUser(user);
                    }
                    //Toast.makeText(MainActivity.this,obj.S_date,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterUser.this,"Stu No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }



    private void GetStaffData(final String roll) {
        String url=apiPath+"func=getStaff"+"&id="+roll;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject resultJsonObject = new JSONObject(response);

                    restulJsonArray = resultJsonObject.getJSONArray("output");
                    //Toast.makeText(MainActivity.this,restulJsonArray.toString(),Toast.LENGTH_SHORT).show();
                    JSONObject jsonObject = restulJsonArray.getJSONObject(0);

                    String id=jsonObject.getString("rollno");
                    if(!id.equals("empty")){

                        user=new User();
                        user.id=roll;
                        user.pwd=passwd;
                        user.role=jsonObject.getString("role");

                        InsertUser(user);

                    }
                    else{
                        Toast.makeText(RegisterUser.this,"User Already Registered",Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(MainActivity.this,obj.S_date,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterUser.this," Staff No Response"+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }


    private void InsertUser(User u) {
        String url=apiPath+"func=insertUser"+"&id="+u.id+"&pwd="+user.pwd+"&role="+user.role;
        RequestQueue queue= Volley.newRequestQueue(this);
        StringRequest jor=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(RegisterUser.this,"Account Registered",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(RegisterUser.this,"Error: "+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(jor);
    }
}