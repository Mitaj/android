package com.media.mfcloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class Connect extends Activity {
	
	private EditText cServerAddress;
	private EditText cServerPort;
	protected ClientWork main_client= null;
	private String address_server = null;
	private Integer port_server = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_of_host);
        initView(); 
    }
    
    private void initView() {
    	
    	cServerAddress = (EditText)findViewById(R.id.editServerAddress);
    	cServerPort = (EditText)findViewById(R.id.editServerPort);
    }
    
    private Boolean check_host() {	
    	address_server = null;
    	port_server = null;
    	
    	address_server = cServerAddress.getText().toString();
    	if(cServerPort.getText().toString().equals("")){
    		port_server = 0;
    	}else{ 
    		port_server = Integer.parseInt(cServerPort.getText().toString());
    	}
    	Toast.makeText(getApplicationContext(),address_server+":"+port_server, Toast.LENGTH_SHORT).show();
    
    	
    	try {
			main_client = new ClientWork(address_server,port_server);
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "This address is not correct", Toast.LENGTH_SHORT).show();
			return false;
		}
    	
    	if(main_client.isAlreadyConnected()||main_client!=null){
    		try{
    			main_client.close_connect();
	    	} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "This address is not correct", Toast.LENGTH_SHORT).show();
				return false;
			}
    		return true;
    	}else{
    		
    		Toast.makeText(getApplicationContext(), "This address is not correct", Toast.LENGTH_SHORT).show();
    		return false;
    	}		
    }
  
	public void buttonConnect_onClick(View v){
		
		Boolean	a = null;
		a = check_host();
		
		if(a){
			Intent intent = new Intent(Connect.this, Authorization.class);
			intent.putExtra("address_server", address_server);
			intent.putExtra("port_server", port_server);
	        startActivity(intent);
 		}
	}

}
