package com.example.bezpiecznedziecko.child.schedules;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bezpiecznedziecko.R;

import java.util.ArrayList;
import java.util.List;

public class childSchedulesListView extends RecyclerView.Adapter<childSchedulesListView.ViewHolder> {

    private ArrayList<Schedules.Schedule> schedulesList;
    private OnNoteListener onNoteListener;

    public childSchedulesListView(OnNoteListener inputOnNoteListener) {
        schedulesList = new ArrayList<>();
        onNoteListener = inputOnNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_schedules_list_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedules.Schedule schedules = schedulesList.get(position);
        String times = "OD: " + schedules.start + " DO: " + schedules.stop;
        holder.txt_description.setText(schedules.description);
        holder.txt_times.setText(times);
    }

    @Override
    public int getItemCount() {
        return schedulesList.size();
    }

    public void setData(List< Schedules.Schedule> schedules) {
        this.schedulesList.addAll(schedules);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnNoteListener onNoteListener;

        public TextView txt_description;
        public TextView txt_times;
        public CardView cardView;

        public ViewHolder(View view, OnNoteListener onNoteListener) {
            super(view);

            txt_description = view.findViewById(R.id.view_txt1);
            txt_times = view.findViewById(R.id.view_txt2);
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
