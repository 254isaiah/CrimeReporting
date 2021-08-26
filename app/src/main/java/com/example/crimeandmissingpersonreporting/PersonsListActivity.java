package com.example.crimeandmissingpersonreporting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PersonsListActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private MissingPersonAdapter mPersonAdapter;

    private DatabaseReference mDatabaseReference;
    private List<Person> mPeople;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persons_list);

        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

        mPeople = new ArrayList<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("missingPersonUploads");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Person person = postSnapshot.getValue(Person.class);
                    mPeople.add(person);
                }
                mPersonAdapter = new MissingPersonAdapter(PersonsListActivity.this, mPeople);
                recycler_view.setAdapter(mPersonAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PersonsListActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}