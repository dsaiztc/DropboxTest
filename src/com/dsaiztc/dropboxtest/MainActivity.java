package com.dsaiztc.dropboxtest;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;
import com.dsaiztc.dropboxtest.adapter.ItemAdapter;
import com.dsaiztc.dropboxtest.adapter.ListItem;
import com.dsaiztc.dropboxtest.model.Ebook;

public class MainActivity extends Activity
{
	private final String TAG = MainActivity.class.getSimpleName();

	private final static DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());

	private static final String appKey = "ma8eek6b9qmw2yq";
	private static final String appSecret = "7ueeyx17dy7mfnl";

	private static final int REQUEST_LINK_TO_DBX = 0;

	private DbxAccountManager mDbxAccountManager;
	private DbxAccount mDbxAccount;
	private DbxFileSystem mDbxFileSystem;
	private List<Ebook> mListEbook;

	private ListView mListView;

	private List<ListItem> mItems;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(android.view.Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_main);

		mListView = (ListView) findViewById(R.id.listView1);

		mDbxAccountManager = DbxAccountManager.getInstance(getApplicationContext(), appKey, appSecret);
		mListEbook = new ArrayList<Ebook>();

		mItems = new ArrayList<ListItem>();
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
			case R.id.action_order_by_date:
				orderByDate();
				updateUI();
				return true;
			case R.id.action_order_by_name:
				orderByName();
				updateUI();
				return true;
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
			ab.setTitle("Cargando...");
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
				if (!mDbxFileSystem.hasSynced())
				{
					mDbxFileSystem.awaitFirstSync();
				}

				mListEbook = new ArrayList<Ebook>();
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

			orderByName();
			updateUI();

			setProgressBarIndeterminateVisibility(false);
		}
	}

	/**
	 * Get the list of all DbxFileInfo with epub extension and saves it to local list
	 * 
	 * @param list List of DbxFileInfo on Root Dropbox folder
	 */
	private void getList(List<DbxFileInfo> list)
	{
		Ebook ebook = new Ebook();

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
					ebook.setDbxFileInfo(dfi);
				}

				if (dfi.path.getName().equalsIgnoreCase("cover.jpg"))
				{
					try
					{
						DbxFile mDbxFile = mDbxFileSystem.open(dfi.path);
						Drawable cover = Drawable.createFromStream(mDbxFile.getReadStream(), "cover");
						mDbxFile.close();
						ebook.setDrawable(cover);
					}
					catch (DbxException e)
					{
						Log.e(TAG, e.toString());
					}
					catch (IOException e)
					{
						Log.e(TAG, e.toString());
					}
				}

				if (dfi.path.getName().equalsIgnoreCase("metadata.opf"))
				{
					try
					{
						DbxFile mDbxFile = mDbxFileSystem.open(dfi.path);

						Document dom;
						DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
						DocumentBuilder db = dbf.newDocumentBuilder();
						dom = db.parse(mDbxFile.getReadStream());
						mDbxFile.close();

						Element doc = dom.getDocumentElement();

						String title = getTextValue(doc, "dc:title");
						String author = getTextValue(doc, "dc:creator");

						ebook.setTitle(title);
						ebook.setAuthor(author);
					}
					catch (Exception e)
					{
						Log.e(TAG, e.toString());
					}
				}
			}
		}

		if (ebook.getDbxFileInfo() != null)
		{
			mListEbook.add(ebook);
			Log.d(TAG, ebook.getDbxFileInfo().path.getName());
			Log.d(TAG, "Título: " + ebook.getTitle() + "\nAutor: " + ebook.getAuthor());
		}
	}

	/**
	 * Read the local list of Ebook and updates the UI (updates the ListView)
	 */
	private void updateUI()
	{
		mItems = new ArrayList<ListItem>();

		for (Ebook ebook : mListEbook)
		{
			// TODO Añadir los elementos de metadata
			ListItem item = new ListItem();

			if (ebook.getDrawable() != null)
			{
				item.setIconDrawable(ebook.getDrawable());
			}
			else
			{
				item.setIcon(R.drawable.icon_book);
			}

			if (ebook.getTitle() != null)
			{
				item.setFirstLine(ebook.getTitle());
			}
			else
			{
				item.setFirstLine(ebook.getDbxFileInfo().path.getName().split("\\.")[0]);
			}

			if (ebook.getAuthor() != null)
			{
				item.setSecondLine("Autor: " + ebook.getAuthor());
			}
			else
			{
				item.setSecondLine("Autor: No disponible");
			}

			mItems.add(item);
		}

		mListView.setAdapter(new ItemAdapter(this, mItems));
	}

	/**
	 * Sort the local list of Ebook by date
	 */
	private void orderByDate()
	{
		Collections.sort(mListEbook, new Comparator<Ebook>()
		{
			public int compare(Ebook o1, Ebook o2)
			{
				if (o1.getDbxFileInfo().modifiedTime == null || o2.getDbxFileInfo().modifiedTime == null) return 0;
				return o1.getDbxFileInfo().modifiedTime.compareTo(o2.getDbxFileInfo().modifiedTime);
			}
		});
	}

	/**
	 * Sort the local list of Ebook by name
	 */
	private void orderByName()
	{
		Collections.sort(mListEbook, new Comparator<Ebook>()
		{
			public int compare(Ebook o1, Ebook o2)
			{
				if (o1.getDbxFileInfo().path.getName() == null || o2.getDbxFileInfo().path.getName() == null)
				{
					return 0;
				}
				if (o1.getTitle() == null && o2.getTitle() != null)
				{
					return o1.getDbxFileInfo().path.getName().compareTo(o2.getTitle());
				}
				if (o1.getTitle() != null && o2.getTitle() == null)
				{
					return o1.getTitle().compareTo(o2.getDbxFileInfo().path.getName());
				}
				if (o1.getTitle() != null && o2.getTitle() != null)
				{
					return o1.getTitle().compareTo(o2.getTitle());
				}
				return o1.getDbxFileInfo().path.getName().compareTo(o2.getDbxFileInfo().path.getName());
			}
		});
	}

	private String getTextValue(Element doc, String tag)
	{
		String value = null;
		NodeList nl;
		nl = doc.getElementsByTagName(tag);
		if (nl.getLength() > 0 && nl.item(0).hasChildNodes())
		{
			value = nl.item(0).getFirstChild().getNodeValue();
		}
		return value;
	}
}
