package com.example.room_data;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private OnItemClickListner listner;
    private List<Note> notes = new ArrayList<>();

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_layout, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.textView_title.setText(currentNote.getTitle());
        holder.textView_description.setText(currentNote.getDescription());
        holder.textView_priority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView textView_title;
        private TextView textView_description;
        private TextView textView_priority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.title_textView);
            textView_description = itemView.findViewById(R.id.description_textView);
            textView_priority = itemView.findViewById(R.id.priority_textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listner != null && position != RecyclerView.NO_POSITION) {
                        listner.onitemClick(notes.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListner {
        void onitemClick(Note note);
    }

    public void setOnItemClickListner(OnItemClickListner listner) {
        this.listner = listner;
    }
}
