package com.examples;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Contacts.People;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class configure extends ExpandableListActivity
{
	private static final int MSG_MONTH = 1;  
	private static final int MSG_WEEK = 2;  
	private static final int MSG_DAY = 3;  
	
	
	private ExpandableListAdapter adapter;
	private String TAG = "configure";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		adapter = new MyExpandableListAdapter();
		setListAdapter(adapter);
		registerForContextMenu(getExpandableListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) 
	{
		menu.setHeaderTitle("calendar configure");
		menu.add(0, 0, 0, "Action");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		boolean flag=false;
		
		// TODO Auto-generated method stub
		ExpandableListContextMenuInfo menuInfo=(ExpandableListContextMenuInfo)item.getMenuInfo();
		String title=((TextView)menuInfo.targetView).getText().toString();
		int type=ExpandableListView.getPackedPositionType(menuInfo.packedPosition);
		
		if (type==ExpandableListView.PACKED_POSITION_TYPE_CHILD) 
		{
			int	groupPos =ExpandableListView.getPackedPositionGroup(menuInfo.packedPosition);
			int	childPos =ExpandableListView.getPackedPositionChild(menuInfo.packedPosition);
			
			CharSequence str=""+title;
			Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
			Log.i("tag", "Run Hereing...");
			
			flag= true;
		}
		
		else
			 if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
	            int groupPos = ExpandableListView.getPackedPositionGroup(menuInfo.packedPosition); 
	            CharSequence cs=""+title;
	            Toast.makeText(this, cs, Toast.LENGTH_SHORT).show();
	            Log.i(TAG, "Run Here...");
	            flag= true;
	        }
		return flag;
	}
	
	 public class MyExpandableListAdapter extends BaseExpandableListAdapter 
	 {
	        public String[] groups = { "¼Ò¦¡" };
	        public String[][] children = {
	                { "¤ë", "¶g", "¤é"},
	        };
	        
	        public Object getChild(int groupPosition, int childPosition) {
	            return children[groupPosition][childPosition];
	        }

	        public long getChildId(int groupPosition, int childPosition)
	        {
	            return childPosition;
	        }

	        public int getChildrenCount(int groupPosition) {
	            return children[groupPosition].length;
	        }

	        public TextView getGenericView() {
	            // Layout parameters for the ExpandableListView
	            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
	                    ViewGroup.LayoutParams.MATCH_PARENT, 64);

	            TextView textView = new TextView(configure.this);
	            textView.setLayoutParams(lp);
	            // Center the text vertically
	            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
	            // Set the text starting position
	            textView.setPadding(36, 0, 0, 0);
	            return textView;
	        }
	        
	        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
	                View convertView, ViewGroup parent) {
	            TextView textView = getGenericView();
	            textView.setText(getChild(groupPosition, childPosition).toString());
	            return textView;
	        }

	        public Object getGroup(int groupPosition) {
	            return groups[groupPosition];
	        }

	        public int getGroupCount() {
	            return groups.length;
	        }

	        public long getGroupId(int groupPosition) {
	            return groupPosition;
	        }

	        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
	                ViewGroup parent) {
	            TextView textView = getGenericView();
	            textView.setText(getGroup(groupPosition).toString());
	            return textView;
	        }

	        public boolean isChildSelectable(int groupPosition, int childPosition) 
	        {
	            Log.i(TAG, Integer.toString(childPosition));
	            Message msg = null;
	            
	            switch (childPosition)
	            {
	            case 0:
	                  msg = new Message();
	                  msg.what = MSG_MONTH;
	                  myHandler.sendMessage(msg); 	            	
	            		break;
	            case 1:
	                  msg = new Message();
	                  msg.what = MSG_WEEK;
	                  myHandler.sendMessage(msg); 	            	
	            		break;            		
	            case 2:
	                  msg = new Message();
	                  msg.what = MSG_DAY;
	                  myHandler.sendMessage(msg); 	            	
            			break;
	            		
	            }

	            return true;
	        }

	        public boolean hasStableIds() {
	            return true;
	        }

	    }
	 
	  public Handler myHandler = new Handler(){
		    public void handleMessage(Message msg)
		    {
		    	Intent intent = null;
		    	configure.this.finish();
		    	
		        switch(msg.what)
		        {
		          case MSG_MONTH:
				         intent = new Intent();
				         intent.setClass(configure.this, CCalendar.class);
		
				         startActivity(intent);
		        	  
		                break;
		          case MSG_WEEK:
				         intent = new Intent();
				         intent.setClass(configure.this, MCalendar.class);
		
				         startActivity(intent);
		                break;
		          case MSG_DAY:
				         intent = new Intent();
				         intent.setClass(configure.this, DCalendar.class);
		
				         startActivity(intent);
		                break;
		        }
		        super.handleMessage(msg);
		    }
		};  


}