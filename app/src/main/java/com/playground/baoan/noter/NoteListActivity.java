package com.playground.baoan.noter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class NoteListActivity extends AppCompatActivity {
    //Unique codes for sharedPref
    public static final String SHARED_PREFS = "SharePrefs";
    public static final String NOTE_LIST = "NoteList";

	//Extra code for putExtra()
    public final static String EXTRA_NOTE = "ExtraNote";
    public final static String EXTRA_ID = "ExtraId";

    //requestCode for startActivityForResult
    public final int EDIT_RESULT = 1;
    public final int CREATE_RESULT = 3;

    private NoteAdapter noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        //get the Note list from SharedPreferences (using Handler for better performance)
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                //the second para states that it will return "" if no jsonArrayJson is found
                String jsonArrayJson = sharedPreferences.getString(NOTE_LIST, "");
                //get the jsonArray from the JSON jsonArray
                List<String> jsonArray = gson.fromJson(jsonArrayJson, List.class);
                //get the Note object from the json list
                if (jsonArray != null) {
                    for (String json: jsonArray) {
                        Note.notes.add(gson.fromJson(json, Note.class));
                    }
                }
            }
        });

        //populate the GridView with notes List
        GridView noteList = (GridView) findViewById(R.id.note_list);
        noteAdapter = new NoteAdapter(
                this,
                R.layout.note_layout,
                Note.notes
        );

        noteList.setAdapter(noteAdapter);

        //create a item click listener for the listView
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            	//create an Intent to EditActivity
                Intent editIntent = new Intent(NoteListActivity.this, NoteEditActivity.class);
                //put in the Intent the Note which is chosen with code EXTRA_NOTE
                editIntent.putExtra(EXTRA_NOTE, (Note) parent.getAdapter().getItem(position));
                //put in the Intent the ID of the item in the Adapter
                editIntent.putExtra(EXTRA_ID, position);
                //start the Intent with requestCode EDIT_RESULT
                startActivityForResult(editIntent, EDIT_RESULT);
            }
        });
    }

    //after startActivityForResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
        	//if the requestCode is EDIT_RESULT (by clicking at the Note)
            case EDIT_RESULT:
                switch(resultCode) {
                	//if the Note is edited successfully
                    case RESULT_OK:
                        int chosenId = data.getExtras().getInt(EXTRA_ID);
                        //get the edited Note created by EditActivity
                        Note editedNote = data.getExtras().getParcelable(NoteEditActivity.RESULT_NOTE);
                        //replace the Note with the chosen ID with the new edited Note
                        Note.notes.set(chosenId, editedNote);
                        //notify the Adapter of data change --> the GridView will get redrawn
                        noteAdapter.notifyDataSetChanged();
                        break;
                    //if the Note is deleted through Edit Activity
                    case NoteEditActivity.RESULT_DELETE:
                    	//get the ID of the previously chosen Note
                        chosenId = data.getExtras().getInt(EXTRA_ID);
                        //delete it
                        Note.notes.remove(chosenId);
                        //notify the Adapter of the data change
                        noteAdapter.notifyDataSetChanged();
                        break;
                }
                break;
            //if the requestCode is CREATE_RESULT (by clicking at the plus icon)
            case CREATE_RESULT:
            	//if the Note has been created successfully
                if (resultCode == RESULT_OK) {
                	//get the created Note
                    Note createdNote = data.getExtras().getParcelable(NoteEditActivity.RESULT_NOTE);
                    //add it into the end of the List
                    Note.notes.add(createdNote);
                    //notify the Adapter of data change
                    noteAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    //create the Menu app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	//get the MenuInflater from the Activity
        MenuInflater inflater = getMenuInflater();
        //inflate the Menu with the layout given
        inflater.inflate(R.menu.list_activity_menu, menu);
        return true;
    }

    //if one of the options in the Menu bar is clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	//get the ID of the clicked item
        switch(item.getItemId()) {
            case R.id.action_create_new:   //create new note
                Intent createIntent = new Intent(NoteListActivity.this, NoteEditActivity.class);
                //put into the Intent a whole new Note with empty content and Color YELLOW
                createIntent.putExtra(EXTRA_NOTE, new Note("", Note.Color.YELLOW));
                //start the Intent with the requestCode CREATE_RESULT
                startActivityForResult(createIntent, CREATE_RESULT);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //when the activity get interrupted --> save all the notes into SharedPreferences
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //use GSON API to store Object as json
        Gson gson = new Gson();
        List<String> jsonArray = new ArrayList<>();
        //save all the Notes as json strings
        for (Note note: Note.notes) {
            jsonArray.add(gson.toJson(note));
        }
        //have to do this because of SharedPrefs scrambling Set
        String jsonArrayJson = gson.toJson(jsonArray);
        sharedPreferences
                .edit()
                .putString(NOTE_LIST, jsonArrayJson)
                .apply();
    }
}
