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

import android.speech.tts.TextToSpeech;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Home extends AppCompatActivity {

    private int score;
    TextToSpeech t1;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button save,next,lis;
    TextView qsNo,qsDesc;
    RadioGroup radioGroup;
    RadioButton rans,r1,r2,r3,r4;
    String answer,rightanswer,rightansh,rightansm,rightansb;
    boolean limitReached;
    static int index = 0;
    static int qsno = 1;
    static final ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        limitReached = false;
        lis= findViewById(R.id.listen);
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
        score = getIntent().getExtras().getInt("myscore");
        retrieveCompleteData();

     t1= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
         @Override
         public void onInit(int i) {
             if(i!=TextToSpeech.ERROR)
             {
                 if(getResources().getConfiguration().locale.toLanguageTag().equals("en-US")||getResources().getConfiguration().locale.toLanguageTag().equals("und"))
                 {
                     t1.setLanguage(new Locale("en","IN"));
                 }

                 if(getResources().getConfiguration().locale.toLanguageTag().equals("hi")||getResources().getConfiguration().locale.toLanguageTag().equals("mr"))
                 {
                     t1.setLanguage(Locale.forLanguageTag("hin"));
                     t1.setSpeechRate((float) 0.85);
                 }

                 if(getResources().getConfiguration().locale.toLanguageTag().equals("bn"))
                 {
                     Toast.makeText(getApplicationContext(),"IN BANGLA",Toast.LENGTH_LONG).show();
                     t1.setLanguage(Locale.forLanguageTag("bn-IN"));

                 }
                 //t1.setLanguage(Locale.forLanguageTag("HIN"));
             }
         }
     });

     lis.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             if(qsDesc.getText().toString().equalsIgnoreCase("How much is 2 + 22 + 222 - 222 - 22?"))
             {
                 Toast.makeText(getApplicationContext(),"in if",Toast.LENGTH_LONG).show();
                 t1.speak("How much is 2 plus 22 plus 222 minus 222 minus 22",TextToSpeech.QUEUE_FLUSH,null,null);
             }
             else {
                 t1.speak(qsDesc.getText(), TextToSpeech.QUEUE_FLUSH, null, null);
             }

             Toast.makeText(getApplicationContext(),getResources().getConfiguration().locale.toLanguageTag(),Toast.LENGTH_SHORT).show();


           t1.speak("Option 1",TextToSpeech.QUEUE_ADD,null,null);
           t1.speak(r1.getText(),TextToSpeech.QUEUE_ADD,null,null);
           t1.speak("Option 2",TextToSpeech.QUEUE_ADD,null,null);
           t1.speak(r2.getText(),TextToSpeech.QUEUE_ADD,null,null);
             t1.speak("Option 3",TextToSpeech.QUEUE_ADD,null,null);
             t1.speak(r3.getText(),TextToSpeech.QUEUE_ADD,null,null);
             t1.speak("Option 4",TextToSpeech.QUEUE_ADD,null,null);
             t1.speak(r4.getText(),TextToSpeech.QUEUE_ADD,null,null);
         }
     });


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

                t1.stop();
                if(limitReached==false) {
                        updateQuestion();
                }
                else{
                    //go to new activity
                    Intent i = new Intent(Home.this,TextBased.class);
                    i.putExtra("myscore",score);
                    startActivity(i);
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

    private void displayMcq() {
        qsNo.setText(String.valueOf(index / 9 + 1));
        if (arrayList.size() != 0) {
            Log.e("q in disMcq", arrayList.get(index).toString());
            if (getResources().getConfiguration().locale.toLanguageTag().equals("hi") || getResources().getConfiguration().locale.toLanguageTag().equals("mr") || getResources().getConfiguration().locale.toLanguageTag().equals("bn")) {
                if (index == 0) {
                    qsDesc.setText(R.string.QM1);
                    r1.setText(arrayList.get(index + 1).toString());
                    r2.setText(arrayList.get(index + 2).toString());
                    r3.setText(arrayList.get(index + 3).toString());
                    r4.setText(arrayList.get(index + 4).toString());
                    rightanswer = arrayList.get(index + 5).toString();
                    rightansb = arrayList.get(index + 6).toString();
                    rightansh = arrayList.get(index + 7).toString();
                    rightansm = arrayList.get(index + 8).toString();
                } else if (index == 9) {
                    qsDesc.setText(R.string.QM2);
                    r1.setText(R.string.QM2A1);
                    r2.setText(R.string.QM2A2);
                    r3.setText(R.string.QM2A3);
                    r4.setText(R.string.QM2A4);
                    rightanswer = arrayList.get(index + 5).toString();
                    rightansb = arrayList.get(index + 6).toString();
                    rightansh = arrayList.get(index + 7).toString();
                    rightansm = arrayList.get(index + 8).toString();
                } else if (index == 18) {
                    qsDesc.setText(R.string.QM3);
                    r1.setText(R.string.QM3A1);
                    r2.setText(R.string.QM3A2);
                    r3.setText(R.string.QM3A3);
                    r4.setText(R.string.QM3A4);
                    rightanswer = arrayList.get(index + 5).toString();
                    rightansb = arrayList.get(index + 6).toString();
                    rightansh = arrayList.get(index + 7).toString();
                    rightansm = arrayList.get(index + 8).toString();

                } else if (index == 27) {
                    qsDesc.setText(R.string.QM4);
                    r1.setText(R.string.QM4A1);
                    r2.setText(R.string.QM4A2);
                    r3.setText(R.string.QM4A3);
                    r4.setText(R.string.QM4A4);
                    rightanswer = arrayList.get(index + 5).toString();
                    rightansb = arrayList.get(index + 6).toString();
                    rightansh = arrayList.get(index + 7).toString();
                    rightansm = arrayList.get(index + 8).toString();

                } else if (index == 36) {
                    qsDesc.setText(R.string.QM5);
                    r1.setText(R.string.QM5A1);
                    r2.setText(R.string.QM5A2);
                    r3.setText(R.string.QM5A3);
                    r4.setText(R.string.QM5A4);
                    rightanswer = arrayList.get(index + 5).toString();
                    rightansb = arrayList.get(index + 6).toString();
                    rightansh = arrayList.get(index + 7).toString();
                    rightansm = arrayList.get(index + 8).toString();
                }
            } else {
                qsDesc.setText(arrayList.get(index + 0).toString());
                r1.setText(arrayList.get(index + 1).toString());
                r2.setText(arrayList.get(index + 2).toString());
                r3.setText(arrayList.get(index + 3).toString());
                r4.setText(arrayList.get(index + 4).toString());
                rightanswer = arrayList.get(index + 5).toString();
                rightansb = arrayList.get(index + 6).toString();
                rightansh = arrayList.get(index + 7).toString();
                rightansm = arrayList.get(index + 8).toString();
            }

        }
             else {
                Toast.makeText(Home.this, "Arraylist is empty", Toast.LENGTH_SHORT).show();
            }
        }




     private void updateQuestion(){
        try {
            if(qsno==5){
                saveAndScore();
                limitReached = true;
            }
            else {
                if (rans == null) {
                    Toast.makeText(getApplicationContext(), "Select a choice", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveAndScore();
                    index = index + 9;
                    if (index == 18) {
                        startActivity(new Intent(Home.this, imageinMCQ.class));
                    }
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

         if(answer.compareToIgnoreCase(rightanswer)==0||answer.compareToIgnoreCase(rightansb)==0||answer.compareToIgnoreCase(rightansh)==0||answer.compareToIgnoreCase(rightansm)==0)
         {
             score = score + 2;
             Toast.makeText(Home.this,"Correct! "+ String.valueOf(score),Toast.LENGTH_LONG).show();
             Log.e("MCQ SCORE",String.valueOf(score));
         }

            /*if (answer.equalsIgnoreCase(rightanswer)) {
                Toast.makeText(Home.this, "Correct", Toast.LENGTH_SHORT).show();
            }*/
            else {
                Log.e("MCQ SCORE",String.valueOf(score));
                Toast.makeText(Home.this, "Wrong "+ String.valueOf(score), Toast.LENGTH_SHORT).show();
            }
     }


    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),"Can't go back",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
        t1.stop();
    }
}


