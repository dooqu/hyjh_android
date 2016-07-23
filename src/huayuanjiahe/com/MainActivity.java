package huayuanjiahe.com;

import java.io.IOException;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

public class MainActivity extends Activity
{
	protected long lastBackKeyDownTime;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		((HYJHApplication) this.getApplication()).regist(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FILL_PARENT,
		// WindowManager.LayoutParams.FILL_PARENT);
		setContentView(R.layout.activity_main);
		this.lastBackKeyDownTime = System.currentTimeMillis() - 5000;
		WebView webView = (WebView) this.findViewById(R.id.webView1);
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new util.UtilityObject(this),
				"UtilityObject");
		webView.addJavascriptInterface(new util.ActivityManager(this),
				"ActivityManager");
		webView.loadUrl("file:///android_asset/login.html");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
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
				((HYJHApplication) this.getApplication()).closeAll();
				System.exit(0);
			}
			this.lastBackKeyDownTime = System.currentTimeMillis();
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onDestroy()
	{
		((HYJHApplication) this.getApplication()).unregist(this);
		super.onDestroy();
	}
}
