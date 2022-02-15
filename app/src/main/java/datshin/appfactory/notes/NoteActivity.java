package datshin.appfactory.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.util.HashSet;

public class NoteActivity extends AppCompatActivity {

    int noteId;
    static HashSet<String> set=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        EditText editContent = findViewById(R.id.editContent);
        Intent intent = getIntent();

        noteId = intent.getIntExtra("noteId", -1);

        if(noteId != -1){
                editContent.setText(MainActivity.notes.get(noteId));
        }
        else{
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
        }

        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MainActivity.notes.set(noteId, String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();

                set = new HashSet<String>(MainActivity.notes);
                MainActivity.sharedPreferences.edit().putStringSet("Content", set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}