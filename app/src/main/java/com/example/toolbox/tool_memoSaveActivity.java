package com.example.toolbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toolbox.Room.AppDataBase;
import com.example.toolbox.Room.User;


public class tool_memoSaveActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 200;
    private EditText description;
    private TextView result;
    private AppDataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tool_memo_save);

        initialized();
    }

    private void initialized() {
        description = findViewById(R.id.description);
        result = findViewById(R.id.result);
        db = AppDataBase.getInstance(this);
    }
    //메모저장하는 버튼
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_memomenu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                make_title();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //액티비티 상단에 존재하는 디스크모양의 Save 버튼을 클릭했을 때
    // make_title( ) 이라는 함수가 호출되고,
    // 작성된 본문 내용의 제목을 작성할 수 있게 dialog 창을 띄우고
    // 그 안에 editText를 이용해서 제목을 작성하게 합니다.
    private void make_title() {
        EditText editText = new EditText(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("제목을 입력하세요");
        builder.setView(editText);

        builder.setPositiveButton("저장", (dialog, which) -> {
            String s = editText.getText().toString();
            //db에 저장하기
            User memo = new User(s, description.getText().toString());
            Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            finish();
        });

        builder.setNegativeButton("취소", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.show();
    }
}