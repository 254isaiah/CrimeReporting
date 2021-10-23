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
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.EventListener;

import static android.widget.Toast.LENGTH_LONG;
import static com.basgeekball.awesomevalidation.ValidationStyle.BASIC;

public class MissingActivity extends AppCompatActivity {
    AwesomeValidation personValidation;
    private Button uploadBtn, showAllBtn;
    private ImageView imageView;
    private EditText nameEditText,ageEditText,genderEditText,descriptionEditText,residenceEditText,contactEditText,birthEditText;
    Spinner genderSpinner;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Report_Missing");
    private StorageReference reference = FirebaseStorage.getInstance().getReference("MissingPeople");
    private Uri imageUri;
    ProgressDialog mProgressDialog;
    //long maxid = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missing);


        Toolbar toolbar = findViewById(R.id.toolbar1);
        toolbar.setTitle("Missing Person");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mProgressDialog=new ProgressDialog(this);

        uploadBtn = findViewById(R.id.uploadBtn);
        showAllBtn = findViewById(R.id.showAll);
        imageView = findViewById(R.id.imageView);
        nameEditText = findViewById(R.id.nameEditText);
        birthEditText = findViewById(R.id.birthEditText);
        ageEditText = findViewById(R.id.ageEditText);
        genderEditText = findViewById(R.id.genderEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        residenceEditText = findViewById(R.id.residenceEditText);
        contactEditText = findViewById(R.id.contactEditText);

        genderSpinner = (Spinner) findViewById(R.id.genderSpinner);//fetch the spinner from layout file
        ArrayAdapter<String> genderadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources()
                .getStringArray(R.array.gender));//setting the gender_array to spinner
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderadapter);

        personValidation = new AwesomeValidation(BASIC);
        personValidation.addValidation(this, R.id.nameEditText, "[-a-zA-Z.'\\s]+", R.string.person_name);
        personValidation.addValidation(this, R.id.birthEditText, "^[0-9]\\d{11}$", R.string.birthCert);
        personValidation.addValidation(this, R.id.ageEditText, "^100|[1-9]?\\d$", R.string.person_age);
        personValidation.addValidation(this, R.id.residenceEditText, "[-a-zA-Z.'\\s]+", R.string.residence);
        personValidation.addValidation(this, R.id.contactEditText, "^[0-9]\\d{9}$", R.string.contact);


        showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MissingActivity.this, showPeopleActivity.class));
                finish();

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
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
                Context context = MissingActivity.this;
                if (!NetworkState.checkConnection(context)) {
                    NetworkState.ifNoInternetConnection(context);
                    return;
                }
                String gender = genderSpinner.getSelectedItem().toString();
                if ("Select Gender".equals(gender)){
                    Toast.makeText(MissingActivity.this, "please select your gender", LENGTH_LONG).show();
                }

                else if (personValidation.validate()) {
                    if (imageUri != null) {
                        uploadToFirebase(imageUri);


                    } else {
                        Toast.makeText(MissingActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
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
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    String name = nameEditText.getText().toString().trim();
                    String age = ageEditText.getText().toString().trim();
                    String gender = genderSpinner.getSelectedItem().toString().trim();
                    String birthCer = birthEditText.getText().toString().trim();
                    String description = descriptionEditText.getText().toString().trim();
                    String residence = residenceEditText.getText().toString().trim();
                    String contact = contactEditText.getText().toString().trim();

                    DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
                    Date date = new Date();
                    String stringdate= dateFormat.format(date);


                    @Override
                    public void onSuccess(Uri uri)
                    {
                        PersonModal personModal = new PersonModal(uri.toString(), name, age, gender, description, residence,contact,birthCer,stringdate);
                        String modalId = root.push().getKey();
                        root.child(modalId).setValue(personModal);
                        //root.child(String.valueOf(maxid+1)).setValue(personModal);
                        mProgressDialog.dismiss();

                        Toast.makeText(MissingActivity.this, "Uploaded successfully", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.images_placehold);
                        startActivity(new Intent(MissingActivity.this, showPeopleActivity.class));
                        finish();
                    }
                    });

            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                mProgressDialog.setTitle("Uploading.....");
                                mProgressDialog.show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgressDialog.dismiss();
                                Toast.makeText(MissingActivity.this, "Upload failed!!!!", Toast.LENGTH_SHORT).show();
                                finish();
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