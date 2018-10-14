package com.example.khire.androidquiz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.zip.Inflater;

public class ResultActivity extends AppCompatActivity {

    private TextView marks,expMarks,expDesc;
    private int score;
    private Button goTodash,moreInfo,okButton;
    LayoutInflater inflater ;
    View explore_yourself;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        score = getIntent().getExtras().getInt("myscore");
        marks = findViewById(R.id.marks);
        goTodash = findViewById(R.id.go_to_dashboard);

        moreInfo = findViewById(R.id.more_about_you);
        marks.setText(String.valueOf(score));

        inflater = this.getLayoutInflater();

        explore_yourself = inflater.inflate(R.layout.explore_yourself,null);
        okButton = explore_yourself.findViewById(R.id.ok);
        expMarks =  explore_yourself.findViewById(R.id.your_evaluation);
        expDesc = explore_yourself.findViewById(R.id.desc);







        goTodash.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(),DashboardActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
        }
    });

    moreInfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            generateResult();
        }
    });

    okButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           startActivity(new Intent(getApplicationContext(),DashboardActivity.class)
                   .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
            overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
        }
    });


    }


    private void generateResult(){



        setContentView(explore_yourself);

        expMarks.setText("Your Marks : "+String.valueOf(score));

        if(score>=24 && score<=30)
        {
            expDesc.setText("NO COGNITIVE IMPAIRMENT");

        }
        else if(score>=18 && score<24)
        {
            expDesc.setText("MILD COGNITIVE IMPAIRMENT");
        }
        else
        {
            expDesc.setText("SEVERE COGNITIVE IMPAIRMENT");
        }
/*
        if(score<21)
        {
           // t8.setText("ABNORMAL IF YOU HAVE COMPLETED ATLEAST 8 YRS OF SCHOOL EDUCATION");
        }
        else if(score<23 && score>=21 )
        {
          //  t8.setText("ABNORMAL IF YOU FINISHED HIGH SCHOOL");
        }
        else if(score==23|| score==24)
        {
           // t8.setText("ABNORMAL IF YOU FINISHED COLLEGE");
        }
        else {
            //t8.setText("NO ABNORMALITY DETECTED");
        }

        */
    }





    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Can't go back",Toast.LENGTH_SHORT).show();
    }
}
