package huayuanjiahe.com;

import java.io.IOException;

import modal.HouseInfo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class EditCustom extends Activity
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

		webView.addJavascriptInterface(new util.UtilityObject(this), "UtilityObject");
		webView.addJavascriptInterface(new util.ActivityManager(this), "ActivityManager");		
		webView.addJavascriptInterface(houseinfo, "HouseInfo");
		
		webView.loadUrl("file:///android_asset/editcustom.html");
	}
	
	@Override
	public void onDestroy()
	{
		((HYJHApplication)this.getApplication()).unregist(this);
		super.onDestroy();
	}
}
