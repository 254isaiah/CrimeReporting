package com.example.crimeandmissingpersonreporting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MissingPersonAdapter extends RecyclerView.Adapter<MissingPersonAdapter.MissingPersonViewHolder> {
    private Context mContext;
    private List<Person> mPersonList;

    public MissingPersonAdapter(Context contect, List<Person> personList) {
        mContext = contect;
        mPersonList = personList;
    }

    @NonNull
    @Override
    public MissingPersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_model, parent, false);
        return new MissingPersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MissingPersonViewHolder holder, int position) {
        Person personCurrent = mPersonList.get(position);
        holder.nameTextView.setText("Full Names" +personCurrent.getName());
        //holder.dateTextView.setText(personCurrent.getDateToday());
        Picasso.get()
                .load(personCurrent.getImageURL())
                .fit()
                .centerCrop()
                .into(holder.personImageView);

        holder.ageTextView.setText(personCurrent.getAge());
        holder.genderTextView.setText(personCurrent.getGender());
        holder.descriptionTextView.setText(personCurrent.getDescription());
        holder.residenceTextView.setText(personCurrent.getResidence());
        holder.contactTextView.setText(personCurrent.getContact());


    }

    @Override
    public int getItemCount() {
        return mPersonList.size();
    }

    public class MissingPersonViewHolder extends RecyclerView.ViewHolder{
        public ImageView personImageView;
        public  TextView nameTextView,dateTextView,ageTextView,genderTextView,descriptionTextView,residenceTextView,contactTextView;

        public MissingPersonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            personImageView = itemView.findViewById(R.id.personImageView);
            //dateTextView = itemView.findViewById(R.id.dateTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            residenceTextView = itemView.findViewById(R.id.residenceTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);

        }
    }
}
