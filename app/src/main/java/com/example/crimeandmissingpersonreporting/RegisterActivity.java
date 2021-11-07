package com.example.crimeandmissingpersonreporting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView textViewLogin,logo_name,bannerDescription;
    FirebaseAuth mAuth;
    private EditText fullname,editTextPhone,email,password;
    private ProgressBar progressBar;
    private Button buttonRegister;
    //AwesomeValidation mAwesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        textViewLogin = findViewById(R.id.textViewLogin);
        textViewLogin.setOnClickListener(this);

        logo_name = findViewById(R.id.logo_name);
        logo_name.setOnClickListener(this);

        bannerDescription = findViewById(R.id.bannerDescription);
        fullname = findViewById(R.id.fullname);
        editTextPhone = findViewById(R.id.editTextPhone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);

        buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(this);
/*
        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.fullname, "[-a-zA-Z.'\\s]+", R.string.name_required);
        mAwesomeValidation.addValidation(this, R.id.email, android.util.Patterns.EMAIL_ADDRESS, R.string.email_required);
        //String regexPassword = "(.*?)";//allow anything
        mAwesomeValidation.addValidation(this, R.id.editTextPhone, "^[0-9]\\d{9}$", R.string.phone_required);
        mAwesomeValidation.addValidation(this, R.id.password, "(.*?)" , R.string.pass_required);*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logo_name:
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                break;
            case R.id.textViewLogin:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.buttonRegister:
                    createUser();
                    break;


        }
    }

    private void createUser(){
        Context context = RegisterActivity.this;
        if (!NetworkState.checkConnection(context)) {
            NetworkState.ifNoInternetConnection(context);
            return;
        }
        String emailAddress = email.getText().toString().trim();
        String names = fullname.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String passwd = password.getText().toString().trim();

        if (names.isEmpty()){
            fullname.setError("Full name is required");
            fullname.requestFocus();
            return;
        }
        if (phone.isEmpty()){
            editTextPhone.setError("phone number is required");
            editTextPhone.requestFocus();
            return;
        }
        if (emailAddress.isEmpty()){
            email.setError("email is required");
            email.requestFocus();
            return;
        }
        if (passwd.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }
        if (passwd.length() < 6) {
            password.setError("Min password length should be six characters");
            password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(emailAddress,passwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            //create user object to store user details in the firebase
                            User user = new User(names, phone, emailAddress);

                            //send user object to the realtime database
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        progressBar.setVisibility(View.GONE);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                        builder.setTitle("Registration Successful")
                                                .setMessage("You can now proceed to login, and enjoy our awesome content!");
                                        builder.setCancelable(false);

                                        builder.setPositiveButton(
                                                "Ok",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                                        finish();

                                                    }
                                                });

                                        AlertDialog alert = builder.create();
                                        alert.show();
                                        //Toast.makeText(RegisterActivity.this, "User has been registered successfully",Toast.LENGTH_LONG).show();
                                        //progressBar.setVisibility(View.GONE);
                                        //redirect to login layout
                                    }else {
                                        Toast.makeText(RegisterActivity.this, "Failed to register try again!!",Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(RegisterActivity.this, "Email is already taken!!",Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });
    }
}