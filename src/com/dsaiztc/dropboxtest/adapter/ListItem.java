package com.dsaiztc.dropboxtest.adapter;

import android.graphics.drawable.Drawable;

/**
 * Class that adapts data to a basic ListView with icon, text1 and text2
 * 
 * @author Daniel Saiz Llarena
 * 
 */
public class ListItem
{
	/**
	 * Icon showed at left
	 */
	private int mIcon;
	
	private Drawable mIconDrawable;
	/**
	 * First line text
	 */
	private String mFirstLine;
	/**
	 * Second line text
	 */
	private String mSecondLine;

	/**
	 * Constructor
	 * 
	 * @param icon Icon showed at left resource ID
	 * @param firstLine First line text
	 * @param secondLine Second line text
	 */
	public ListItem(int icon, String firstLine, String secondLine)
	{
		this.mIcon = icon;
		this.mIconDrawable = null;
		this.mFirstLine = firstLine;
		this.mSecondLine = secondLine;
	}
	
	public ListItem(Drawable iconDrawable, String firstLine, String secondLine)
	{
		this.mIcon = -1;
		this.mIconDrawable = iconDrawable;
		this.mFirstLine = firstLine;
		this.mSecondLine = secondLine;
	}
	
	public ListItem()
	{
		this.mIcon = -1;
		this.mIconDrawable = null;
		this.mFirstLine = null;
		this.mSecondLine = null;
	}

	/**
	 * Get icon showed at left resource ID
	 * 
	 * @return Icon showed at left resource ID
	 */
	public int getIcon()
	{
		return mIcon;
	}

	/**
	 * Set icon showed at left resource ID
	 * 
	 * @param mIcon Icon showed at left resource ID
	 */
	public void setIcon(int icon)
	{
		this.mIcon = icon;
	}

	/**
	 * Get first line text
	 * 
	 * @return First line text
	 */
	public String getFirstLine()
	{
		return mFirstLine;
	}

	/**
	 * Set first line text
	 * 
	 * @param firstLine First line text
	 */
	public void setFirstLine(String firstLine)
	{
		this.mFirstLine = firstLine;
	}

	/**
	 * Get second line text
	 * 
	 * @return Second line text
	 */
	public String getSecondLine()
	{
		return mSecondLine;
	}

	/**
	 * Set second line text
	 * 
	 * @param secondLine Second line text
	 */
	public void setSecondLine(String secondLine)
	{
		this.mSecondLine = secondLine;
	}

	public Drawable getIconDrawable()
	{
		return mIconDrawable;
	}

	public void setIconDrawable(Drawable iconDrawable)
	{
		this.mIconDrawable = iconDrawable;
	}
}