package com.examples;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class DCalendar extends Activity implements OnClickListener
	{
		private static final String tag = "MCalendar";
	    private static final int MENU_UINDEX = Menu.FIRST  ;
		private static final int MENU_DINDEX = Menu.FIRST +1 ;

		private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

		//private Button selectedDayMonthYearButton;
		private Button currentMonth;
		private Button Setting;

		private ImageView prevMonth;
		private ImageView nextMonth;
		//private GridView calendarView;
		private Calendar _calendar;

		private EditText tcalendar;
		private int month, year, day;
		
		private final DateFormat dateFormatter = new DateFormat();
		private static final String dateTemplate = "MMMMdd yyyy";

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState)
		{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.day_view);

				_calendar = Calendar.getInstance(Locale.getDefault());
				month = _calendar.get(Calendar.MONTH) + 1;
				year = _calendar.get(Calendar.YEAR);
				day = _calendar.get(Calendar.DATE);
				
				
				Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

				//selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
				//selectedDayMonthYearButton.setText("Selected: ");

				prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
				prevMonth.setOnClickListener(this);
				
				tcalendar = (EditText) this.findViewById(R.id.tcalendar);

				currentMonth = (Button) this.findViewById(R.id.currentMonth);
				currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

				nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
				nextMonth.setOnClickListener(this);

				Setting = (Button) this.findViewById(R.id.Setting);
				
				Setting.setOnClickListener(new Button.OnClickListener()
		        {
		         public void onClick(View v)
		         {
		        	  DCalendar.this.finish();
			         Intent intent = new Intent();
			         intent.setClass(DCalendar.this, configure.class);
	
			         startActivity(intent);
		         }
		        });				
				
				//calendarView = (GridView) this.findViewById(R.id.calendar);

				// Initialised
				//adapter = new WeekHandler(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
				//adapter.getWeekList(0);
				//adapter.notifyDataSetChanged();
				//calendarView.setAdapter(adapter);
		}

		  public boolean onCreateOptionsMenu(Menu menu)
		  {
		    super.onCreateOptionsMenu(menu);
		    
		    menu.add(0 , MENU_UINDEX, 0 , "")
		    .setAlphabeticShortcut('S');
		    menu.add(0 , MENU_DINDEX, 1 , "")
		    .setAlphabeticShortcut('E');
		  return true;  
		  }
		  
		  
		  @Override
		  public boolean onOptionsItemSelected(MenuItem item)
		  {
		    
		    switch (item.getItemId())
		      { 
		          case MENU_UINDEX:
		             return true;
		      
		          case MENU_DINDEX:
		             break ;
		      }
		    
		  return true ;
		  }
		
		/**
		 * 
		 * @param month
		 * @param year
		 */
		private void setGridCellAdapterToDate(int year, int month, int day)
		{
				//adapter = new WeekHandler(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
				_calendar.set(year, month - 1, day);
				currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
				//adapter.getWeekList(0);
				//adapter.notifyDataSetChanged();
				//calendarView.setAdapter(adapter);
		}

		@Override
		public void onClick(View v)
		{
			Log.d(tag, "Month: " + month + "/" + Integer.toString(day) + " " + "Year: " + year);
				if (v == prevMonth)
				{		
					if (day <= 1)
					{
						if (month <= 1)
						{
							month = 12;
							year--;
						}
						else
						{
							month--;
						}
						
						day = daysOfMonth[month-1];
					}
					else
					{
						day--;
					}
					
					Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
				}
				
				if (v == nextMonth)
				{
					if (day >= daysOfMonth[month-1])
					{
						if (month > 11)
						{
								month = 1;
								year++;
						}
						else
						{
								month++;
						}
						day = 1;
					}
					else
					{
						day++;
					}
					
					Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
				}

				setGridCellAdapterToDate(year, month, day);
		}

		@Override
		public void onDestroy()
		{
				Log.d(tag, "Destroying View ...");
				super.onDestroy();
		}
		
		//show message
		  public void openOptionsDialog(String info)
		  {
		    new AlertDialog.Builder(this)
		    .setTitle("message")
		    .setMessage(info)
		    .setPositiveButton("OK",
		        new DialogInterface.OnClickListener()
		        {
		         public void onClick(DialogInterface dialoginterface, int i)
		         {
		         }
		         }
		        )
		    .show();
		  }		

	}
