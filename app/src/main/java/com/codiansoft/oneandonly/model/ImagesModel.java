package com.codiansoft.oneandonly.model;

import android.graphics.Bitmap;

/**
 * Created by salal-khan on 6/1/2017.
 */

public class ImagesModel {
    Bitmap bitmap;

    public ImagesModel(Bitmap bitmap) {
        this.bitmap = bitmap;

    }


    public Bitmap getImageBitmap() {
        return bitmap;
    }
}
