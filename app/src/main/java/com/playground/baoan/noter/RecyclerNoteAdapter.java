package com.playground.baoan.noter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bao An on 10/3/2016.
 */

public class RecyclerNoteAdapter extends RecyclerView.Adapter<RecyclerNoteAdapter.Holder> {
    private List<Note> noteList;
    private Context context;

    //an interface for the OnClick of noteView to communicate
    //with the Activity calling the Adapter
    interface myItemClickListener {
        void itemClicked(int id, Note note);
    }
    private myItemClickListener listener;

    //the Holder class to hold the references to View elements
    public static class Holder extends RecyclerView.ViewHolder {
        //provide access to all the views of the noteView
        public TextView contentView;
        public RelativeLayout noteRegion;
        public LinearLayout noteAction;
        public ImageView deleteButton;

        public Holder(View noteView) {
            super(noteView);
            //using the noteView to get access to the references of the sub views
            this.contentView = (TextView) noteView.findViewById(R.id.content_view);
            this.noteRegion = (RelativeLayout) noteView.findViewById(R.id.note_region);
            this.noteAction = (LinearLayout) noteView.findViewById(R.id.note_action_bar);
            this.deleteButton = (ImageView) noteView.findViewById(R.id.delete_button);
        }
    }

    //provide data source for the Adapter
    public RecyclerNoteAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    //create new views
    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        //get the invoking context
        context = parent.getContext();

        //get the click listener from context which implements the myOnItemClickListener interface
        listener = (myItemClickListener) context;

        //instantiate a new view
        View view = LayoutInflater
                .from(context)
                .inflate(R.layout.note_layout, parent, false);

        //return the Holder with the newly created View
        return new Holder(view);
    }

    //set the content of the view
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        //get the note at the chosen id
        Note chosenNote = noteList.get(position);
        //get the color
        Note.Color chosenColor = chosenNote.getColor();

        //populate data to the views
        //the content of the note
        holder.contentView.setText(chosenNote.getContent());
        //the color of the note background
        holder.noteRegion.setBackgroundColor(ContextCompat
                .getColor(context, chosenColor.getColorId()));
        //the color of the note top bar
        holder.noteAction.setBackgroundColor(ContextCompat
                .getColor(context, chosenColor.getAltColorId()));

        //get the position of the current item
        final int currentPos = holder.getAdapterPosition();

        //set the DELETE ACTION for the delete button
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(currentPos);
            }
        });

        //because RecyclerView does not come with a OnItemClickListener, one has to implement it himself
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.itemClicked(currentPos, noteList.get(currentPos));
            }
        });
    }

    //return the number of items in the data set
    @Override
    public int getItemCount() {
        return noteList.size();
    }

    //methods defining add and remove actions
    public void removeItem(int position) {
        noteList.remove(position);
        //use this call to get the remove animation to work
        notifyItemRemoved(position);
        //fix wrong index
        notifyItemRangeChanged(position, getItemCount());
    }

    public void addItem(Note note) {
        noteList.add(note);
        //use this call to get the insert animation to work
        //use getItemCount because always add to the end of the list
        notifyItemInserted(getItemCount());
    }

    public void changeItem(Note note, int position) {
        noteList.set(position, note);
        notifyItemChanged(position);
    }

    public List<Note> getNoteList() {
        return noteList;
    }
}
