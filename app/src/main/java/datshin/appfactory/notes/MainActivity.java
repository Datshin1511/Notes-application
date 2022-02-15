package datshin.appfactory.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    MenuInflater menuInflater = null;
    ListView listView;
    static int count=0;
    static SharedPreferences sharedPreferences = null;
    static ArrayAdapter arrayAdapter;
    static ArrayList<String> notes = new ArrayList<String>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menuInflater = (MenuInflater) getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.addNewNote){
            Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
            startActivity(intent);
            return true;
        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listView);
        sharedPreferences = getApplicationContext().getSharedPreferences("datshin.appfactory.notes", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("Content", null);
        if(set == null){
            notes.add("Example Note");
        }
        else{
            notes = new ArrayList(set);
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), NoteActivity.class);
                intent.putExtra("noteId", i);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                int noteToBeDeleted = i;

                new AlertDialog.Builder(MainActivity.this).setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure ?")
                        .setMessage("Deleting this note will erase all the data present in it.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(noteToBeDeleted);
                                arrayAdapter.notifyDataSetChanged();

                                MainActivity.sharedPreferences = getApplication().getSharedPreferences("datshin.appfactory.notes", Context.MODE_PRIVATE);
                                NoteActivity.set = new HashSet<String>(MainActivity.notes);
                                MainActivity.sharedPreferences.edit().putStringSet("Content", NoteActivity.set).apply();
                                Log.i("Info", "Note : "+Integer.toString(noteToBeDeleted)+" is deleted");
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });

    }
}