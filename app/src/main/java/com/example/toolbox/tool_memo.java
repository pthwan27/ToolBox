package com.example.toolbox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.Recycler.RecyclerAdapter;
import com.example.toolbox.Room.AppDataBase;
import com.example.toolbox.Room.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class tool_memo extends AppCompatActivity {
    private final int SAVE_MEMO_ACTIVITY = 1;
    private FloatingActionButton add;

    //리사이클러 뷰
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerAdapter adapter;
    private List<User> users;

    /*
    메인에서 initialized( ) 함수를 통해서 recyclerview 객체를 찾고,
    리사이클러뷰 내부에서 아이템 뷰들을 배치를 관리하기 위해 LinearLayoutManger를 생성하고
    생성된 linearLayoutManger를 리사이클러뷰에 추가
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_memo);

        initialized();

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        add.setOnClickListener(v -> {
            move();
        });
    }

    private void initialized() {
        add = findViewById(R.id.addMemo);

        recyclerView = findViewById(R.id.mainRecyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        adapter = new RecyclerAdapter();

        /*
        getInstance 함수에 userDao 인터페이스 안에 존재하는 모든 데이터를 불러오는
        getAll( ) 함수를 사용해서 size를 구했고, 그 size 크기만큼
        adapter에 additems 함수를 통해 내용 하나하나를 추가
         */
        users = AppDataBase.getInstance(this).userDao().getAll();
        int size = users.size();
        for (int i = 0; i < size; i++) {
            adapter.addItem(users.get(i));
        }
    }

    private void move() {
        Intent intent = new Intent(getApplicationContext(), tool_memoSaveActivity.class);
        startActivity(intent);
    }
    @Override
    protected void onStart() {
        users = AppDataBase.getInstance(this).userDao().getAll();
        adapter.addItems((ArrayList) users);
        super.onStart();
    }
}