package com.example.khire.androidquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;

public class InstructionActivity extends AppCompatActivity {

    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InstructionActivity.this,DashboardActivity.class));
        overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
    }
}
