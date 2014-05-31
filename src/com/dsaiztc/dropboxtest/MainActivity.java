package com.dsaiztc.dropboxtest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFileSystem;

public class MainActivity extends Activity
{
	private final String TAG = MainActivity.class.getSimpleName();

	private static final String appKey = "3hg2xk0qief0orz";
	private static final String appSecret = "xu2bl7v74crrj7h";

	private static final int REQUEST_LINK_TO_DBX = 0;

	private DbxAccountManager mDbxAccountManager;
	private DbxAccount mDbxAccount;
	private DbxFileSystem mDbxFileSystem;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (mDbxAccountManager.hasLinkedAccount())
		{
			mDbxAccount = mDbxAccountManager.getLinkedAccount();
			try
			{
				mDbxFileSystem = DbxFileSystem.forAccount(mDbxAccount);
			}
			catch (Unauthorized e)
			{
				Log.e(TAG, e.toString());
			}
			showLibrary();
		}
		else
		{
			mDbxAccountManager.startLink((Activity) this, REQUEST_LINK_TO_DBX);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle presses on the action bar items
		switch (item.getItemId())
		{
			case R.id.action_disconnect:
				mDbxAccountManager.getLinkedAccount().unlink();
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == REQUEST_LINK_TO_DBX)
		{
			if (resultCode == Activity.RESULT_OK)
			{
				showLibrary();
			}
			else
			{
				Toast.makeText(this, "Link to Dropbox failed or was cancelled.", Toast.LENGTH_SHORT).show();
			}
		}
		else
		{
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	private void showLibrary()
	{
		// TODO
	}
}
