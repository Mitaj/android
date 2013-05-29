package com.media.mfcloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Authorization extends Activity {
	
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.authorization);
	        initView();
	    }
	    
	    private void initView() {
	    	
	    	
	    }
	    
	    
	   
		public void buttonLogin_onClick(View v) {
	    	if(v.getId() == R.id.button_login){
	    		Intent intent = new Intent(this, MainActivity.class);
		        startActivity(intent);
	    	}
	    }


}
