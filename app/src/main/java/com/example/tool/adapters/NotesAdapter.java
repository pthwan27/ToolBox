package com.example.tool.adapters;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toolbox.R;
import com.example.tool.entities.Note;
import com.example.tool.listeners.NotesListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//note, room db의 entities 값들을 가져와 item_container_note를 RecyclerView로 보여준다.
public class NotesAdapter extends RecyclerView.Adapter<com.example.tool.adapters.NotesAdapter.NoteViewHolder>{
    private List<Note> notes;

    //노트 view or update 하기위해
    private NotesListener notesListener;

    private Timer timer;
    private List<Note> noteSource;


    public NotesAdapter(List<Note> notes, NotesListener notesListener)
    {
        this.notes = notes;
        this.notesListener = notesListener;
        noteSource = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_container_note, parent, false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.setNote(notes.get(position));
        holder.layoutNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesListener.onNoteClicked(notes.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView textTitle, textSubtitle, textDateTime;
        LinearLayout layoutNote; // 노트의 색상 적용하기위해
        RoundedImageView imageNote; // 이미지를 보여주기 위해
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            textSubtitle = itemView.findViewById(R.id.textSubtitle);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            layoutNote = itemView.findViewById(R.id.layoutNote);
            imageNote = itemView.findViewById(R.id.imageNote);
        }

        void setNote(Note note){
            textTitle.setText(note.getTitle());
            if(note.getSubtitle().trim().isEmpty()){
                textSubtitle.setVisibility(View.GONE);
            }else{
                textSubtitle.setText(note.getSubtitle());
            }
            textDateTime.setText(note.getDateTime());

            GradientDrawable gradientDrawable = (GradientDrawable) layoutNote.getBackground();
            if(note.getColor() != null) {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            }else{
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }

            if(note.getImagePath() != null){
                imageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                imageNote.setVisibility(View.VISIBLE);
            }else{
                imageNote.setVisibility(View.GONE);
            }
        }
    }

    public void searchNote(final String searchKeyword){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //notes -> 최종적으로 보여줄 노트.
                //noteSource == notes
                if(searchKeyword.trim().isEmpty()){
                    notes = noteSource;
                }else {
                    //note에 notesource를 넣어가면서 찾아봄.
                    //똑같은 문자열 발견시 temp에 저장
                    ArrayList<Note> temp = new ArrayList<>();
                    for(Note note : noteSource){
                        if(note.getTitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                        note.getSubtitle().toLowerCase().contains(searchKeyword.toLowerCase()) ||
                        note.getNoteText().toLowerCase().contains(searchKeyword.toLowerCase())){
                            temp.add(note);
                        }
                    }
                    notes = temp;
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //RecyclerView adapter 갱신시켜준다.
                        notifyDataSetChanged();
                    }
                });
            }
        }, 500);
    }
    public void cancelTimer(){
        if (timer != null) {
            timer.cancel();
        }
    }
}