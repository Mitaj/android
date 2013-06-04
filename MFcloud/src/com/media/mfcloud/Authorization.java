package com.media.mfcloud;

import com.media.mfcloud.exceptions.SocketException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
				Toast.makeText(getApplicationContext(), "error connect", Toast.LENGTH_SHORT).show();
				return false;
			}
    		try {
				main_client.Send_to_Dir("F:/");
				String files = main_client.getDir();
				Toast.makeText(getApplicationContext(), files, Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Log.i("Auth",e.getMessage());
				Toast.makeText(getApplicationContext(), "error send dir", Toast.LENGTH_SHORT).show();
				return false;
			}
    		
    		/*
    		try {
				main_client.Send_Auth(login, password);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "error authorization", Toast.LENGTH_SHORT).show();
				return false;
			}
    		try {
				if(main_client.GetAuth()) return true;
			} catch (Exception e) {
				Log.i("Auth","error authorization");
			}*/
    		/*
    		String toencrypt = login+":"+password;
    		Toast.makeText(getApplicationContext(), toencrypt, Toast.LENGTH_SHORT).show();
    		
    		try {
    			
				main_client.SendString(toencrypt);
			} catch (Exception e) {
				return false;
			}
    		 String login_encrypted;
    		try {
    			
				login_encrypted =Aes.encrypt_string(toencrypt);
			} catch (Exception e1) {
				return false;			
			}
    		Toast.makeText(getApplicationContext(), "encrypt correct: "+login_encrypted, Toast.LENGTH_SHORT).show();
    		try {
				main_client.SendString(login_encrypted);
			}catch (Exception e) {
				return false;
			}
    		
    		String decrypt;
    		try {
				decrypt = Aes.decrypt_string(login_encrypted);
			} catch (Exception e1) {
				return false;
			}
    		
    		Toast.makeText(getApplicationContext(), "decrypt correct: "+decrypt, Toast.LENGTH_SHORT).show();
    		try {
    			
				main_client.SendString(decrypt);
			} catch (Exception e) {
				return false;
			}
    		
    		*/
    		
    	return false;
    }
   
	public void buttonLogin_onClick(View v) {
		Boolean a;
		a = check_login();
    	if(a){
    		Intent intent = new Intent(this, MainActivity.class);
    		intent.putExtra("address_server", server_address);
			intent.putExtra("port_server", server_port);
			intent.putExtra("session_id", main_client.getSession());
	        startActivity(intent);
    	}
    }


}
