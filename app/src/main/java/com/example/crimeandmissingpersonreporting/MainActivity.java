package com.example.crimeandmissingpersonreporting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView cover;
    private TextView textView;
    private Button btnMissingP,btnRegister,btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cover = findViewById(R.id.cover);
        btnMissingP = findViewById(R.id.btnMissingP);
        textView = findViewById(R.id.textView);
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        btnMissingP.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        if (v == btnMissingP)
        {
            startActivity(new Intent(this, MissingPersonActivity.class));
        }
        if (v==btnRegister)
        {
            startActivity(new Intent(this, RegisterActivity.class));
        }
        if (v==btnLogin)
        {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}