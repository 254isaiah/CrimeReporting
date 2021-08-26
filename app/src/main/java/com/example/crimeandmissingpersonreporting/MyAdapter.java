package com.example.crimeandmissingpersonreporting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private ArrayList<PersonModal> mList;
    private Context context;

    public MyAdapter(Context context , ArrayList<PersonModal> mList){
        this.context = context;
        this.mList = mList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item , parent ,false);
        return new MyViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);

        holder.nameTextView.setText("Full Name: "+mList.get(position).getNames());
        holder.dateTextView.setText("Date: "+mList.get(position).getDate());
        holder.ageTextView.setText("Age: "+mList.get(position).getAge());
        holder.genderTextView.setText("Gender: "+mList.get(position).getGender());
        holder.descriptionTextView.setText("Description: "+mList.get(position).getDescription());
        holder.residenceTextView.setText("Residence: "+mList.get(position).getResidence());
        holder.contactTextView.setText("Contact: "+mList.get(position).getContact());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        //added
        TextView nameTextView,dateTextView,ageTextView,genderTextView,descriptionTextView,residenceTextView,contactTextView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            //added
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            ageTextView = itemView.findViewById(R.id.ageTextView);
            genderTextView = itemView.findViewById(R.id.genderTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            residenceTextView = itemView.findViewById(R.id.residenceTextView);
            contactTextView = itemView.findViewById(R.id.contactTextView);
        }
    }
    private String getDateToday(){
        DateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd");
        Date date=new Date();
        String today= dateFormat.format(date);
        return today;
    }
}
