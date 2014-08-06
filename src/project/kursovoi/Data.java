package project.kursovoi;

import java.io.Serializable;


public class Data implements Serializable{
	
	private long id;
	private long date;
	private String title;
	private int icon;
	
	public Data (long id, long date, String title, int icon) {
		this.id = id;
		this.date = date;
		this.title = title;
		this.icon = icon;
	}
	
	public long getID () {return id;}
	public long getDate () {return date;}
	public String getTitle () {return title;}
	public int getIcon () {return icon;}
}