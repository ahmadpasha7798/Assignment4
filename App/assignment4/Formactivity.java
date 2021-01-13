package com.example.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Formactivity extends AppCompatActivity {
    String[] strarr={"A","B","C"};
    String C_Selected="A";
    String N_Selected="A";
    private User user=null;
    private Calendar calender;
    String id;
    private SimpleDateFormat dateFormat;
    DBHelper db=new DBHelper(this);


    private String apiPath = "http://192.168.0.106/restexample/RestController.php?";
    private JSONArray restulJsonArray;
    Spinner C_sec;

    int chk(String a)
    {
        for(int i=0;i<3;i++)
        {
            if(a.equals(strarr[i])) {
                return i ;
            }
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formactivity);
        final EditText S_date=findViewById(R.id.Date);
        //getActionBar().setTitle("Form");

        id=getIntent().getStringExtra("Username");
        calender = calender.getInstance();
        dateFormat=new SimpleDateFormat("dd/MM/YYYY");
        S_date.setText(dateFormat.format(calender.getTime()));
        S_date.setEnabled(false);
        ArrayAdapter adapter=new ArrayAdapter<String>(Formactivity.this, R.layout.support_simple_spinner_dropdown_item, strarr);
        C_sec=findViewById(R.id.C_sec);
        C_sec.setAdapter(adapter);
        GetStuData(id);
        //Cursor res=db.get_Studata();
        //res.moveToFirst();

        final Spinner N_sec=findViewById(R.id.N_sec);
        N_sec.setAdapter(adapter);
        N_sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                N_Selected =strarr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                N_Selected=strarr[1];
            }
        });
        final EditText Roll_no=findViewById(R.id.Roll_no);
        Roll_no.setText(id);
        Roll_no.setEnabled(false);
        final TextView num=findViewById(R.id.Application);
        final Intent intent=getIntent();
        int n=intent.getIntExtra("num", 0);
        num.setText(Integer.toString(n));
        Button btn=findViewById(R.id.submit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText Description=findViewById(R.id.Description);

                if(Roll_no.getText().toString().isEmpty())
                {
                    Toast.makeText(Formactivity.this, "Enter Roll Number!", Toast.LENGTH_SHORT).show();
                    Roll_no.setError("Enter Roll No.!");
                }
                else if(C_Selected==N_Selected)
                {
                    Toast.makeText(Formactivity.this, "Please Select Valid Section!", Toast.LENGTH_SHORT).show();
                    N_sec.setPrompt("Select Different Section!");
                    C_sec.setPrompt("Select Different Section!");
                }
                else if(Description.getText().toString().isEmpty())
                {
                    Toast.makeText(Formactivity.this, "Enter Description!", Toast.LENGTH_SHORT).show();
                    Description.setError("Enter Description Here!");
                }
                else
                {
                    Intent act=new Intent();
                    act.putExtra("Roll_no", Roll_no.getText().toString());
                    act.putExtra("Date", S_date.getText().toString());
                    act.putExtra("C_sec", C_Selected);
                    act.putExtra("N_sec", N_Selected);
                    act.putExtra("Reason", Description.getText().toString());
                    setResult(RESULT_OK,act);
                    finish();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent act=new Intent();
        setResult(RESULT_CANCELED,act);
        finish();
    }


    private void GetStuData(String id) {
        String url=apiPath+"func=getStudent"+"&id="+id;
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
                        user.id=id;
                        user.pwd=jsonObject.getString("name");
                        user.role=jsonObject.getString("section");

                        C_sec.setSelection(chk(user.role));
                        C_Selected=(String)C_sec.getSelectedItem();
                        C_sec.setEnabled(false);

                    }
                    //Toast.makeText(MainActivity.this,obj.S_date,Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Formactivity.this,"Error: "+error.toString(),Toast.LENGTH_SHORT).show();

            }
        });

        queue.add(jor);
    }

}