package com.example.khire.androidquiz;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.Locale;

public class ImageBased extends AppCompatActivity {

    private int score;
    ImageView imageView;
    EditText answerText;
    TextView qsNo,qstxt;
    String imageUrl_1,imageUrl_2;
    Button next,lis;
    TextToSpeech t1;
    static int index = 1;
    String rightanswer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_based);

        qstxt= findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        answerText = findViewById(R.id.editText);
        qsNo = findViewById(R.id.qsno1);
        next = findViewById(R.id.nextBtn);
        lis = findViewById(R.id.lit);
        score = getIntent().getExtras().getInt("myscore",score);
        imageUrl_1 = "https://firebasestorage.googleapis.com/v0/b/androidquiz-22fda.appspot.com/o/watch.jpg?alt=media&token=5cf81785-6650-41a5-b435-46433d333c26";
        imageUrl_2 = "https://firebasestorage.googleapis.com/v0/b/androidquiz-22fda.appspot.com/o/pencil.jpg?alt=media&token=b590cc37-1462-46f5-bc37-7a23973d1be9";


        t1= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {
                    if(getResources().getConfiguration().locale.toLanguageTag().equals("en-US") ||getResources().getConfiguration().locale.toLanguageTag().equals("und"))
                    {
                        t1.setLanguage(new Locale("en","IN"));
                    }

                    if(getResources().getConfiguration().locale.toLanguageTag().equals("hi")||getResources().getConfiguration().locale.toLanguageTag().equals("mr"))
                    {
                        t1.setLanguage(Locale.forLanguageTag("hin"));
                        t1.setSpeechRate((float)0.85);
                    }

                    if(getResources().getConfiguration().locale.toLanguageTag().equals("bn"))
                    {
                        Toast.makeText(getApplicationContext(),"IN BANGLA",Toast.LENGTH_LONG).show();
                        t1.setLanguage(Locale.forLanguageTag("bn-IN"));

                    }
                }
            }
        });


        lis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(qstxt.getText(),TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.stop();
                if(index>2){
                    Intent i = new Intent(getApplicationContext(),ResultActivity.class);
                    i.putExtra("myscore",score);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),"Finish in next",Toast.LENGTH_SHORT).show();
                }
                else {
                    nextQuestion();
                }
            }
        });

    }

    private void setImage(){
        if(index<=2){
            qsNo.setText(String.valueOf(index));
        }
        if(index == 1){
            Glide.with(getApplicationContext()).load(imageUrl_1).into(imageView);
        }
        else if (index==2){
            Glide.with(getApplicationContext()).load(imageUrl_2).into(imageView);
        }
    }

    private void checkAnswer(){
        if(TextUtils.isEmpty(answerText.getText())){
            Toast.makeText(getApplicationContext(),"Enter answer",Toast.LENGTH_SHORT).show();
        }
        else{

            if(answerText.getText().toString().equalsIgnoreCase(rightanswer)){
                score = score + 3;
                Toast.makeText(getApplicationContext(),"correct "+ String.valueOf(score),Toast.LENGTH_SHORT).show();

                Log.e("Image SCORE",String.valueOf(score));
            }
            else{
                Log.e("Image SCORE",String.valueOf(score));
                Toast.makeText(getApplicationContext(),"Wrong "+ String.valueOf(score),Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void nextQuestion() {

        if (TextUtils.isEmpty(answerText.getText())) {
            Toast.makeText(getApplicationContext(), "Enter answer", Toast.LENGTH_SHORT).show();
        } else {
            checkAnswer();
            answerText.setText("");
            index++;
            setImage();
            rightanswer = "pencil";
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        qsNo.setText(String.valueOf(index));
        setImage();
        rightanswer = "watch";
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
