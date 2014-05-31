package com.dsaiztc.dropboxtest.adapter;

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
		this.mFirstLine = firstLine;
		this.mSecondLine = secondLine;
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
	public void setIcon(int mIcon)
	{
		this.mIcon = mIcon;
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
}