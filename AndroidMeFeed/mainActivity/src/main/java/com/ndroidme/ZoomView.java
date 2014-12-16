package com.ndroidme;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;
import android.widget.ImageView;

public class ZoomView extends ImageView {

	private ScaleGestureDetector scaleGesture;

	public ZoomView(Context context, AttributeSet attrs) {
		super(context, attrs);

		initializeView();

	}

	private void initializeView() {

		setScaleType(ScaleType.MATRIX);

		scaleGesture = new ScaleGestureDetector(getContext(),
				new ScaleListener());

	}

	public ZoomView(Context context) {
		super(context);

		initializeView();
	}

	public void setBitmap(Bitmap bitmap) {
		setImageBitmap(bitmap);
		center(bitmap.getHeight(),bitmap.getWidth());

	}

	private void center(int bitmapHeight,int bitmapWidth) {
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int centerHeight= (screenHeight-bitmapHeight)/2;
        int centerWidth= (screenWidth-bitmapWidth)/2;
		Matrix translateMatrix = new Matrix();

		translateMatrix.postTranslate(centerWidth,centerHeight);
		Matrix imageMatrix = new Matrix(getImageMatrix());
		imageMatrix.postConcat(translateMatrix);
		setImageMatrix(imageMatrix);
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
		boolean handled = scaleGesture.onTouchEvent(motionEvent);
		if (handled == false) {
			handled = super.onTouchEvent(motionEvent);
		}
		return handled;
	}

	public class ScaleListener implements OnScaleGestureListener {
		float lastFocusX;
		float lastFocusY;

		@Override
		public boolean onScale(ScaleGestureDetector detector) {

			Matrix imageMatrix = new Matrix(getImageMatrix());
			float focusX = detector.getFocusX();
			float focusY = detector.getFocusY();
			float focusShiftX = focusX - lastFocusX;
			float focusShiftY = focusY - lastFocusY;
			float scaleFactor = detector.getScaleFactor();
			Matrix scaleMatrix = new Matrix();
			scaleMatrix.postTranslate(-focusX, -focusY);
			scaleMatrix.postScale(scaleFactor, scaleFactor);
			scaleMatrix.postTranslate(focusX + focusShiftX, focusY
					+ focusShiftY);
			imageMatrix.postConcat(scaleMatrix);
			lastFocusX = focusX;
			lastFocusY = focusY;
			setImageMatrix(imageMatrix);
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			lastFocusX = detector.getFocusX();
			lastFocusY = detector.getFocusY();

			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {

		}

	}

}
