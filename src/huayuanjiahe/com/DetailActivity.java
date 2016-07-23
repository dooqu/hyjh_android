package huayuanjiahe.com;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import modal.HouseInfo;

public class DetailActivity extends Activity 
{
	HouseInfo houseinfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		((HYJHApplication)this.getApplication()).regist(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		Intent intent = getIntent();
		String houseId = intent.getStringExtra("house_id");
		houseinfo = new HouseInfo();
		houseinfo.setHouseId(houseId);
		
		WebView webView = (WebView)this.findViewById(R.id.webView1);

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);		

		try
		{
			webView.addJavascriptInterface(new util.UtilityObject(this), "UtilityObject");
			webView.addJavascriptInterface(new util.ImageProvider(this.getApplicationContext(), webView), "ImageProvider");
			webView.addJavascriptInterface(new util.ActivityManager(this), "ActivityManager");		
			webView.addJavascriptInterface(webView, "WebView");
			webView.addJavascriptInterface(houseinfo, "HouseInfo");
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		
		webView.loadUrl("file:///android_asset/houseinfo.html");
	}
	
	@Override
	public void onDestroy()
	{
		((HYJHApplication)this.getApplication()).unregist(this);
		super.onDestroy();
	}
}
