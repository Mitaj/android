package com.media.mfcloud;

import java.net.*;
import java.io.*;

public class ClientWork {
	
	private String address = "0.0.0.0";
	private Integer port = 80;
	private Socket socket = null;
	
	public ClientWork(String naddress,Integer nport) throws Exception{
		
		if(isAlreadyConnected()){
			socket.close();
		}
		
		address = naddress;
		port = nport;
		
		socket = new Socket();
		InetAddress ipAddress = null;
    	ipAddress = InetAddress.getByName(address);
    	
	    SocketAddress sa_test = new InetSocketAddress(ipAddress, port);
		socket.connect(sa_test, 1000);
		
    	
		 
	}
	
	public void SendString(String tosend)throws Exception{
		
		if(isAlreadyConnected()){
			
		}else{
			open_connect();
		}
		
		 InputStream sin = socket.getInputStream();
         OutputStream sout = socket.getOutputStream();
         
         DataInputStream in = new DataInputStream(sin);
         DataOutputStream out = new DataOutputStream(sout);
         
         //sending
         out.writeUTF(tosend);
         out.flush();
	}
	
	public Boolean isAlreadyConnected(){
		if(socket!= null){
			return socket.isConnected();
		}else return false;
	}
	
	public void open_connect() throws Exception{
		if(isAlreadyConnected()){
			return;
		}
		socket = new Socket();
		InetAddress ipAddress = null;
    	ipAddress = InetAddress.getByName(address);
    	
	    SocketAddress sa_test = new InetSocketAddress(ipAddress, port);
		socket.connect(sa_test, 1000);
	}
	
	public void close_connect() throws Exception{
		if(isAlreadyConnected()){
			socket.close();
		}
	}

}
