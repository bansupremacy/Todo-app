package com.playground.baoan.noter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Bao An on 10/3/2016.
 */

//RecyclerView support the concept of ItemDecoration for padding and drawing around item
public class SpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int spacing;

    SpacingItemDecoration(int spacing) {
        this.spacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //set the offset of the area wrapping the item
        outRect.set(spacing, spacing, spacing, spacing);
    }
}
