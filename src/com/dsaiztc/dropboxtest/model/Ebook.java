package com.dsaiztc.dropboxtest.model;

import android.graphics.drawable.Drawable;

import com.dropbox.sync.android.DbxFileInfo;

public class Ebook
{
	private DbxFileInfo mDbxFileInfo;
	private Drawable mDrawable;
	private String title;
	private String author;

	public Ebook()
	{
		mDbxFileInfo = null;
		mDrawable = null;
		title = null;
		author = null;
	}

	public DbxFileInfo getDbxFileInfo()
	{
		return mDbxFileInfo;
	}

	public void setDbxFileInfo(DbxFileInfo dbxFileInfo)
	{
		this.mDbxFileInfo = dbxFileInfo;
	}

	public Drawable getDrawable()
	{
		return mDrawable;
	}

	public void setDrawable(Drawable drawable)
	{
		this.mDrawable = drawable;
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getAuthor()
	{
		return author;
	}

	public void setAuthor(String author)
	{
		this.author = author;
	}
}
