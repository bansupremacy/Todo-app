package com.playground.baoan.noter;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by Bao An on 9/19/2016.
 */
public class Note implements Parcelable {
    //list of available colors
    public enum Color{
        YELLOW (R.color.yellow, R.color.dark_yellow),
        RED (R.color.red, R.color.dark_red),
        BLUE (R.color.blue, R.color.dark_blue),
        GREEN (R.color.green, R.color.dark_green),
        TEAL (R.color.teal, R.color.dark_teal),
        ORANGE (R.color.orange, R.color.dark_orange);

        private final int colorId;
        private final int altColorId;

        Color(int colorId, int altColorId) {
            this.colorId = colorId;
            this.altColorId = altColorId;
        }

        public int getColorId() {
            return colorId;
        }
        public int getAltColorId() {
            return altColorId;
        }
        public static List<Color> getColorList(){
            //make a Collection of enum 
            EnumSet<Color> allColor = EnumSet.allOf(Color.class);
            //create a List from the Collection 
            return new ArrayList<Color>(allColor);
            //the ArrayList takes a Collection as an argument
        }
    }
    private String content;
    private Color color;

    Note(String content, Color color) {
        this.content = content;
        this.color = color;
    }

    //getters
    public String getContent() {
        return content;
    }
    public Color getColor() {
        return color;
    }

    //Parcelable methods
    @Override
    public int describeContents() {
        return 0;
    }

    //write the properties into the Parcel (all of them)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        //Enum type is serializable
        dest.writeSerializable(color);
    }

    //get the properties from the Parcel
    private Note(Parcel in) {
        content = in.readString();
        color = (Color) in.readSerializable();
    }

    //a prerequisite of the process
    public static final Parcelable.Creator<Note> CREATOR
            = new Parcelable.Creator<Note>() {
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        public Note[] newArray(int size) {
            return new Note[size];
        }
    };
}
