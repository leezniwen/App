package com.example.msiqlab;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

public class littlesupper_assist_viewbinder implements SimpleAdapter.ViewBinder {
    @Override
    public boolean setViewValue(View view, Object data, String textRepresentation) {
        if (view instanceof ImageView && data instanceof Bitmap) {
            ImageView iv = (ImageView) view;
            iv.setImageBitmap((Bitmap) data);
            return true;
        }

        return false;
    }
}
