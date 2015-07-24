package com.ilife.suixinji.ui;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ilife.suixinji.FlipActivity;
import com.ilife.suixinji.R;
import com.ilife.suixinji.util.IOOperation;

public class PhotoImageView extends ImageView {

	private int imageIndex = 0;
	private ArrayList<String> allImagePath;

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			showFullPhoto(imageIndex);
		}

	};

	private void showFullPhoto(int imageIndex) {
		Intent intent = new Intent(getContext(), FlipActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("imageIndex", imageIndex);
		bundle.putStringArrayList("imageList", allImagePath);
		intent.putExtras(bundle);
		getContext().startActivity(intent);
	}
	
	public PhotoImageView(Context context) {
		super(context);
	}

	public PhotoImageView(Context context, int imageIndex, ArrayList<String> imagePath) {
		super(context);
		allImagePath = imagePath;
		this.imageIndex = imageIndex;
		setBackgroundResource(R.drawable.frame_photo);
		setPadding(1, 1, 1, 1);
		this.setOnClickListener(listener);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(150, 150);
		lp.leftMargin = 5;
		lp.rightMargin = 5;
		setLayoutParams(lp);
	}

	public void setDrawableByPath(String path) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = IOOperation.calculateInSampleSize(options, 150, 150);
        options.inJustDecodeBounds = false;
        Bitmap bm =BitmapFactory.decodeFile(path, options);
        int degree = NewRecordActivity.readPictureDegree(path);
        bm = NewRecordActivity.rotaingBitmap(degree, bm);
		setImageBitmap(bm);
	}
	
	public Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);//
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }
}
