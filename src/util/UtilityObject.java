package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import android.content.Context;
import android.widget.Toast;

import org.json.*;

public class UtilityObject 
{
	Context context_;
	public UtilityObject(Context context)
	{
		this.context_ = context;
	}
	
	public Boolean saveUserInfoIntoRes(String userid, String username, String mobile, String passwordMD5, String roleName, String departmentName, String createDate, String lastLoginDate)
	{
		try 
		{			
			JSONObject jsobj = new JSONObject();
			
			jsobj.put("UserId", userid);
			jsobj.put("Username", username);
			jsobj.put("PasswordMD5", passwordMD5);
			jsobj.put("Mobile", mobile);
			jsobj.put("RoleName", roleName);
			jsobj.put("DepartmentName", departmentName);
			jsobj.put("CreateDate", createDate);
			jsobj.put("LastLoginDate", lastLoginDate);
			
			String jsString = jsobj.toString();			
			byte[] buffer = jsString.getBytes(Charset.forName("UTF-8"));
			
			String filePath = context_.getExternalFilesDir("").getPath() + "/user_info_res";	
			File resFile = new File(filePath);
			
			try 
			{
				Boolean isExist = resFile.exists();
				Boolean isFile = resFile.isFile();
				
				if(isExist == false)
				{
					resFile.createNewFile();
				}
			}
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}			
			
			FileOutputStream writer = new FileOutputStream(resFile, false);			
			
			try
			{
				writer.write(buffer);				
				return true;
			} 
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
				try 
				{
					writer.close();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		catch (FileNotFoundException e) 
		{			
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String getUserInfoFromRes()
	{
		String reVal = "{}";
		
		String filePath = context_.getExternalFilesDir("").getPath() + "/user_info_res";
		
		File resFile = null;
		
		try
		{
			resFile = new File(filePath);		
			
			if(resFile.exists() == false)
				return reVal;		
		}
		catch(Exception ex)
		{
			return "{}";
		}

		
		FileInputStream reader = null;
		try 
		{
			reader = new FileInputStream(resFile);
		} 
		catch (FileNotFoundException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
			return reVal;
		}
		
		if(reader != null)
		{
			int ret = 0;
			byte[] buffer = new byte[2048];
			
			try 
			{
				ret = reader.read(buffer, 0, buffer.length);				
				reVal = new String(buffer, "UTF-8").trim();
				
				return reVal;
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return reVal;
			}	
			finally
			{
				try
				{
					reader.close();
				} 
				catch (IOException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}			
		}
		
		return reVal;
	}
	
	public void clearUserInfoAtRes()
	{
		String filePath = context_.getExternalFilesDir("").getPath() + "/user_info_res";
		
		File resFile = null;
		
		try
		{
			resFile = new File(filePath);		
			
			if(resFile.exists() == true)
			{
				resFile.delete();
			}
		}
		catch(Exception ex)
		{
			return;
		}		
	}
	
	public void toast(String msg)
	{
		Toast.makeText(this.context_, msg, 1500).show();
	}
}
