package com.ndroidme;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;

public class ScaleBitmap {
	

	public ScaleBitmap() {
	
	}
    public static Bitmap scaleToView(Context context,String photoPath)
    {
    	DisplayMetrics metrics= new DisplayMetrics();
       ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
       return scaleToView(context,photoPath,metrics.widthPixels,metrics.heightPixels);
    }
    
	public static Bitmap scaleToView(Context context,String photoPath,int width, int height) {
		/*final Uri imageURI = Uri.parse("file:"+photoPath);
		BufferedInputStream inStr = null;
		try {
			inStr = new BufferedInputStream(context.getContentResolver()
					.openInputStream(imageURI));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
		    String message=e1.getMessage();
			e1.printStackTrace();
		}*/
//         DisplayMetrics metrics= new DisplayMetrics();
//        ((CreateDinner)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        float density= metrics.density;
//        int height = 100;
//		int width =(int) (100*density);

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		BitmapFactory.decodeFile(photoPath, options);
		
        
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		

		Bitmap selectImg = BitmapFactory.decodeFile(photoPath, options);
		return selectImg;
	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/(float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

}
