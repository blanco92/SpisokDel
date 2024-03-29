package project.kursovoi;

import java.util.Date;

import project.kursovoi.R;


import android.app.Activity;  
import android.content.Context;  
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;  
//import android.util.Log;
import android.view.View;  
import android.view.ViewGroup;  
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;  
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Gallery;  
import android.widget.ImageView;  
import android.widget.TextView;

public class NewActivity extends Activity  {
	
	private Gallery mGallery;  
	private ImageAdapter mImageAdapter;
	private Button butSave, butCancel;
	private TextView mTextView;
	private DatePicker mDatePicker;
	Context mContext;
	private long MyDataID;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add);
        	
      
        mGallery = (Gallery) findViewById(R.id.gallery);
        mImageAdapter = new ImageAdapter(this);  
        mGallery.setAdapter(mImageAdapter);
        
        
        mTextView = (TextView) findViewById(R.id.DescText);
        mDatePicker = (DatePicker) findViewById(R.id.Date);
        
        if (getIntent().hasExtra("MyData")) {
        	Data md = (Data) getIntent().getSerializableExtra("MyData");
        	Date d = new Date (md.getDate());
        	mDatePicker.updateDate(d.getYear() + 1900, d.getMonth(), d.getDate());
        	mGallery.setSelection(mImageAdapter.getPositionbyResId(md.getIcon()));
        	mTextView.setText(md.getTitle());
        	MyDataID = md.getID();
        }
        else {
        	MyDataID = -1;
        	mGallery.setSelection(mImageAdapter.getCount() / 2);
        }
        
        butSave = (Button) findViewById(R.id.butSave);
        butCancel = (Button) findViewById(R.id.butCancel);
        
        butSave.setOnClickListener (new OnClickListener() {
            public void onClick(View v) {
            	
            	Date date = new Date(mDatePicker.getYear()-1900, mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
           		Data md = new Data (MyDataID, date.getTime(), mTextView.getText().toString(), mImageAdapter.getResourceId(mGallery.getSelectedItemPosition()));
            	Intent intent = getIntent();
            	intent.putExtra("MyData", md);
            	setResult(RESULT_OK, intent);
            	finish();
            }
         });

        butCancel.setOnClickListener (new OnClickListener() {
            public void onClick(View v) {
            	setResult(RESULT_CANCELED, new Intent());
            	finish();
            }
         });
    }
    
    public class ImageAdapter extends BaseAdapter {
        private Context mContext;
        int bg;

        private int[] mImageIds = {R.drawable.s01, R.drawable.s02, R.drawable.s03};  

        public ImageAdapter(Context c) {
            mContext = c;
            TypedArray attr = mContext.obtainStyledAttributes(R.styleable.MyGallery);
            bg = attr.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            attr.recycle();
        }

        public int getCount() {
            return mImageIds.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }
        
        public int getResourceId(int position) {
        	
        	int id = mImageIds[position];
            return id;
        }
        
        public int getPositionbyResId(int ResId) {
        	
        	for (int i = 0; i < mImageIds.length; i++)
        		if (mImageIds[i] == ResId)
        			return i;
            return 0;
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(mImageIds[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(2, 2, 2, 2);
            imageView.setBackgroundResource(bg);
            imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            return imageView;
        }
    }
}
