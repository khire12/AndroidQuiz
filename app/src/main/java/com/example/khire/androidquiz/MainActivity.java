package com.example.khire.androidquiz;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.design.widget.Snackbar;
import com.example.khire.androidquiz.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    MaterialEditText edtNewUser,edtNewPassword,edtNewEmail;     //for signup
    MaterialEditText edtEmail,edtPassword;                       //for signin
    Button btnSignUp,btnSignIn;

    FirebaseDatabase database;
    DatabaseReference users;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("Users");
        edtEmail = (MaterialEditText)findViewById(R.id.EdtEmail);
        edtPassword = (MaterialEditText)findViewById(R.id.EdtPassword);
        btnSignIn = (Button)findViewById(R.id.btn_sign_in);
        btnSignUp = (Button)findViewById(R.id.btn_sign_up);
        mAuth = FirebaseAuth.getInstance();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignIn();
              //  signIn(edtUser.getText().toString(),edtPassword.getText().toString());
            }
        });

    }




    private void showSignUpDialog() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Sign up");
        alertDialog.setMessage("Please fill up the form");

        LayoutInflater inflater = this.getLayoutInflater();
        View sign_up_layout = inflater.inflate(R.layout.sign_up_layout,null);
        edtNewUser = (MaterialEditText)sign_up_layout.findViewById(R.id.EdtNewUserName);
        edtNewEmail = (MaterialEditText)sign_up_layout.findViewById(R.id.EdtNewEmail);
        edtNewPassword = (MaterialEditText)sign_up_layout.findViewById(R.id.EdtNewPassword);
        alertDialog.setView(sign_up_layout);
        alertDialog.setIcon(R.drawable.ic_account_circle_black_24dp);


        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final User user = new User(edtNewUser.getText().toString(), edtNewPassword.getText().toString(),edtNewEmail.getText().toString());

                users.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUsername()).exists()) {
                          //  Toast.makeText(MainActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                           Snackbar snackbar = Snackbar.make(getCurrentFocus(),"User already exists!",Snackbar.LENGTH_LONG);
                           snackbar.show();
                        }
                        else{
                            users.child(user.getUsername()).setValue(user);
                            //Toast.makeText(MainActivity.this,"User registeration successful!",Toast.LENGTH_SHORT).show();

                            createUser();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    private void createUser(){
        mAuth.createUserWithEmailAndPassword(edtNewEmail.getText().toString(),edtNewPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                           // Toast.makeText(MainActivity.this,"User registeration successful!",Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(getCurrentFocus(),"User registeration successful!",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                        else{
                            //Toast.makeText(MainActivity.this,"Registeration failed",Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar.make(getCurrentFocus(),"Registeration failed!",Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });
    }

    private void userSignIn(){

        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                //Toast.makeText(MainActivity.this,"User login successful!",Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(getCurrentFocus(), "User login successful", Snackbar.LENGTH_LONG);
                                snackbar.show();
                                startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
                                overridePendingTransition(R.anim.fui_slide_in_right,R.anim.fui_slide_out_left);
                            } else {
                                //Toast.makeText(MainActivity.this,"login failed",Toast.LENGTH_SHORT).show();
                                Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Login failed", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                    });
        }
        else {
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), "Enter email/password", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

}
