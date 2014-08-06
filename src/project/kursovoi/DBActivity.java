package project.kursovoi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import project.kursovoi.R;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DBActivity extends Activity {
	
	DBConnector mDBConnector;
	Context mContext;
	ListView mListView;
	myListAdapter mAdapter;
	
	int ADD_ACTIVITY = 0;
	int UPDATE_ACTIVITY = 1;
	
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mContext = this;
        
        mDBConnector = new DBConnector (this);
        
        mListView = (ListView)findViewById(R.id.list);
        mAdapter = new myListAdapter(mContext, mDBConnector.selectAll());
        mListView.setAdapter(mAdapter);
        
        registerForContextMenu(mListView);
    }
    
    @Override  
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
    	super.onCreateContextMenu(menu, v, menuInfo);  
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.context_menu, menu);
    }
    
    @Override
	public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch(item.getItemId()) {
    	case R.id.edit:
    		Intent i = new Intent(mContext, NewActivity.class);
    		Data md = mDBConnector.select(info.id);
    		i.putExtra("MyData", md);
        	startActivityForResult (i, UPDATE_ACTIVITY);
        	updateList();
    		return true;
    	case R.id.delete:
    		mDBConnector.delete (info.id);
    		updateList();
    		return true;
    	default:
    		return super.onContextItemSelected(item);
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true; 
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.add:
	        	Intent i = new Intent(mContext, NewActivity.class);
            	startActivityForResult (i, ADD_ACTIVITY);
            	updateList();
	            return true;
            case R.id.exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
        if (resultCode == Activity.RESULT_OK) {
        	Data md = (Data) data.getExtras().getSerializable("MyData");
        	if (requestCode == UPDATE_ACTIVITY)
        		mDBConnector.update(md);
        	else
        		mDBConnector.insert(md);
        	updateList();
        }        
    }
    
    private void updateList () {
    	mAdapter.setArrayMyData(mDBConnector.selectAll());
    	mAdapter.notifyDataSetChanged();
    }
        
    class myListAdapter extends BaseAdapter {
    	private LayoutInflater mLayoutInflater;
    	private ArrayList<Data> arrayMyData;
    	    	
    	public myListAdapter (Context ctx, ArrayList<Data> arr) {
    		mLayoutInflater = LayoutInflater.from(ctx);
    		setArrayMyData(arr);
    	}
    	
		public ArrayList<Data> getArrayMyData() {
			return arrayMyData;
		}

		public void setArrayMyData(ArrayList<Data> arrayMyData) {
			this.arrayMyData = arrayMyData;
		}
    	
    	public int getCount () {
    		return arrayMyData.size();
    	}
    		
    	public Object getItem (int position) {
    		
    		return position;
    	}
    		
    	public long getItemId (int position) {
    		Data md = arrayMyData.get(position);
    		if (md != null) {
    			return md.getID();
    		}
    		return 0;
    	}
    		
    	public View getView(int position, View convertView, ViewGroup parent) { 
    		
    		if (convertView == null)
    			convertView = mLayoutInflater.inflate(R.layout.item, null);
    		
    		ImageView vIcon = (ImageView)convertView.findViewById(R.id.Icon);
    		TextView vTitle = (TextView)convertView.findViewById(R.id.Title);
    		TextView vDate = (TextView)convertView.findViewById(R.id.Date);
    		
    		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); 
    		
   			Data md = arrayMyData.get(position);    			
   			vDate.setText(dateFormat.format(md.getDate()));
   			vTitle.setText(md.getTitle());
   			vIcon.setImageResource(md.getIcon());
    			
    		return convertView;
    	}
    } 
}