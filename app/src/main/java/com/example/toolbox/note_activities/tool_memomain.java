package com.example.toolbox.note_activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.toolbox.BackPressHandler;
import com.example.toolbox.R;
import com.example.toolbox.adapters.NotesAdapter;
import com.example.toolbox.database.NotesDatabase;
import com.example.toolbox.entities.Note;
import com.example.toolbox.listeners.NotesListener;

import java.util.ArrayList;
import java.util.List;

public class tool_memomain extends AppCompatActivity implements NotesListener {

    public static final int REQUEST_CODE_ADD_NOTE = 1;
    public static final int REQUEST_CODE_UPDATE_NOTE = 2;

    //모든 노트를 보여주는데 사용
    public static final int REQUEST_CODE_SHOW_NOTES = 3;

    private RecyclerView notesRecyclerView;
    private List<Note> noteList;
    private NotesAdapter notesAdapter;
    private int noteClickedPosition = -1;

    private BackPressHandler backPressHandler = new BackPressHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_memomain);

        //ADD NOTE
        ImageView imageAddnoteMain = findViewById(R.id.imageAddNoteMain);
        imageAddnoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        new Intent(getApplicationContext(), CreateNoteActivity.class),
                        REQUEST_CODE_ADD_NOTE
                );
            }
        });
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        );
        noteList = new ArrayList<>();
        notesAdapter = new NotesAdapter(noteList, this);
        notesRecyclerView.setAdapter(notesAdapter);

        //onCreate될 때는 모든 노트를 가져올 수 있도록 설정, 전부다 가져오는 상황이므로 NoteDeleted는 false
        getNotes(REQUEST_CODE_SHOW_NOTES, false);
    }

    //VIEW or UPDATE NOTE는 선택된 note db를 보낸다
    //그리고 View or Update 할 때 layout_various에 삭제하는 부분이 보이도록
    @Override
    public void onNoteClicked(Note note, int position) {
        noteClickedPosition = position;
        Intent intent = new Intent(getApplicationContext(), CreateNoteActivity.class);
        intent.putExtra("viewORupdate", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE);
    }


    /* startActivityForResult로 tool_memomain은 CreateNoteActivity를 호출하고 (requestCode 로 어느 액티비티를 호출 했는지 구별)
     *  CreateNoteActivity에서 setResult()로 돌려줄 결과를 저장하고 finish()로 CreateNoteActivity를 종료
     *  호출했던 tool_memomain는 호출한 액티비티가 저장해둔 결과를 onActivityResult()를 통해 전달받는다.*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_NOTE && resultCode == RESULT_OK) {
            getNotes(REQUEST_CODE_ADD_NOTE, false);
        }else if (requestCode == REQUEST_CODE_UPDATE_NOTE && resultCode == RESULT_OK) {
            if (data != null) {
                //삭제되면 boolean값을 true로 받는다.
                getNotes(REQUEST_CODE_UPDATE_NOTE, data.getBooleanExtra("isNoteDeleted", false));
            }
        }
    }


    //데이터베이스로부터 노트가져오고 , requestCode,isNoteDeleted 의 값을 보고 결정.
    private void getNotes(final int requestCode, final boolean isNoteDeleted) {
        class GetNotesTaks extends AsyncTask<Void, Void, List<Note>> {

            //doInBackground(Params... params): 스레드에 의해 처리될 내용을 담기 위한 함수
            @Override
            protected List<Note> doInBackground(Void... voids) {
                return NotesDatabase.getDatabase(getApplicationContext()).noteDao().getAllNotes();
            }

            //onPostExecute(Result result): AsyncTask의 모든 작업이 완료된 후 가장 마지막에 한 번 호출.
            // doInBackground() 함 수의 최종 값을 받기 위해 사용
            @Override
            protected void onPostExecute(List<Note> notes) {
                super.onPostExecute(notes);
                if (requestCode == REQUEST_CODE_SHOW_NOTES) {
                    noteList.addAll(notes);
                } else if (requestCode == REQUEST_CODE_ADD_NOTE) {
                    noteList.add(0, notes.get(0));
                    notesAdapter.notifyItemInserted(0);
                    notesRecyclerView.smoothScrollToPosition(0);
                } else if (requestCode == REQUEST_CODE_UPDATE_NOTE) {
                    //첫번째로 list에서 삭제하고 note가 삭제되었는지 안되었는지 확인 후
                    //삭제되면 Adpater에 알려 삭제하고, 삭제되지 않았다면 변경된 내용을 Adapter에 알린다.
                    noteList.remove(noteClickedPosition);
                    if(isNoteDeleted){
                        notesAdapter.notifyItemRemoved(noteClickedPosition);
                    }else{
                        noteList.remove(noteClickedPosition);
                        noteList.add(noteClickedPosition, notes.get(noteClickedPosition));
                    }
                }
            }
        }
        new GetNotesTaks().execute();
    }



    @Override
    public void onBackPressed() {
        backPressHandler.onBackPressed("뒤로가기 버튼 한번 더 누르면 종료", 3000);
    }
}