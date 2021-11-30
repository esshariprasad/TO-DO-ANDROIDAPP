package com.example.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Adapter is responsible for taking the data at a particular position and putting into ViewHolder
public class MainActionAdapter extends RecyclerView.Adapter<MainActionAdapter.ViewHolder> {


    public interface OnClickListener{
        void onItemClicked(int postion);
    }
    List<String> items;
    OnClickListener clickListener;
    public MainActionAdapter(List<String> items, OnClickListener clickListener)
    {
        this.items = items;
        this.clickListener = clickListener;
    }
    @NonNull
    @Override


    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Layout inflator is used to increase the view
        View TodoListView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent , false);
        return new ViewHolder(TodoListView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String list = items.get(position);
        holder.bind(list);

    }

    //Tells the recycler view how many task/items in the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //ViewHolder is a way to provide access to views to each row in the list
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemList;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemList = itemView.findViewById(android.R.id.text1);
        }

        //Responsibilty of bind method is to update the view inside the View holder with the data of String item

        public void bind(String list) {
            itemList.setText( list);
            //Interface to communicate with main activity
            itemList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });

        }
    }

}
