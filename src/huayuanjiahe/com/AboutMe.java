package huayuanjiahe.com;


import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import modal.HouseInfo;

public class AboutMe extends Activity 
{
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		
		((HYJHApplication)this.getApplication()).regist(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		WebView webView = (WebView)this.findViewById(R.id.webView1);

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);		
		
		webView.addJavascriptInterface(new util.UtilityObject(this), "UtilityObject");
		webView.addJavascriptInterface(new util.ActivityManager(this), "ActivityManager");
		
		webView.loadUrl("file:///android_asset/aboutme.html");
	}
	
	@Override
	public void onDestroy()
	{
		((HYJHApplication)this.getApplication()).unregist(this);
		super.onDestroy();
	}
}
