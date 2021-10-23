package com.example.crimeandmissingpersonreporting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> implements Filterable {

    private ArrayList<PersonModal> mList;
    ArrayList<PersonModal> mListFull;
    private Context context;


    public MyAdapter(Context context , ArrayList<PersonModal> mList){
        this.context = context;
        this.mListFull = mList;
        this.mList = new ArrayList<>(mListFull);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clickeddddddddddddd", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public Filter getFilter() {
        return listFilter;
    }

    private final Filter listFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<PersonModal> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){

                filteredList.addAll(mListFull);

            }else{

                String filterPattern = constraint.toString().toLowerCase().trim();

                for (PersonModal personModal : mListFull){

                    if (personModal.getNames().toLowerCase().contains(filterPattern) || personModal.getResidence().toLowerCase().contains(filterPattern))
                        filteredList.add(personModal);
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            filterResults.count = filteredList.size();
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mList.clear();
            mList.addAll((ArrayList)results.values);
            notifyDataSetChanged();

        }
    };


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
