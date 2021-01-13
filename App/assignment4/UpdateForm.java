package com.example.assignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateForm extends AppCompatActivity {

    String[] strarr = {"A", "B", "C"};
    String C_Selected = "A";
    String N_Selected = "A";
    private Log log;

    int chk(String a)
    {
        log.d("tag", a);
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
        setContentView(R.layout.activity_update_form);
        final EditText S_date = findViewById(R.id.Date);
        final Intent intent = getIntent();
        int n = intent.getIntExtra("num", 0);
        final TextView num = findViewById(R.id.Application);
        num.setText(Integer.toString(n));
        S_date.setText(intent.getStringExtra("Date"));
        S_date.setEnabled(false);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, strarr);
        final Spinner C_sec = findViewById(R.id.C_sec);
        C_sec.setAdapter(adapter);
        C_sec.setSelection(chk(intent.getStringExtra("C_sec")));
        C_sec.setEnabled(false);
        C_Selected=C_sec.getSelectedItem().toString();


        final Spinner N_sec = findViewById(R.id.N_sec);
        N_sec.setAdapter(adapter);
        N_sec.setSelection(chk(intent.getStringExtra("N_sec")));
        N_sec.setEnabled(false);
        N_Selected=N_sec.getSelectedItem().toString();


        final EditText Description = findViewById(R.id.Description);
        final EditText Roll_no = findViewById(R.id.Roll_no);
        Description.setText( intent.getStringExtra("Reason"));
        Roll_no.setText(intent.getStringExtra("Roll_no"));
        Description.setEnabled(false);
        Roll_no.setEnabled(false);
        final EditText comment=findViewById(R.id.comment_txt);
        comment.setText(intent.getStringExtra("comment"));
        comment.setEnabled(false);

        final EditText status=findViewById(R.id.status_txt);
        status.setText(intent.getStringExtra("status"));
        status.setEnabled(false);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Roll_no.getText().toString().isEmpty()) {
                    Toast.makeText(UpdateForm.this, "Enter Roll Number!", Toast.LENGTH_SHORT).show();
                    Roll_no.setError("Enter Roll No.!");
                } else if (C_Selected == N_Selected) {
                    Toast.makeText(UpdateForm.this, "Please Select Valid Section!", Toast.LENGTH_SHORT).show();
                    N_sec.setPrompt("Select Different Section!");
                    C_sec.setPrompt("Select Different Section!");
                } else if (Description.getText().toString().isEmpty()) {
                    Toast.makeText(UpdateForm.this, "Enter Description!", Toast.LENGTH_SHORT).show();
                    Description.setError("Enter Description Here!");
                } else {
                    Intent act = new Intent();
                    act.putExtra("Roll_no", Roll_no.getText().toString());
                    act.putExtra("Date", S_date.getText().toString());
                    act.putExtra("C_sec", C_Selected);
                    act.putExtra("N_sec", N_Selected);
                    act.putExtra("Reason", Description.getText().toString());
                    act.putExtra("comment",comment.getText().toString());
                    act.putExtra("status",status.getText().toString());
                    setResult(RESULT_OK, act);
                    finish();
                }
            }
        });

        Button update=findViewById(R.id.Update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.getText().toString().equals("Approved")){
                    Toast.makeText(UpdateForm.this,"Editing Not Possible",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    N_sec.setEnabled(true);
                    Description.setEnabled(true);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent act = new Intent();
        setResult(RESULT_CANCELED, act);
        finish();
    }
}


