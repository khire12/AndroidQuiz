package com.example.khire.androidquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialMultiAutoCompleteTextView;

public class DashboardActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    Button signout,instr,startTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        signout = findViewById(R.id.sign_out);
        instr = findViewById(R.id.instructions_manual);
        startTest = findViewById(R.id.start_test);
        mAuth = FirebaseAuth.getInstance();


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                finish();
            }
        });

        startTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this,Home.class));
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

        mAuth.signOut();

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Press SignOut to sign-out",Toast.LENGTH_SHORT).show();
    }
}
