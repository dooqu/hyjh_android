package huayuanjiahe.com;

import java.io.IOException;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MenuActivity extends Activity
{
	protected long lastBackKeyDownTime;
	protected boolean backKeyDownOnce;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		((HYJHApplication) this.getApplication()).regist(this);
		lastBackKeyDownTime = System.currentTimeMillis() - 5000;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		WebView webView = (WebView) this.findViewById(R.id.webView1);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);

		try
		{
			webView.addJavascriptInterface(
					new util.ImageProvider(this.getApplicationContext(),
							webView), "ImageProvider");
			webView.addJavascriptInterface(new util.ActivityManager(this),
					"ActivityManager");
			
			webView.addJavascriptInterface(new util.UtilityObject(this),
					"UtilityObject");
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		webView.loadUrl("file:///android_asset/houselist.html");
	}

	@Override
	public void onDestroy()
	{
		((HYJHApplication) this.getApplication()).unregist(this);
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			if ((System.currentTimeMillis() - this.lastBackKeyDownTime) > 3000)
			{
				Toast.makeText(this, "再次按动返回键退出应用", 3000).show();
			}
			else
			{
				System.exit(0);
			}
			this.lastBackKeyDownTime = System.currentTimeMillis();
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}
}
