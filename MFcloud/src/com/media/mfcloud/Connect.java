package com.media.mfcloud;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Connect extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_of_host);
        initView();
        
    }
    
    private void initView() {
    
    }
    
    
 
	public void buttonConnect_onClick(View v) {
		
    	
    		
    		//check host
    		
		Intent intent = new Intent(this, Authorization.class);
        startActivity(intent);
    	
    }
	
	

}
