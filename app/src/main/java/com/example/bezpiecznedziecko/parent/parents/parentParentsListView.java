package com.example.bezpiecznedziecko.parent.parents;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bezpiecznedziecko.R;
import com.example.bezpiecznedziecko.parent.children.Children;

import java.util.ArrayList;
import java.util.List;

public class parentParentsListView extends RecyclerView.Adapter<parentParentsListView.ViewHolder> {

    private ArrayList<Parents.Parent> parentsList;
    private OnNoteListener onNoteListener;

    public parentParentsListView(OnNoteListener inputOnNoteListener) {
        parentsList = new ArrayList<>();
        onNoteListener = inputOnNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_parents_list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Parents.Parent parents = parentsList.get(position);

        holder.txtCoin.setText(parents.login);
        holder.txtMarket.setText(parents.first_name);
        holder.txtPrice.setText(parents.last_name);
    }

    @Override
    public int getItemCount() {
        return parentsList.size();
    }

    public void setData(List<Parents.Parent> parents) {
        this.parentsList.addAll(parents);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnNoteListener onNoteListener;

        public TextView txtCoin;
        public TextView txtMarket;
        public TextView txtPrice;
        public CardView cardView;

        public ViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);

            txtCoin = view.findViewById(R.id.txtCoin);
            txtMarket = view.findViewById(R.id.txtMarket);
            txtPrice = view.findViewById(R.id.txtPrice);
            cardView = view.findViewById(R.id.cardView);

            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener {
        void onNoteClick(int position);
    }

}
