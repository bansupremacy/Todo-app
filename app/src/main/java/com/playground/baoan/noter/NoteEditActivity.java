package com.playground.baoan.noter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class NoteEditActivity extends AppCompatActivity {
	//the extraCode of the returned Note for putExtra()
    public final static String RESULT_NOTE = "ResultNote";
    //the resultCode for startActivityForResult
    public final static int RESULT_DELETE = 2;

    //the id of the item chosen from the AdapterView
    private int chosenId;

    private Spinner colorChooser;
    private RelativeLayout contentRegion;
    private LinearLayout bigActionBar;
    private EditText contentEdit;
    private Note.Color noteColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        //get the invoking intent
        Intent editIntent = getIntent();

        Note editNote = editIntent.getExtras().getParcelable(NoteListActivity.EXTRA_NOTE);
        chosenId = editIntent.getExtras().getInt(NoteListActivity.EXTRA_ID);
        //get the Color from the Note object
        noteColor = editNote.getColor();

        //get the contentRegion
        contentRegion = (RelativeLayout) findViewById(R.id.content_region);
        //get the action bar of the note
        bigActionBar = (LinearLayout) findViewById(R.id.big_note_action_bar);
        //set the color of the two
        contentRegion.setBackgroundColor(ContextCompat.getColor(this, noteColor.getColorId()));
        bigActionBar.setBackgroundColor(ContextCompat.getColor(this, noteColor.getAltColorId()));

        //put text into the edit field
        contentEdit = (EditText) findViewById(R.id.content_edit);
        contentEdit.setText(editNote.getContent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_activity_menu, menu);

        //get the Spinner
        MenuItem menuItem = menu.findItem(R.id.action_color_chooser);
        colorChooser = (Spinner) MenuItemCompat.getActionView(menuItem);

        //populate the Spinner with the Color List get from the EnumSet
        ColorSpinnerAdapter colorAdapter = new ColorSpinnerAdapter(
                this.getSupportActionBar().getThemedContext(),
                R.layout.color_layout,
                Note.Color.getColorList()
        );
        colorChooser.setAdapter(colorAdapter);

        //set the selection of the Spinner to the given Color by pass the position of it in the Adapter
        //this is just like a man-made selection (it is actually get selected, so onItemSelected
        //will be called
        colorChooser.setSelection(colorAdapter.getPosition(noteColor));

        colorChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Note.Color chosenColor = (Note.Color) parent.getAdapter().getItem(position);

                contentRegion.setBackgroundColor(ContextCompat.getColor(
                        NoteEditActivity.this,
                        chosenColor.getColorId()));
                bigActionBar.setBackgroundColor(ContextCompat.getColor(
                        NoteEditActivity.this,
                        chosenColor.getAltColorId()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch(itemId) {
            case R.id.action_save:
                Intent resultIntent = new Intent();
                //put the new Note content and Color into the Intent
                resultIntent.putExtra(RESULT_NOTE, new Note(
                        contentEdit.getText().toString(),
                        (Note.Color) colorChooser.getSelectedItem()
                ));
                //put the chosen item id from the Adapter back to the ResultIntent
                resultIntent.putExtra(NoteListActivity.EXTRA_ID, chosenId);
                //set the resultCode to RESULT_OK for later referencing
                setResult(RESULT_OK, resultIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onDeleteButtonClick(View view) {
        Intent deleteIntent = new Intent();
        //put the id of the chosen Note in the Adapter
        deleteIntent.putExtra(NoteListActivity.EXTRA_ID, chosenId);
        //set the resultCode to RESULT_DELETE
        setResult(RESULT_DELETE, deleteIntent);
        finish();
    }
}
