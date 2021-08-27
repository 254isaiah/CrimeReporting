package com.example.crimeandmissingpersonreporting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.ComplainViewHolder> implements Filterable {

    private ArrayList<ComplainModel> mList;
    ArrayList<ComplainModel> mListFull;
    private Context context;

    public ComplainAdapter(Context context , ArrayList<ComplainModel> mList){
        this.context = context;
        this.mListFull = mList;
        this.mList = new ArrayList<>(mListFull);
    }

    @NonNull
    @Override
    public ComplainAdapter.ComplainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_reports , parent ,false);
        return new ComplainViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ComplainAdapter.ComplainViewHolder holder, int position) {
        Glide.with(context).load(mList.get(position).getImageUrl()).into(holder.imageView);

        holder.nameTextView.setText("Full Name: "+mList.get(position).getNames());
        holder.dateTextView.setText("Date: "+mList.get(position).getDate());
        holder.complainTextView.setText("Complain: "+mList.get(position).getComplain());
        holder.residenceTextView.setText("Residence: "+mList.get(position).getResidence());

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

            ArrayList<ComplainModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){

                filteredList.addAll(mListFull);

            }else{

                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ComplainModel complainModel : mListFull){

                    if (complainModel.getNames().toLowerCase().contains(filterPattern) || complainModel.getResidence().toLowerCase().contains(filterPattern)
                        || complainModel.getComplain().toLowerCase().contains(filterPattern))
                        filteredList.add(complainModel);
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

    public static class ComplainViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        //added
        TextView nameTextView,dateTextView,complainTextView,residenceTextView;
        public ComplainViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.m_image);
            //added
            nameTextView = itemView.findViewById(R.id.nameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            complainTextView = itemView.findViewById(R.id.complainTextView);
            residenceTextView = itemView.findViewById(R.id.residenceTextView);

        }
    }
}
