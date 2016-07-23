package huayuanjiahe.com;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

import util.ImageProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import modal.HouseInfo;

public class EditHouse extends Activity
{
	protected WebView webView;
	Map<String, Boolean> tasklistStatus;
	Handler handler;
	String sessionCookie;
	String mobile = null;
	String passwordMD5 = null;
	HouseInfo houseinfo;
	protected String uploadServiceURL;

	public EditHouse()
	{
		this.tasklistStatus = new Hashtable<String, Boolean>();
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);

				switch (msg.what)
				{
					case 1:
					{
						String[] args = (String[]) msg.obj;
						EditHouse.this.webView
								.loadUrl("javascript:onCreateUploaddingImage('"
										+ args[0] + "','" + args[1] + "');");
					}
						break;

					case 2:
					{
						String taskId = msg.obj.toString();
						EditHouse.this.webView
								.loadUrl("javascript:onUploaddingImageProgress('"
										+ taskId
										+ "',"
										+ String.valueOf(msg.arg1)
										+ ","
										+ String.valueOf(msg.arg2) + ")");
					}
						break;
						
					case 3:
					{
						String taskId = msg.obj.toString();
						EditHouse.this.webView
						.loadUrl("javascript:onUploaddingImageCanceled('" + taskId + "');");
					}
					break;

					case 4:
					{
						String[] args = (String[]) msg.obj;
						EditHouse.this.webView
								.loadUrl("javascript:onUploaddingImageError('"
										+ args[0] + "','" + args[1] +  "','" + args[2] +"');");
					}
						break;

					case 5:
					{
						String[] args = (String[]) msg.obj;
						EditHouse.this.webView
								.loadUrl("javascript:onUploaddingImageSuccess('"
										+ args[0] + "','" + args[1] + "');");				
					}
						break;

				}
			}
		};
	}

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		((HYJHApplication) this.getApplication()).regist(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		Intent intent = getIntent();
		String houseId = intent.getStringExtra("house_id");
		houseinfo = new HouseInfo();
		houseinfo.setHouseId(houseId);

		webView = (WebView) this.findViewById(R.id.webView1);

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);

		try
		{
			webView.addJavascriptInterface(this, "ActivityObject");
			webView.addJavascriptInterface(new util.UtilityObject(this),
					"UtilityObject");
			webView.addJavascriptInterface(
					new util.ImageProvider(this.getApplicationContext(),
							webView), "ImageProvider");
			webView.addJavascriptInterface(new util.ActivityManager(this),
					"ActivityManager");
			webView.addJavascriptInterface(houseinfo, "HouseInfo");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		webView.loadUrl("file:///android_asset/edithouse.html");
	}
	
	public synchronized void addTask(String taskId)
	{
		if(this.tasklistStatus.containsKey(taskId) == false)
		{
			this.tasklistStatus.put(taskId, true);
		}
	}
	
	public synchronized void RemoveTask(String taskId)
	{
		if(this.tasklistStatus.containsKey(taskId) == true)
		{
			this.tasklistStatus.remove(taskId);
		}		
	}
	
	public synchronized boolean hasTask(String taskId)
	{
		return this.tasklistStatus.containsKey(taskId);
	}
	
	public synchronized void setStatus(String taskId, boolean running)
	{
		if(this.tasklistStatus.containsKey(taskId) == true)
		{
			this.tasklistStatus.put(taskId, running);
		}			
	}
	
	public void setAuthInfo(String mobile, String passwordMD5)
	{
		this.mobile = mobile;
		this.passwordMD5 = passwordMD5;
	}
	
	public void setUploadServiceURL(String url)
	{
		this.uploadServiceURL = url;
	}

	@Override
	public void onDestroy()
	{
		((HYJHApplication) this.getApplication()).unregist(this);
		super.onDestroy();
	}
	
	public void setSessionCookie(String cookieString)
	{
		this.sessionCookie = cookieString;
	}

	public void openAlbum()
	{
		Intent intent = new Intent(Intent.ACTION_PICK, null);
		intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		startActivityForResult(intent, 3023);
	}

	protected JSONObject postStream(String taskId, URL serverUrl, String filepath,
			ByteArrayInputStream fileStream, int bytesTotal, int degree)
	{
		String end = "\r\n";
		String Hyphens = "--";
		String boundary = UUID.randomUUID().toString();
		
		JSONObject rst = new JSONObject();		

		URLConnection conn = null;
		try
		{
			conn = (HttpURLConnection) serverUrl.openConnection();

			conn.setConnectTimeout(90);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			((HttpURLConnection) conn).setRequestMethod("POST");

			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");			
			conn.setRequestProperty("Cookie", String.valueOf(sessionCookie));
			conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
			
			if(this.mobile != null && this.passwordMD5 != null)
			{
				conn.setRequestProperty("Mobile", this.mobile);
				conn.setRequestProperty("PasswordMD5", this.passwordMD5);
			}
			
			/* 设置DataOutputStream */
			DataOutputStream ds = new DataOutputStream(conn.getOutputStream());
			
			//写入参数
			ds.writeBytes(Hyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; name=\"houseid\"" + end);
			ds.writeBytes(end);
			ds.writeBytes(houseinfo.getHouseId());
			ds.writeBytes(end);
			
			
			//写入参数
			ds.writeBytes(Hyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; name=\"degree\"" + end);
			ds.writeBytes(end);
			ds.writeBytes(String.valueOf(degree));
			ds.writeBytes(end);
			
			//写入文件数据
			File file = new File(filepath);
			ds.writeBytes(Hyphens + boundary + end);
			ds.writeBytes("Content-Disposition: form-data; name=\"uploadimage\";filename=\"" + file.getName() + "\"" + end);
			ds.writeBytes(end);

			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			int i = 0;
			int bytesReaded = 0;
			while ((length = fileStream.read(buffer)) != -1)
			{
				bytesReaded += length;
				/* 将资料写入DataOutputStream中 */
				ds.write(buffer, 0, length);
				//ds.flush();
				if (++i % 5 == 0)
				{
					Message msg = new Message();
					msg.what = 2;
					msg.obj = taskId;
					msg.arg1 = bytesReaded;
					msg.arg2 = bytesTotal;
					this.handler.sendMessage(msg);
				}
			}

			ds.writeBytes(end);
			ds.writeBytes(Hyphens + boundary + Hyphens + end);			
			fileStream.close();
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));			
			StringBuilder sb = new StringBuilder();
			
			do
			{
				String stringline = reader.readLine();	
				
				if(stringline == null)
					break;
				
				sb.append(stringline);
			}
			while(true);
						
			ds.flush();
			ds.close();
			
			JSONObject response = new JSONObject(sb.toString());
			return response;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			try
			{
				rst.put("errorCode", "-97");
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try
			{
				rst.put("errorMsg", ex.getMessage());
			}
			catch (JSONException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		return rst;
	}

	protected void createUpLoadImageTask(final Uri imageUri)
	{
		final String taskID = UUID.randomUUID().toString();
		final String imagePath = imageUri.toString();

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					ContentResolver resolver = EditHouse.this
							.getContentResolver();

					Bitmap bitmap = null;
					try
					{
						bitmap = MediaStore.Images.Media.getBitmap(resolver,
								imageUri);
					}
					catch (FileNotFoundException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					if (bitmap == null)
					{
						return;
					}

					// handle start message.
					Message startMsg = new Message();
					startMsg.what = 1;
					startMsg.obj = new String[] { imagePath, taskID };
					EditHouse.this.handler.sendMessage(startMsg);

					ByteArrayOutputStream outStream = new ByteArrayOutputStream();

					int degree = ImageProvider.readPictureDegree(imagePath);
					bitmap.compress(CompressFormat.JPEG, 60, outStream);
					bitmap.recycle();
					bitmap = null;

					ByteArrayInputStream reader = new ByteArrayInputStream(
							outStream.toByteArray());
					int bytesTotal = outStream.size();

					try
					{
						outStream.close();
						outStream = null;
					}
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					URL serverURL = null;

					try
					{
						serverURL = new URL(EditHouse.this.uploadServiceURL);
					}
					catch (Exception ex)
					{
						serverURL = null;
					}

					if (serverURL == null)
					{
						Message resultMessage = new Message();
						resultMessage.what = 4;
						resultMessage.obj = new String[] { taskID, "10",
								"服务地址错误" };
						EditHouse.this.handler.sendMessage(resultMessage);
						return;
					}

					try
					{
						JSONObject ret = EditHouse.this.postStream(taskID,
								serverURL, imagePath, reader, bytesTotal,
								degree);

						if (Integer.parseInt(String.valueOf(ret
								.get("errorCode"))) > 0)
						{
							Message resultMessage = new Message();
							resultMessage.what = 5;
							resultMessage.obj = new String[] { taskID,
									String.valueOf(ret.get("errorMsg")) };
							EditHouse.this.handler.sendMessage(resultMessage);
							return;
						}
						else
						{
							Message resultMessage = new Message();
							resultMessage.what = 4;
							resultMessage.obj = new String[] { taskID,
									String.valueOf(ret.get("errorCode")),
									String.valueOf(ret.get("errorMsg")) };
							EditHouse.this.handler.sendMessage(resultMessage);
							return;
						}
					}
					catch (Exception ex)
					{
						Message resultMessage = new Message();
						resultMessage.what = 4;
						resultMessage.obj = new String[] { taskID,
								String.valueOf(ex.hashCode()), ex.getMessage() };
						EditHouse.this.handler.sendMessage(resultMessage);
						return;
					}
				}
				finally
				{
					EditHouse.this.tasklistStatus.remove(taskID);
				}
			}
		}).start();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		// 拍照
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode)
		{
			case 3023:
			{
				this.createUpLoadImageTask(data.getData());
			}
		}
	}
}
