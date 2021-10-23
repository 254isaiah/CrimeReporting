package com.example.crimeandmissingpersonreporting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.LENGTH_LONG;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class ReportCrime extends AppCompatActivity {

    private ProgressBar progress_bar;
    private EditText nameEditText,idEditText,ageEditText,genderEditText,complainEditText,residenceEditText,contactEditText;
    private Button button_choose_image,uploadBtn;
    private ImageView imageView;
    Spinner genderSpinner,CrimeSpinner;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Report_Complain");
    private StorageReference reference = FirebaseStorage.getInstance().getReference("Report_Complain");
    private Uri imageUri;
    ProgressDialog mProgressDialog;
    AwesomeValidation mAwesomeValidation;
    private FirebaseAuth mAuth;

    private DatabaseReference mDatabaseUser;
    private String userID;
    private FirebaseUser user;

    //long maxid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_crime);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();
        //Log.d(TAG, "onCreate: "+userID);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Report Complaint");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog=new ProgressDialog(this);

        nameEditText = findViewById(R.id.nameEditText);

        ageEditText = findViewById(R.id.ageEditText);
        idEditText = findViewById(R.id.idEditText);
        //genderEditText = findViewById(R.id.genderEditText);
        residenceEditText = findViewById(R.id.residenceEditText);
        contactEditText = findViewById(R.id.contactEditText);
        complainEditText = findViewById(R.id.complainEditText);
        button_choose_image = findViewById(R.id.button_choose_image);
        imageView = findViewById(R.id.imageView);
        uploadBtn = findViewById(R.id.reportBtn);

        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);//fetch the spinner from layout file
        CrimeSpinner = (Spinner) findViewById(R.id.CrimeSpinner);

        ArrayAdapter<String> genderadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.gender));//setting the gender_array to spinner
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderadapter);

        ArrayAdapter<String> crimeadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.crime));//setting the crime_array to spinner
        crimeadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CrimeSpinner.setAdapter(crimeadapter);

        mAwesomeValidation = new AwesomeValidation(BASIC);
        mAwesomeValidation.addValidation(this, R.id.nameEditText, "[-a-zA-Z.'\\s]+", R.string.person_name);
        mAwesomeValidation.addValidation(this, R.id.idEditText, "^[0-9]\\d{7}$", R.string.id);
        mAwesomeValidation.addValidation(this, R.id.ageEditText, "^100|[1-9]?\\d$", R.string.person_age);
        mAwesomeValidation.addValidation(this, R.id.residenceEditText, "[-a-zA-Z.'\\s]+", R.string.residence);
        mAwesomeValidation.addValidation(this, R.id.contactEditText, "^[0-9]\\d{9}$", R.string.contact);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });
        button_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = ReportCrime.this;
                if (!NetworkState.checkConnection(context)) {
                    NetworkState.ifNoInternetConnection(context);
                    return;
                }
                String gender = genderSpinner.getSelectedItem().toString();
                String crime = CrimeSpinner.getSelectedItem().toString();
                if ("Select Gender".equals(gender)){
                    Toast.makeText(ReportCrime.this, "please select your gender", LENGTH_LONG).show();
                }
                else if ("Select Crime".equals(crime)){
                    Toast.makeText(ReportCrime.this, "Type of crime required", LENGTH_LONG).show();
                }

                else if (mAwesomeValidation.validate()) {
                    if (imageUri != null) {
                        uploadToFirebase(imageUri);

                    }else {
                        Toast.makeText(ReportCrime.this, "Please Upload your ID or evidence to the case", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }

    private void uploadToFirebase(Uri uri) {

        StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        /*root.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    //maxid = (snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    String name = nameEditText.getText().toString().trim();
                    String idNo = idEditText.getText().toString().trim();
                    String age = ageEditText.getText().toString().trim();
                    String gender = genderSpinner.getSelectedItem().toString().trim();
                    String crime = CrimeSpinner.getSelectedItem().toString().trim();
                    String complain = complainEditText.getText().toString().trim();
                    String residence = residenceEditText.getText().toString().trim();
                    String contact = contactEditText.getText().toString().trim();


                    DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
                    Date date = new Date();
                    String stringdate= dateFormat.format(date);





                    @Override
                    public void onSuccess(Uri uri) {
                        ComplainModel complainModel = new ComplainModel(uri.toString(), name, age, gender, crime, complain, residence,contact,idNo,stringdate, userID);
                        //String userID = mDatabaseUser.push().getKey();
                       // mDatabaseUser.child(userID).setValue(complainModel);
                        String modalId = root.push().getKey();
                        root.child(modalId).setValue(complainModel);


                        //root.child(String.valueOf(maxid+1)).setValue(complainModel);
                        mProgressDialog.dismiss();

                        Toast.makeText(ReportCrime.this, "Sent successfully", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.images_placehold);
                        startActivity(new Intent(ReportCrime.this, HistoryActivity.class));
                        finish();

                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                mProgressDialog.setTitle("Sending.....");
                mProgressDialog.show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(ReportCrime.this, "Failed to send", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
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