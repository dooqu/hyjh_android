package huayuanjiahe.com;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class CreateHouse extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		((HYJHApplication) this.getApplication()).regist(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FILL_PARENT,
		// WindowManager.LayoutParams.FILL_PARENT);
		setContentView(R.layout.activity_main);
		WebView webView = (WebView) this.findViewById(R.id.webView1);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new util.UtilityObject(this),
				"UtilityObject");
		webView.addJavascriptInterface(new util.ActivityManager(this),
				"ActivityManager");
		webView.loadUrl("file:///android_asset/createhouse.html");
	}
	
	@Override
	public void onDestroy()
	{
		((HYJHApplication)this.getApplication()).unregist(this);
		super.onDestroy();
	}
}
