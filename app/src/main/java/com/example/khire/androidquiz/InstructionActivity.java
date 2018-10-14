package com.example.khire.androidquiz;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.Button;


import com.rengwuxian.materialedittext.MaterialMultiAutoCompleteTextView;

import java.util.Locale;

public class InstructionActivity extends AppCompatActivity {

    TextView m1;

    Toolbar toolbar;
    TextToSpeech t1;
    Button b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

    m1= findViewById(R.id.instructionText);

b1= findViewById(R.id.button);
        t1= new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR)
                {

                    if(getResources().getConfiguration().locale.toLanguageTag().equals("en-US"))
                    {
                        t1.setLanguage(new Locale("en","IN"));
                    }

                    if(getResources().getConfiguration().locale.toLanguageTag().equals("hi")||getResources().getConfiguration().locale.toLanguageTag().equals("mr"))
                    {
                        t1.setLanguage(Locale.forLanguageTag("hin"));
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



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                t1.speak(m1.getText(),TextToSpeech.QUEUE_FLUSH,null,null);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        t1.stop();
        startActivity(new Intent(InstructionActivity.this,DashboardActivity.class));
        overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        t1.stop();
    }
}
