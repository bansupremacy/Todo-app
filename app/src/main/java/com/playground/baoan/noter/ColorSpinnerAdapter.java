package com.playground.baoan.noter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Bao An on 9/21/2016.
 */
public class ColorSpinnerAdapter extends ArrayAdapter<Note.Color> {
    private Context context;
    private int resource;
    private List<Note.Color> colors;
    private class Holder {
        ImageView colorBlock;
    }

    public ColorSpinnerAdapter(Context context, int resource, List<Note.Color> colors) {
        super(context, resource, colors);
        this.context = context;
        this.resource = resource;
        this.colors = colors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View colorView = convertView;
        Holder holder;

        if (colorView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            colorView = inflater.inflate(resource, parent, false);

            holder = new Holder();
            holder.colorBlock = (ImageView) colorView.findViewById(R.id.color_block);

            colorView.setTag(holder);
        }
        else {
            holder = (Holder) colorView.getTag();
        }

        Note.Color color = colors.get(position);
        holder.colorBlock.setColorFilter(ContextCompat.getColor(context, color.getColorId()));

        return colorView;
    }

    @Override
    //A identical copy of getView, but still has to be implemented for Spinner
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View colorView = convertView;
        Holder holder;

        if (colorView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            colorView = inflater.inflate(resource, parent, false);

            holder = new Holder();
            holder.colorBlock = (ImageView) colorView.findViewById(R.id.color_block);

            colorView.setTag(holder);
        }
        else {
            holder = (Holder) colorView.getTag();
        }

        Note.Color color = colors.get(position);
        holder.colorBlock.setColorFilter(ContextCompat.getColor(context, color.getColorId()));

        return colorView;
    }
}
