package com.example.toolbox.listeners;

import com.example.toolbox.entities.Note;

//노트를 열어서 보고, 수정 가능하게 할 수 있도록 하기위한 인터페이스
public interface NotesListener {
    void onNoteClicked(Note note, int position);
}
