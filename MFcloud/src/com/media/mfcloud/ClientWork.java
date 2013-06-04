package com.media.mfcloud;

import java.net.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

import android.text.format.Time;
import android.util.Log;

public class ClientWork {
	
	private String address = "0.0.0.0";
	private Integer port = 80;
	private Socket socket = null;
	private long session_id = 0;
	
	public void setSession(long session){
		session_id = session;
	}
	public long getSession(){
		return session_id;
	}
	
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
	
	public String getValue(String name_value,String msg){
		String res = "";
	    res =msg.substring(msg.lastIndexOf(name_value+"=")+name_value.length()+2);
	    res = res.substring(0, res.indexOf("\""));
		return res;
		
	}
	
	public String getDir() throws Exception{
		String get_messages = GetString();
		String res = "";
		Log.i("message",get_messages);
		if(getValue("msg",get_messages).equals("dir")){
			res = getValue("data",get_messages);
			return res;
		}
		return "";
	}
	public void Send_to_Dir(String path) throws Exception{//F:/
		String res = "?msg=\"dir\"&time=\""+new Date().getTime()+"\"&session=\""+session_id+"\"&path=\""+path+"\"";
		SendString(res);
		
	}
	public void Send_to_File(String path,String name) throws Exception{//F:/
		String res = "?msg=\"file\"&time=\""+new Date().getTime()+"\"&session=\""+session_id+"\"&path=\""+path+"\"&name=\""+name+"\"";
		SendString(res);
		
	}
	public Boolean Send_Auth(String login,String password) throws Exception{
		
		String res = "?msg=\"auth\"&time=\""+new Date().getTime()+"\"&login=\""+login+"\"&pass=\""+password+"\"";
		
		SendString(res);
		if(GetAuth()) return true;
		//encrypted
		//res = Aes.encrypt_string(res);
		//SendString(res);
		return false;
	}
	
	public Boolean GetAuth() throws Exception{
		String get_messages = GetString();
		if(getValue("msg",get_messages).equals("auth")){
			session_id = Long.parseLong(getValue("session",get_messages));
			if(session_id==0)return false;
			return true;
		}
		return false;
		
	}
	public void SendString(String tosend)throws Exception{
		
		if(isAlreadyConnected()){
			
		}else{
			//open_connect();
		}
		
		
         OutputStream sout = socket.getOutputStream();
         
         
         DataOutputStream out = new DataOutputStream(sout);
         
         //sending
        // out.write(tosend.getBytes());
         out.writeUTF(tosend);
         out.flush();
	}
	
public String GetString()throws Exception{
		if(!isAlreadyConnected()){
			open_connect();
		}
		long start = System.currentTimeMillis();
		long end = start + 10*1000;
		String line =new String("".getBytes());
		
		 InputStream sin = socket.getInputStream();
        
         
         //DataInputStream in = new DataInputStream(sin);
         //BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sin),8192);
         StringBuilder sb = new StringBuilder();
         String str;
         BufferedReader reader = new BufferedReader(new InputStreamReader(sin, Charset.forName("UTF-16LE")),8192);
       /*  
         boolean notDone = true;
         while(notDone)
         {
             if(reader.a() > 0)
             {
                // do stuff
             }
             else
             {
                 System.out.println("Done!");
                 notDone = false;
             }
         }
         do{
        	 str = reader.readLine();
        	 sb.append(str);
         }while(str!=null&&!str.equals(""));
        line = sb.toString();
        reader.close();*/
         // in.readUTF().getBytes("UTF-8");
        	 
         //line =ucs2ToUTF8(sb.toString().getBytes("UTF-16"));
         
         return line;
	}

public String ucs2ToUTF8(byte[] ucs2Bytes) throws UnsupportedEncodingException{  
	  
    String unicode = new String(ucs2Bytes, "UTF-16");  
     
    String utf8 = new String(unicode.getBytes("UTF-8"), "Cp1252");  
     
    return utf8;  
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
