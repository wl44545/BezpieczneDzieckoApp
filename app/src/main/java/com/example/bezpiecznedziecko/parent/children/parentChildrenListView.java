package com.example.bezpiecznedziecko.parent.children;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.bezpiecznedziecko.R;
import java.util.ArrayList;
import java.util.List;

public class parentChildrenListView extends RecyclerView.Adapter<parentChildrenListView.ViewHolder> {

    private ArrayList<Children.Child> childrenList;
    private OnNoteListener onNoteListener;

    public parentChildrenListView(OnNoteListener inputOnNoteListener) {
        childrenList = new ArrayList<>();
        onNoteListener = inputOnNoteListener;
    }

    @Override
    public parentChildrenListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_children_list_view, parent, false);
        parentChildrenListView.ViewHolder viewHolder = new parentChildrenListView.ViewHolder(view, onNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Children.Child children = childrenList.get(position);

        holder.txtCoin.setText(children.login);
        holder.txtMarket.setText(children.first_name);
        holder.txtPrice.setText(children.last_name);
    }

    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    public void setData(List<Children.Child> children) {
        this.childrenList.addAll(children);
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
