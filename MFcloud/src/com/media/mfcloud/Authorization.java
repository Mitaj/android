package com.media.mfcloud;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Authorization extends Activity {
	
	private EditText cLoginName;
	private EditText cLoginPassword;
	private ClientWork main_client = null;
	private String server_address = null;
	private Integer server_port = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authorization);
        initView();
    }
    
    private void initView() {
    	cLoginName = (EditText)findViewById(R.id.editLogin);
    	cLoginPassword = (EditText)findViewById(R.id.editPassword);
    	
    	Intent main_intent = getIntent();
    	server_address = main_intent.getStringExtra("address_server");
    	server_port = main_intent.getIntExtra("port_server", 0);
    	if(server_port ==0){
    		Intent intent = new Intent(Authorization.this, Connect.class);
    		startActivity(intent);
    	}
    	
    }
    
    private Boolean check_login(){
    		String login = cLoginName.getText().toString();
    		String password = cLoginPassword.getText().toString();
    		
    		
    		try {
				main_client = new ClientWork(server_address, server_port);
			} catch (Exception e) {
				return false;
			}
    		
    		try {
				main_client.SendString(login+" "+password);
			} catch (Exception e) {
				return false;
			}
    		
    		
    	return false;
    }
   
	public void buttonLogin_onClick(View v) {
		Boolean a;
		a = check_login();
    	if(a){
    		Intent intent = new Intent(this, MainActivity.class);
	        startActivity(intent);
    	}
    }


}
