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
import android.content.Context;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MCalendar extends Activity implements OnClickListener
	{
		private static final String tag = "MCalendar";
	    private static final int MENU_UINDEX = Menu.FIRST  ;
		private static final int MENU_DINDEX = Menu.FIRST +1 ;

		private ListView mCal;
		//private Button selectedDayMonthYearButton;
		private Button currentMonth;
		private ImageView prevMonth;
		private ImageView nextMonth;
		//private GridView calendarView;
		private WeekHandler adapter;
		private Calendar _calendar;
		private int month, year;
		private int weekofnumber;
		private int nindex;
		private int maxindex;
		
		private final DateFormat dateFormatter = new DateFormat();
		private static final String dateTemplate = "MMMM yyyy";

		/** Called when the activity is first created. */
		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
				setContentView(R.layout.month_view);

				_calendar = Calendar.getInstance(Locale.getDefault());
				month = _calendar.get(Calendar.MONTH) + 1;
				year = _calendar.get(Calendar.YEAR);
				
				Log.d(tag, "Calendar Instance:= " + "Month: " + month + " " + "Year: " + year);

				nindex = 0;
				maxindex=0;
				//selectedDayMonthYearButton = (Button) this.findViewById(R.id.selectedDayMonthYear);
				//selectedDayMonthYearButton.setText("Selected: ");

				prevMonth = (ImageView) this.findViewById(R.id.prevMonth);
				prevMonth.setOnClickListener(this);
				
				mCal = (ListView) this.findViewById(R.id.lcalendar);

				currentMonth = (Button) this.findViewById(R.id.currentMonth);
				currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));

				nextMonth = (ImageView) this.findViewById(R.id.nextMonth);
				nextMonth.setOnClickListener(this);

				//calendarView = (GridView) this.findViewById(R.id.calendar);

				// Initialised
				adapter = new WeekHandler(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
				adapter.getWeekList(0);
				//adapter.notifyDataSetChanged();
				//calendarView.setAdapter(adapter);
		}

		  public boolean onCreateOptionsMenu(Menu menu)
		  {
		    super.onCreateOptionsMenu(menu);
		    
		    menu.add(0 , MENU_UINDEX, 0 , "上一頁")
		    .setAlphabeticShortcut('S');
		    menu.add(0 , MENU_DINDEX, 1 , "下一頁")
		    .setAlphabeticShortcut('E');
		  return true;  
		  }
		  
		  
		  @Override
		  public boolean onOptionsItemSelected(MenuItem item)
		  {
		    
		    switch (item.getItemId())
		      { 
		          case MENU_UINDEX:
		        	 if (nindex-7 >= 0)
		        		 nindex-=7;
		        	 
		        	 mCal.setAdapter(null);
					 adapter.getWeekList(nindex);
		             return true;
		      
		          case MENU_DINDEX:
		        	  if (nindex+7 < maxindex)
			        		 nindex+=7;		    
		        	 mCal.setAdapter(null);
					 adapter.getWeekList(nindex);
		             break ;
		      }
		    
		  return true ;
		  }
		
		
		
		
		/**
		 * 
		 * @param month
		 * @param year
		 */
		private void setGridCellAdapterToDate(int month, int year)
		{
				adapter = new WeekHandler(getApplicationContext(), R.id.calendar_day_gridcell, month, year);
				_calendar.set(year, month - 1, _calendar.get(Calendar.DAY_OF_MONTH));
				currentMonth.setText(dateFormatter.format(dateTemplate, _calendar.getTime()));
				adapter.getWeekList(0);
				//adapter.notifyDataSetChanged();
				//calendarView.setAdapter(adapter);
		}

		@Override
		public void onClick(View v)
		{
			
				if (v == prevMonth)
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
						Log.d(tag, "Setting Prev Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}
				if (v == nextMonth)
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
						Log.d(tag, "Setting Next Month in GridCellAdapter: " + "Month: " + month + " Year: " + year);
						setGridCellAdapterToDate(month, year);
					}

		}

		@Override
		public void onDestroy()
		{
				Log.d(tag, "Destroying View ...");
				super.onDestroy();
		}

		public class WeekHandler implements OnClickListener
		{
				private static final String tag = "WeekHandler";
				private final Context _context;

				private final List<String> list;
				private static final int DAY_OFFSET = 1;
				private final String[] weekdays = new String[]{"日", "一", "二", "三", "四", "五", "六"};
				private final String[] months = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
				private final int[] daysOfMonth = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
				private final int month, year;
				private int daysInMonth, prevMonthDays;
				private int currentDayOfMonth;
				private int currentWeekDay;
				private Button gridcell;
				private TextView num_events_per_day;
				private final HashMap eventsPerMonthMap;
				private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");

				// Days in Current Month
				public WeekHandler(Context context, int textViewResourceId, int month, int year)
				{
						super();
						this._context = context;
						this.list = new ArrayList<String>();
						this.month = month;
						this.year = year;

						Log.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
						Calendar calendar = Calendar.getInstance();
						setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
						setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
						Log.d(tag, "New Calendar:= " + calendar.getTime().toString());
						Log.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
						Log.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

						// Print Month
						printMonth(month, year);

						// Find Number of Events
						eventsPerMonthMap = findNumberOfEventsPerMonth(year, month);
				}
				
				
				private String getMonthAsString(int i)
				{
						return months[i];
				}

				private String getWeekDayAsString(int i)
				{
						return weekdays[i];
				}

				private int getNumberOfDaysOfMonth(int i)
				{
						return daysOfMonth[i];
				}

				public String getItem(int position)
				{
						return list.get(position);
				}

				/**
				 * Prints Month
				 * 
				 * @param mm
				 * @param yy
				 */
				private void printMonth(int mm, int yy)
				{
						Log.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
						// The number of days to leave blank at
						// the start of this month.
						int trailingSpaces = 0;
						int leadSpaces = 0;
						int daysInPrevMonth = 0;
						int prevMonth = 0;
						int prevYear = 0;
						int nextMonth = 0;
						int nextYear = 0;
						int currentMonth = mm - 1;
						
						String currentMonthName = getMonthAsString(currentMonth);
						daysInMonth = getNumberOfDaysOfMonth(currentMonth);

						Log.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

						GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
						Log.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

						if (currentMonth == 11)
						{
								prevMonth = currentMonth - 1;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								nextMonth = 0;
								prevYear = yy;
								nextYear = yy + 1;
								Log.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
						}
						else if (currentMonth == 0)
						{
								prevMonth = 11;
								prevYear = yy - 1;
								nextYear = yy;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								nextMonth = 1;
								Log.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
						}
						else
						{
								prevMonth = currentMonth - 1;
								nextMonth = currentMonth + 1;
								nextYear = yy;
								prevYear = yy;
								daysInPrevMonth = getNumberOfDaysOfMonth(prevMonth);
								Log.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
						}

						// Compute how much to leave before before the first day of the
						// month.
						// getDay() returns 0 for Sunday.
						int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
						trailingSpaces = currentWeekDay;

						Log.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
						Log.d(tag, "No. Trailing space to Add: " + trailingSpaces);
						Log.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

						if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1)
						{
								++daysInMonth;
						}

						// Trailing Month days
						for (int i = 0; i < trailingSpaces; i++)
						{
								Log.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
								list.add(String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i) + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
						}

						// Current Month Days
						for (int i = 1; i <= daysInMonth; i++)
						{
								Log.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
								
								if (i == getCurrentDayOfMonth())
								{
										list.add(String.valueOf(i) + "-" + getMonthAsString(currentMonth) + "-" + yy);
								}
								else
								{
										list.add(String.valueOf(i) + "-" + getMonthAsString(currentMonth) + "-" + yy);
								}
						}

						// Leading Month days
						for (int i = 0; i < list.size() % 7; i++)
						{
								Log.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
								list.add(String.valueOf(i + 1) + "-" + getMonthAsString(nextMonth) + "-" + nextYear);
						}
						
						weekofnumber = list.size();
						maxindex = weekofnumber;
					}

				/**
				 * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
				 * ALL entries from a SQLite database for that month. Iterate over the
				 * List of All entries, and get the dateCreated, which is converted into
				 * day.
				 * 
				 * @param year
				 * @param month
				 * @return
				 */
				private HashMap findNumberOfEventsPerMonth(int year, int month)
				{
						HashMap map = new HashMap<String, Integer>();
						// DateFormat dateFormatter2 = new DateFormat();
						//						
						// String day = dateFormatter2.format("dd", dateCreated).toString();
						//
						// if (map.containsKey(day))
						// {
						// Integer val = (Integer) map.get(day) + 1;
						// map.put(day, val);
						// }
						// else
						// {
						// map.put(day, 1);
						// }
						return map;
				}

				public int getWeekofNumber()
				{
						return weekofnumber;
				}

				public void getWeekList(int weekofmonth)
				{
					Log.d(tag, "Current Day: " + getCurrentDayOfMonth());

			        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();  
			        for(int i=weekofmonth, j=0 ;i<7+weekofmonth; i++, j++)  
			        {  
						String[] day_color = list.get(i).split("-");
						String theday = day_color[0];
						String themonth = day_color[1];
						String theyear = day_color[2];
						
						if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null))
						{
								if (eventsPerMonthMap.containsKey(theday))
									{
										//num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
										Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
										//num_events_per_day.setText(numEvents.toString());
									}
						}

						Log.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);
						
			            HashMap<String, Object> map = new HashMap<String, Object>();  
			            map.put("ItemTitle", themonth + "/" + theday);  
			            map.put("ItemText", weekdays[j]);  
			            listItem.add(map);  
			        }  
				        
			        SimpleAdapter listItemAdapter = new SimpleAdapter(MCalendar.this, listItem,
				            R.layout.list_items,  
				            new String[] {"ItemTitle", "ItemText"},   
				            new int[] {R.id.ItemTitle, R.id.ItemText});  
				         
			        mCal.setAdapter(listItemAdapter);  				
				}
				
				@Override
				public void onClick(View view)
				{
						String date_month_year = (String) view.getTag();
						//selectedDayMonthYearButton.setText("Selected: " + date_month_year);

						try
							{
								Date parsedDate = dateFormatter.parse(date_month_year);
								Log.d(tag, "Parsed Date: " + parsedDate.toString());

							}
						catch (ParseException e)
							{
								e.printStackTrace();
							}
				}

				public int getCurrentDayOfMonth()
				{
						return currentDayOfMonth;
				}

				private void setCurrentDayOfMonth(int currentDayOfMonth)
				{
						this.currentDayOfMonth = currentDayOfMonth;
				}
				
				public void setCurrentWeekDay(int currentWeekDay)
				{
						this.currentWeekDay = currentWeekDay;
				}
				
				public int getCurrentWeekDay()
				{
						return currentWeekDay;
				}
			}
	}
