package com.example.crimeandmissingpersonreporting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MissingPersonActivity extends AppCompatActivity {
    private TextView headerTxt;
    private Button viewPerson,addPerson;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing_person);

        headerTxt = findViewById(R.id.headerTxt);
        logo = findViewById(R.id.logo);
        viewPerson = findViewById(R.id.viewPerson);
        addPerson = findViewById(R.id.addPerson);

        viewPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPersonsListActivity();
            }
        });
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MissingPersonActivity.this, UploadPersonActivity.class));
            }
        });

    }

    private void openPersonsListActivity(){
        Intent intent = new Intent(this, PersonsListActivity.class);
        startActivity(intent);
    }
}