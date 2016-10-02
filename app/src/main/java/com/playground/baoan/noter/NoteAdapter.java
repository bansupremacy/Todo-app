package com.playground.baoan.noter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bao An on 9/19/2016.
 */
public class NoteAdapter extends ArrayAdapter<Note>{
    private Context context;
    private int resource;
    private List<Note> notes;
    //a Holder sub-class to hold the GUIs 
    private class Holder {
        TextView contentView;
        RelativeLayout noteRegion;
        LinearLayout noteAction;
        ImageView deleteButton;
    }

    //THE CONSTRUCTOR
    public NoteAdapter(Context context, int resource, List<Note> notes) {
        super(context, resource, notes);
        this.context = context;	//the invoking context
        this.resource = resource;	//the layout resource to populate the View
        this.notes = notes;	//the data source
    }

    //POPULATE EACH VIEW
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View noteView = convertView;    //get the View of each cell
        Holder holder;  	//hold GUI elements

        //if the View is not yet instantiated
        if (noteView == null) {
        	//get the LayoutInflater from the invoking context
            LayoutInflater inflater = LayoutInflater.from(context);
            //inflate the GridView with the given layout resource
            noteView = inflater.inflate(resource, parent, false);

            //create the holder and pass to it GUI elements
            holder = new Holder();
            holder.contentView = (TextView) noteView.findViewById(R.id.content_view);
            holder.noteRegion = (RelativeLayout) noteView.findViewById(R.id.note_region);
            holder.noteAction = (LinearLayout) noteView.findViewById(R.id.note_action_bar);
            holder.deleteButton = (ImageView) noteView.findViewById(R.id.delete_button);

            //make the View remember the holder
            noteView.setTag(holder);
        }
        else {
        	//if the View is already instantiated --> get the tagged Holder from the View
            holder = (Holder) noteView.getTag();
        }

        //get the Object from the data source
        Note myNote = notes.get(position);

        //populate the View
        holder.contentView.setText(myNote.getContent());
        //ContextCompat is to get the color from resource with the given Id
        holder.noteRegion.setBackgroundColor(ContextCompat.getColor(context, myNote.getColor().getColorId()));
        holder.noteAction.setBackgroundColor(ContextCompat.getColor(context, myNote.getColor().getAltColorId()));

        //add action DELETE to delete button (has to be in the Adapter implementation itself)
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note.notes.remove(position);
                notifyDataSetChanged();	//notify the adapter of data change. 
                //In the implementation of Adapter it can be called implicitly
                //--> AdapterView will redrawn itself
            }
        });

        return noteView;
    }
}