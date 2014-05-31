package com.dsaiztc.dropboxtest;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dsaiztc.dropboxtest.adapter.ItemAdapter;
import com.dsaiztc.dropboxtest.adapter.ListItem;

public class MainActivity extends Activity
{
	private final String TAG = MainActivity.class.getSimpleName();
	
	private final static DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());

	private static final String appKey = "3hg2xk0qief0orz";
	private static final String appSecret = "xu2bl7v74crrj7h";

	private static final int REQUEST_LINK_TO_DBX = 0;

	private Activity mActivity;

	private DbxAccountManager mDbxAccountManager;
	private DbxAccount mDbxAccount;
	private DbxFileSystem mDbxFileSystem;
	private List<DbxFileInfo> mListDbxFileInfo;

	private ListView mListView;
	
	private List<ListItem> mItems = new ArrayList<ListItem>();

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(android.view.Window.FEATURE_INDETERMINATE_PROGRESS);

		mActivity = this;

		setContentView(R.layout.activity_main);

		mListView = (ListView) findViewById(R.id.listView1);
		mListView.setAdapter(new ItemAdapter(this, mItems));

		mDbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
		mListDbxFileInfo = new ArrayList<DbxFileInfo>();
	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (mDbxAccountManager.hasLinkedAccount())
		{
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
		ShowLibraryTask mShowLibraryTask = new ShowLibraryTask();
		mShowLibraryTask.execute();
	}

	private class ShowLibraryTask extends AsyncTask<Void, DbxPath, Void>
	{
		@Override
		protected void onPreExecute()
		{
			ActionBar ab = getActionBar();
		    ab.setTitle("Conectando...");
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Void doInBackground(Void... params)
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

			try
			{
				mDbxFileSystem.awaitFirstSync();
				getList(mDbxFileSystem.listFolder(DbxPath.ROOT));
			}
			catch (DbxException e)
			{
				Log.e(TAG, e.toString());
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			ActionBar ab = getActionBar();
		    ab.setTitle(R.string.app_name);
		    
			setProgressBarIndeterminateVisibility(false);
			
			updateUI();
		}
	}

	private void getList(List<DbxFileInfo> list)
	{
		for (DbxFileInfo dfi : list)
		{
			if (dfi.isFolder)
			{
				try
				{
					getList(mDbxFileSystem.listFolder(dfi.path));
				}
				catch (DbxException e)
				{
					Log.e(TAG, e.toString());
				}
			}
			else
			{
				String[] s = dfi.path.getName().split("\\.");
				if (s[s.length - 1].equalsIgnoreCase("epub"))
				{
					mListDbxFileInfo.add(dfi);
				}
			}
		}
	}
	
	private void updateUI() 
	{
		for (DbxFileInfo dfi : mListDbxFileInfo)
		{
			String[] s = dfi.path.getName().split("\\.");
			mItems.add(new ListItem(R.drawable.icon_book, s[0], getString(R.string.modified_date) + " " + DATE_FORMAT.format(dfi.modifiedTime)));
		}
		
		mListView.setAdapter(new ItemAdapter(this, mItems));
	}
}
