package com.media.mfcloud;




import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class Connect extends Activity {
	
	private EditText cServerAddress;
	private EditText cServerPort;
	
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
    
    public boolean validateIPAddress( String ipAddress ) {
		String[] tokens = ipAddress.split("\\.");
		if (tokens.length != 4) {
			return false;
		}
		for (String str : tokens) {
			int i = Integer.parseInt(str);
			if ((i < 0) || (i > 255)) {
				return false;
			}
		}
		return true;
	}
    
    private Boolean check_host() {	
    
    	String address_server = cServerAddress.getText().toString();
    	Integer port_server = Integer.parseInt(cServerPort.getText().toString());
    	Toast.makeText(getApplicationContext(), address_server, Toast.LENGTH_SHORT).show();
    	
    	InetAddress ipAddress = null;
    	try{
    	ipAddress = InetAddress.getByName(address_server);
	    } catch (IOException e) {
	    	return false;
		}
	
    	Socket test_socket = new Socket();
    	SocketAddress sa_test = new InetSocketAddress(ipAddress, port_server);
    	try {
			test_socket.connect(sa_test, 1000);
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "This address is not correct", Toast.LENGTH_SHORT).show();
			return false;
		}
    		 
    	if(test_socket.isConnected()||test_socket!=null ||!test_socket.isClosed()){
    		
    		return true;
    	}else{
    		
    		Toast.makeText(getApplicationContext(), "this not address", Toast.LENGTH_SHORT).show();
    		return false;
    	}		
    }
  
	public void buttonConnect_onClick(View v){
		
		Boolean	a = null;
		a = check_host();
				
		if(a){
			Intent intent = new Intent(Connect.this, Authorization.class);
	        startActivity(intent);
 		}
}

}
