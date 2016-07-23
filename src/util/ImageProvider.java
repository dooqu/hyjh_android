package util;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;

import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.cache.disc.impl.*;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class ImageProvider
{
	protected ImageLoader loader;
	protected Handler handler;
	protected WebView webView;

	public ImageProvider(Context context, WebView webView) throws IOException
	{
		this.webView = webView;
		handler = new Handler();
		this.loader = ImageLoader.getInstance();

		/*
		 * DisplayImageOptions options = new DisplayImageOptions.Builder()
		 * .resetViewBeforeLoading(false) // default .cacheInMemory(false) //
		 * default .cacheOnDisk(true) // default .considerExifParams(false) //
		 * default .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) //
		 * default .bitmapConfig(Bitmap.Config.ARGB_8888) // default
		 * .displayer(new SimpleBitmapDisplayer()) // default .handler(new
		 * Handler()) // default .build();
		 * 
		 * 
		 * File cacheDir = StorageUtils.getCacheDirectory(context);
		 * 
		 * ImageLoaderConfiguration config = new
		 * ImageLoaderConfiguration.Builder(context)
		 * .memoryCacheExtraOptions(480, 800) // default = device screen
		 * dimensions .diskCacheExtraOptions(480, 800, null)
		 * .defaultDisplayImageOptions(options) .threadPoolSize(3) // default
		 * .threadPriority(Thread.NORM_PRIORITY - 2) // default
		 * .denyCacheImageMultipleSizesInMemory() .diskCache(new
		 * UnlimitedDiskCache(cacheDir)) // default .diskCacheSize(200 * 1024 *
		 * 1024) .diskCacheFileCount(300) .diskCacheFileNameGenerator(new
		 * HashCodeFileNameGenerator()) // default .imageDownloader(new
		 * BaseImageDownloader(context)) // default .writeDebugLogs() .build();
		 * this.loader.init(config);
		 */
	}

	public void load(String url, final String data)
	{
		this.loader.loadImage(url, new SimpleImageLoadingListener()
		{
			@Override
			public void onLoadingComplete(final String imageUri, View view,
					Bitmap loadedImage)
			{
				final String path = ImageProvider.this.loader.getDiskCache()
						.get(imageUri).getPath();

				handler.post(new Runnable()
				{
					public void run()
					{
						ImageProvider.this.webView
								.loadUrl("javascript:onImageLoaded('" + path
										+ "','" + data + "');");
					}
				});
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view)
			{
				final File cacheFile = ImageProvider.this.loader.getDiskCache()
						.get(imageUri);

				if (cacheFile.exists())
				{
					handler.post(new Runnable()
					{
						public void run()
						{
							ImageProvider.this.webView.loadUrl("javascript:onImageLoaded('"
											+ cacheFile.getPath() + "','"
											+ data + "');");
						}
					});
				}
			}
		});
	}
	
	

	public static Bitmap rotaingImageView(int angle, Bitmap bitmap)
	{
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
		return resizedBitmap;
	}
	
	
	/**
	 * 读取图片属性：旋转的角度
	 * @param path 图片绝对路径
	 * @return degree旋转的角度
	 */
	public static int readPictureDegree(String path)
	{
		int degree = 0;
		try
		{
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation)
			{
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return degree;
	}

}
