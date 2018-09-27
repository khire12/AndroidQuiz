package com.example.khire.androidquiz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    Button save,next;
    TextView qsNo,qsDesc;
    RadioGroup radioGroup;
    RadioButton rans,r1,r2,r3,r4;
    String answer,rightanswer;
    boolean limitReached;
    static int index = 0;
    static int qsno = 1;
    static final ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        limitReached = false;
        save = findViewById(R.id.save);
        next = findViewById(R.id.next);
        qsNo = findViewById(R.id.quesNo);
        qsDesc = findViewById(R.id.quesText);
        r1 = findViewById(R.id.radioButton1);
        r2 = findViewById(R.id.radioButton2);
        r3 = findViewById(R.id.radioButton3);
        r4 = findViewById(R.id.radioButton4);
        database = FirebaseDatabase.getInstance("https://androidquiz-22fda.firebaseio.com/");
        myRef = database.getReference().child("root").child("quiz").child("MCQ");
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();


        retrieveCompleteData();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(rans == null){
                    Toast.makeText(getApplicationContext(),"Select a choice",Toast.LENGTH_SHORT).show();
                }
                else {
                    saveAndScore();

                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(limitReached==false) {
                    if (rans == null) {
                        Toast.makeText(getApplicationContext(), "Select a choice", Toast.LENGTH_SHORT).show();
                    } else {
                        updateQuestion();
                    }
                }
                else{
                    //go to new activity
                    startActivity(new Intent(Home.this,TextBased.class));
                    overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                 rans = findViewById(checkedId);
                if(rans != null){
                    answer = rans.getText().toString();
                }
            }
        });

    }

    private void retrieveCompleteData() {

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for(int i=1;;i++) {
                        if(dataSnapshot.child("q"+i).exists() == false) {
                            break;
                        }
                        for (DataSnapshot postSnapshot : dataSnapshot.child("q" + i).getChildren()) {

                            arrayList.add(postSnapshot.getValue().toString());
                            Log.e("Added value", postSnapshot.getValue().toString());
                        }

                    }
                    displayMcq();
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void displayMcq(){
            qsNo.setText(String.valueOf(index/6 + 1));
            if(arrayList.size()!=0) {
                Log.e("q in disMcq", arrayList.get(index).toString());
                qsDesc.setText(arrayList.get(index+0).toString());
                r1.setText(arrayList.get(index+1).toString());
                r2.setText(arrayList.get(index+2).toString());
                r3.setText(arrayList.get(index+3).toString());
                r4.setText(arrayList.get(index+4).toString());
                rightanswer = arrayList.get(index+5).toString();
            }
            else{
                Toast.makeText(Home.this,"Arraylist is empty",Toast.LENGTH_SHORT).show();
            }
     }



     private void updateQuestion(){
        try {
            if(rans == null){
                Toast.makeText(getApplicationContext(),"Select a choice",Toast.LENGTH_SHORT).show();
            }
            else {

                if(qsno==5){
                    limitReached = true;
                }
                else {
                    saveAndScore();
                    index = index + 6;
                    displayMcq();
                    radioGroup.clearCheck();
                    qsno++;
                }
            }
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
     }


     private void saveAndScore(){
            if (answer.equalsIgnoreCase(rightanswer)) {
                Toast.makeText(Home.this, "Correct", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Home.this, "Wrong", Toast.LENGTH_SHORT).show();
            }
     }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Can't go back",Toast.LENGTH_SHORT).show();
    }
}


