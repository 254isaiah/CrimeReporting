package com.example.crimeandmissingpersonreporting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView recycler_view;
    private ArrayList<ComplainModel> list;
    private ComplainAdapter adapter;
    private FirebaseAuth mAuth;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Report_Complain");
    private DatabaseReference mDatabaseCurrentUser;
    private Query mQueryCurrentUser;
    private DatabaseReference mDatabaseUser;
    //private String userID;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Reported Complaints");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("Users");
        //userID = user.getUid();

        String userID = mAuth.getCurrentUser().getUid();
        //Log.d(TAG, "onCreate: "+currentUserId);
        mDatabaseCurrentUser = FirebaseDatabase.getInstance().getReference().child("Report_Complain");
        mQueryCurrentUser = mDatabaseCurrentUser.orderByChild("userID").equalTo(userID);
        //Log.d(TAG, "onCreate: "+userID);
        Log.d(TAG, "onCreate: "+mQueryCurrentUser);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        Context context = HistoryActivity.this;
        if (!NetworkState.checkConnection(context)) {
            NetworkState.ifNoInternetConnection(context);
            return;
        }

        mQueryCurrentUser.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ComplainModel complainModel = dataSnapshot.getValue(ComplainModel.class);
                    list.add(complainModel);
                }
                adapter = new ComplainAdapter(HistoryActivity.this, list);
                recycler_view.setAdapter(adapter);

                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.search_action);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search Here!!");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                adapter.getFilter().filter(newText);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}