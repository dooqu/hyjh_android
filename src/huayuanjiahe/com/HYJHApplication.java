package huayuanjiahe.com;


import java.io.File;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;

import com.nostra13.universalimageloader.core.*;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.cache.disc.impl.*;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.utils.StorageUtils;


public class HYJHApplication extends Application
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		this.activities = new HashSet<Activity>();
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .resetViewBeforeLoading(false)  // default
        .cacheInMemory(false) // default
        .cacheOnDisk(true) // default
        .considerExifParams(false) // default
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .bitmapConfig(Bitmap.Config.ARGB_8888) // default
        .displayer(new SimpleBitmapDisplayer()) // default
        .handler(new Handler()) // default
        .build();
		
		
		File cacheDir = StorageUtils.getCacheDirectory(this);
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
        .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
        .diskCacheExtraOptions(480, 800, null)
        .defaultDisplayImageOptions(options)
        .threadPoolSize(3) // default
        .threadPriority(Thread.NORM_PRIORITY - 2) // default
        .denyCacheImageMultipleSizesInMemory()
        .diskCache(new UnlimitedDiskCache(cacheDir)) // default
        .diskCacheSize(200 * 1024 * 1024)
        .diskCacheFileCount(300)
        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
        .imageDownloader(new BaseImageDownloader(this)) // default
        .writeDebugLogs()
        .build();
		ImageLoader.getInstance().init(config);
	}
	
	Set<Activity> activities;
	
	public void regist(Activity act)
	{
		if(this.activities.contains(act)== false)
		this.activities.add(act);
	}
	
	public void unregist(Activity act)
	{
		if(this.activities.contains(act))
		{
			this.activities.remove(act);
		}
	}
	
	public void closeAll()
	{
		Iterator<Activity> e = this.activities.iterator();
		
		while(e.hasNext())
		{
			Activity currAct = e.next();
			
			try
			{
				if(currAct != null)
					currAct.finish();
			}
			catch(Exception ex)
			{
				
			}			
		}
		
	}
}
