package com.media.mfcloud.images;

import com.media.mfcloud.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageActivity extends Activity {
	private ImageView image;
	private Bundle extras;
	private String image_url = "";
	public ImageLoader imageLoader; 
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image);
		extras = getIntent().getExtras();
		image_url = extras.getString("image_url");
		init_view();
	}
	
	
	public void init_view(){
		
		image = (ImageView)findViewById(R.id.SimpleImg);
		 imageLoader=new ImageLoader(this);
		 imageLoader.DisplayImage(image_url,R.drawable.picture, image);
		
	}
}
