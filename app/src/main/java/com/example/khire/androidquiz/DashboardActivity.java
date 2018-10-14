package com.example.khire.androidquiz;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialMultiAutoCompleteTextView;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private int score;
    FirebaseAuth mAuth;
    Button signout,instr,startTest,langch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        langch= findViewById(R.id.lang);
        signout = findViewById(R.id.sign_out);
        instr = findViewById(R.id.instructions_manual);
        startTest = findViewById(R.id.start_test);
        mAuth = FirebaseAuth.getInstance();
        score = 0;

        langch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent i = new Intent(getApplicationContext(),ChooseLang.class);
               startActivity(i);
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOut();
            }
        });

        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this,Home.class);
                i.putExtra("myscore",score);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
            }
        });


        instr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(DashboardActivity.this,InstructionActivity.class));
                overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);

            }
        });


    }


    private void signOut(){

        Resources res= getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale= new Locale("en-US");
        res.updateConfiguration(conf,dm);
        mAuth.signOut();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Press SignOut to sign-out",Toast.LENGTH_SHORT).show();
    }
}
