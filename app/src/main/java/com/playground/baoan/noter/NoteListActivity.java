package com.playground.baoan.noter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
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

public class NoteListActivity extends AppCompatActivity implements RecyclerNoteAdapter.myItemClickListener{
    //Unique codes for sharedPref
    public static final String SHARED_PREFS = "SharePrefs";
    public static final String NOTE_LIST = "NoteList";

	//Extra code for putExtra()
    public final static String EXTRA_NOTE = "ExtraNote";
    public final static String EXTRA_ID = "ExtraId";

    //requestCode for startActivityForResult
    public final int EDIT_RESULT = 1;
    public final int CREATE_RESULT = 3;

    private RecyclerNoteAdapter recyclerNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);

        //create an empty List of Note
        final List<Note> noteData = new ArrayList<>();

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
                        noteData.add(gson.fromJson(json, Note.class));
                    }
                }
            }
        });

        /**USING RECYCLERVIEW*/
        //get the reference to the RecyclerView
        RecyclerView noteList = (RecyclerView) findViewById(R.id.recycler_view);

        //create a new RecyclerAdapter with data
        recyclerNoteAdapter = new RecyclerNoteAdapter(noteData);

        //do this if layout does not change size for better performance
        noteList.setHasFixedSize(true);

        //set the layout wrapper for the RecyclerView
        //using a GridLayoutManager with 2 columns
        noteList.setLayoutManager(new GridLayoutManager(this, 2));
        //add spacing around items using ItemDecoration
        noteList.addItemDecoration(new SpacingItemDecoration(
                getResources().getDimensionPixelOffset(R.dimen.note_spacing)));

        //set the adapter for the noteList
        noteList.setAdapter(recyclerNoteAdapter);
        /**USING RECYCLERVIEW*/
    }

    /**the self-implemented OnItemClickListener of RecyclerView
     * because RecyclerView does not come with one*/
    @Override
    public void itemClicked(int id, Note note) {
        //create an Intent to EditActivity
        Intent editIntent = new Intent(NoteListActivity.this, NoteEditActivity.class);
        //put in the Intent the Note which is chosen with code EXTRA_NOTE
        editIntent.putExtra(EXTRA_NOTE, note);
        //put in the Intent the ID of the item in the Adapter
        editIntent.putExtra(EXTRA_ID, id);
        //start the Intent with requestCode EDIT_RESULT
        startActivityForResult(editIntent, EDIT_RESULT);
    }
    /**--------------------------------------------------*/

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
                        //call the recyclerNoteAdapter to update
                        recyclerNoteAdapter.changeItem(editedNote, chosenId);
                        break;
                    //if the Note is deleted through Edit Activity
                    case NoteEditActivity.RESULT_DELETE:
                    	//get the ID of the previously chosen Note
                        chosenId = data.getExtras().getInt(EXTRA_ID);
                        //call the recyclerNoteAdapter to remove
                        recyclerNoteAdapter.removeItem(chosenId);
                        break;
                }
                break;
            //if the requestCode is CREATE_RESULT (by clicking at the plus icon)
            case CREATE_RESULT:
            	//if the Note has been created successfully
                if (resultCode == RESULT_OK) {
                	//get the created Note
                    Note createdNote = data.getExtras().getParcelable(NoteEditActivity.RESULT_NOTE);
                    //call the recyclerNoteAdapter to insert
                    recyclerNoteAdapter.addItem(createdNote);
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
        //use a Handler for better performance
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                //use GSON API to store Object as json
                Gson gson = new Gson();
                List<String> jsonArray = new ArrayList<>();
                //save all the Notes as json strings
                for (Note note: recyclerNoteAdapter.getNoteList()) {
                    jsonArray.add(gson.toJson(note));
                }
                //have to do this because of SharedPrefs scrambling Set
                String jsonArrayJson = gson.toJson(jsonArray);
                sharedPreferences
                        .edit()
                        .putString(NOTE_LIST, jsonArrayJson)
                        .apply();
            }
        });
    }
}
