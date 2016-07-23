package util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import huayuanjiahe.com.*;

public class ActivityManager 
{
	protected Activity context_;
	public ActivityManager(Activity context)
	{
		this.context_ = context;
	}
	
	public void startActivity(String packageName, String className)
	{
		Intent intent = new Intent();		
		
		try 
		{
			intent.setClassName(packageName, className);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

			if(intent != null)
			{
				this.context_.startActivity(intent);
			}			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void startActivity(String packageName, String className, String key, String Value)
	{
		Intent intent = new Intent();		
		
		try 
		{
			intent.setClassName(packageName, className);
			intent.putExtra(key, Value);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

			if(intent != null)
			{
				this.context_.startActivity(intent);
				//this.context_.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);				
			}			
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void finishSelf()
	{
		this.context_.finish();
	}
}
