package com.example.toolbox.note_activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toolbox.R;
import com.example.toolbox.database.NotesDatabase;
import com.example.toolbox.entities.Note;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText inputNoteTitle, inputNoteSubtitle, inputNoteText;
    private TextView textDateTime;
    private View viewSubtitleIndicator; //createNote, Subtitle옆 네모난 view
    private ImageView ImageNote;

    private String selectedNoteColor; //noteColor설정 + DB에 저장하기 위한 값
    private String selectedImagePath; //노트에 추가한 이미지에 대한 경로, DB에 이미지 경로 저장

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private AlertDialog dialogDeleteNote;
    private Note alreadyAvailableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        ImageView imageBack = findViewById(R.id.imageBack);
        //뒤로가는 버튼
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //ADD NOTE
        inputNoteTitle = findViewById(R.id.inputNoteTitile);
        inputNoteSubtitle = findViewById(R.id.inputNoteSubtitle);
        inputNoteText = findViewById(R.id.inputNote);
        textDateTime = findViewById(R.id.textDateTime);

        //createNote, Subtitle옆 네모난 view
        viewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);

        //image 저장하는 이미지지뷰
        ImageNote = findViewById(R.id.imageNote);

        textDateTime.setText(new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm a", Locale.getDefault())
                .format(new Date()));

        ImageView imageSave = findViewById(R.id.imageSave);
        imageSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        selectedNoteColor = "#333333";
        selectedImagePath = "";

        //getSerializableExtra이전에 putExtra()로 추가된 항목의 값, 나의 생각으론 클릭한 note
        //viewOrupdate update노트를 골랐으면 true가 옴.
        if (getIntent().getBooleanExtra("viewORupdate", false)) {
            alreadyAvailableNote = (Note) getIntent().getSerializableExtra("note");
            setviewORupdate();
        }

        findViewById(R.id.imageRemove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageNote.setImageBitmap(null);
                ImageNote.setVisibility(View.GONE);
                findViewById(R.id.imageRemove).setVisibility(View.GONE);
                selectedImagePath = "";
            }
        });

        initVariousColor();
        setViewSubtitleIndicatorColor();
    }

    private void saveNote() {
        if (inputNoteTitle.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_LONG).show();
            return;
        } else if (inputNoteSubtitle.getText().toString().trim().isEmpty() &&
                inputNoteText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note title can't be empty!", Toast.LENGTH_LONG).show();
            return;
        }
        //note에 작성한 column에 대한 값 입력 + room db에 저장
        final Note note = new Note();
        note.setTitle(inputNoteTitle.getText().toString());
        note.setSubtitle(inputNoteSubtitle.getText().toString());
        note.setNoteText(inputNoteText.getText().toString());
        note.setDateTime(textDateTime.getText().toString());
        note.setColor(selectedNoteColor);
        note.setImagePath(selectedImagePath);

        //노트가 update되면(notedao) 새로 추가되는게 아니라 alreadyAvailableNote(수정한 note)의 id(primary key)로 entities가 replace
        if (alreadyAvailableNote != null) {
            note.setId(alreadyAvailableNote.getId());
        }

        // Room은 메인 스레드에서 데이터베이스 작업을 허용하지 않기 때문에
        // 직접 작업을 해야한다
        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                NotesDatabase.getDatabase(getApplicationContext()).noteDao().insertNote(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        new SaveNoteTask().execute();
    }

    //view or Update Note
    private void setviewORupdate() {
        inputNoteTitle.setText(alreadyAvailableNote.getTitle());
        inputNoteSubtitle.setText(alreadyAvailableNote.getSubtitle());
        inputNoteText.setText(alreadyAvailableNote.getNoteText());
        textDateTime.setText(alreadyAvailableNote.getDateTime());
        if (alreadyAvailableNote.getImagePath() != null && !alreadyAvailableNote.getImagePath().trim().isEmpty()) {
            ImageNote.setImageBitmap(BitmapFactory.decodeFile(alreadyAvailableNote.getImagePath()));
            ImageNote.setVisibility(View.VISIBLE);
            findViewById(R.id.imageRemove).setVisibility(View.VISIBLE);
            selectedImagePath = alreadyAvailableNote.getImagePath();
        }
    }

    //Delete Dialog
    private void showDeleteDialog(){
        if(dialogDeleteNote == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNoteActivity.this);
            View view = LayoutInflater.from(this).inflate(
                    R.layout.layout_deletenote, (ViewGroup)findViewById(R.id.layoutDeleteNoteContainer)
            );
            builder.setView(view);
            dialogDeleteNote = builder.create();
            if(dialogDeleteNote.getWindow() != null){
                dialogDeleteNote.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }view.findViewById(R.id.textDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    @SuppressLint("StaticFieldLeak")
                    class DeleteNoteTasek extends AsyncTask<Void, Void, Void>{
                        @Override
                        protected Void doInBackground(Void... voids) {
                            NotesDatabase.getDatabase(getApplicationContext()).noteDao().deleteNote(alreadyAvailableNote);
                            return null;
                        }
                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Intent intent = new Intent();
                            intent.putExtra("isNoteDeleted", true);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }
                    new DeleteNoteTasek().execute();
                }
            });
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogDeleteNote.dismiss();
                }
            });
        }
        dialogDeleteNote.show();
    }

    //노트 컬러 + imageAdd 부분
    //Bottom Sheets는 화면의 아래쪽에 표시되고
    //처음에는 내용의 일부만 보여지지만 위쪽 방향으로 쓸어 올려 전체 내용을 표시하는 컴포넌트
    private void initVariousColor() {
        final LinearLayout layoutVarious = findViewById(R.id.layoutVarious);
        final BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutVarious);
        layoutVarious.findViewById(R.id.textVarious).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        final ImageView imageColor1 = layoutVarious.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutVarious.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutVarious.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutVarious.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutVarious.findViewById(R.id.imageColor5);
        //원하는 색상 클릭시 색상이 바뀌게 하고, 선택한 imageView만 체크표시 나오게
        layoutVarious.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333"; // default color
                imageColor1.setImageResource(R.drawable.ic_done);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicatorColor();
            }
        });
        layoutVarious.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDBE3B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.ic_done);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicatorColor();
            }
        });
        layoutVarious.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.ic_done);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicatorColor();
            }
        });
        layoutVarious.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52FC";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.ic_done);
                imageColor5.setImageResource(0);
                setViewSubtitleIndicatorColor();
            }
        });
        layoutVarious.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.ic_done);
                setViewSubtitleIndicatorColor();
            }
        });
        //performClick() -> 클릭 이벤트를 강제로 발생시킨다
        if (alreadyAvailableNote != null && alreadyAvailableNote.getColor() != null && !alreadyAvailableNote.getColor().trim().isEmpty()) {
            switch (alreadyAvailableNote.getColor()) {
                case "333333":
                    layoutVarious.findViewById(R.id.viewColor1).performClick();
                    break;
                case "#FDBE3B":
                    layoutVarious.findViewById(R.id.viewColor2).performClick();
                    break;
                case "#FF4842":
                    layoutVarious.findViewById(R.id.viewColor3).performClick();
                    break;
                case "#3A52FC":
                    layoutVarious.findViewById(R.id.viewColor4).performClick();
                    break;
                case "#000000":
                    layoutVarious.findViewById(R.id.viewColor5).performClick();
                    break;
            }
        }

        //이미지 추가 onClickLinstener
        layoutVarious.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                //권한 확인 checkSelfPermission
                //권한 요청 requestPermissions() -> onRequestPermissionsResult()
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CreateNoteActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });

        //alreadyAvailableNote는 Update or View 일 때 null이 아니기 때문에 이 때만 delete가 보이도록한다.
        if (alreadyAvailableNote != null) {
            layoutVarious.findViewById(R.id.layoutDeleteNote).setVisibility(View.VISIBLE);
            layoutVarious.findViewById(R.id.layoutDeleteNote).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    showDeleteDialog();
                }
            });
        }
    }

    //선택된 노트 컬러로 같이 변경되도록
    private void setViewSubtitleIndicatorColor() {
        GradientDrawable gradientDrawble = (GradientDrawable) viewSubtitleIndicator.getBackground();
        gradientDrawble.setColor(Color.parseColor(selectedNoteColor));
    }

    //이미지 추가 부분
    //layout_various 에서 이미지 추가 누르면 권한요청 -> selectImage()
    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ImageNote.setImageBitmap(bitmap);
                        ImageNote.setVisibility(View.VISIBLE);
                        findViewById(R.id.imageRemove).setVisibility(View.VISIBLE);

                        selectedImagePath = getPathUri(selectedImageUri);
                    } catch (Exception exception) {
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private String getPathUri(Uri contentUri) {
        String filepath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            filepath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filepath = cursor.getString(index);
            cursor.close();
        }
        return filepath;
    }
}