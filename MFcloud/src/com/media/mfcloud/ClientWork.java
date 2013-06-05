package com.media.mfcloud;

import java.net.*;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.io.*;

import com.media.mfcloud.exceptions.SocketException;

import android.text.format.Time;
import android.util.Log;

public class ClientWork {
	
	private String address = "0.0.0.0";
	private Integer port = 80;
	private Socket socket = null;
	private long session_id = 0;
	private static String PARSE_DATA=", ";
	private static String ROOT="/";
	private List<String> folders;
	
	public void setSession(long session){
		session_id = session;
	}
	public long getSession(){
		return session_id;
	}
	
	public List<String> getRootFolders(){
		return folders;
	}
	public String getAddress(){
		return address;
	}
	public Integer getPort(){
		return port;
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
	public String getName(String path)
    {
    	String res;
    		String [] a = path.split("/");
    		res = a[a.length-1];
    	return res;
    }
	public String getPath(String path)
    {
    	String res="";
    		String [] a = path.split("/");
    		for(int i=0;i<a.length-1;i++){
    			res+=a[i]+"/";
    		}
    		
    	return res;
    }
	public String getFile_Url(String file) throws SocketException{
		String res = "";
		String path = getPath(file);
		String name = getName(file);
		try {
			Send_to_File(path,name);
		} catch (Exception e) {
			throw new SocketException("bad connection send file");
		}
		
		try {
			res = getFile();
		} catch (Exception e) {
			throw new SocketException("bad connection get file");
		}
		return res;
	}
	public List<String> getDirectory(String path) throws SocketException{
		List<String> res = new ArrayList<String>();
			try {
				Send_to_Dir(path);
			} catch (Exception e) {
				throw new SocketException("bad connection send dir");
			}
			try {
				String toparse =  getDir();
				if(toparse.equals("")){ 
					return res;
				}
				String [] files = toparse.split(PARSE_DATA);
					if(files.length ==0) throw new SocketException("Bad getting data");
				for(int i = 0;i<files.length;i++){
					res.add(files[i]);
				}
			} catch (Exception e){
				throw new SocketException("bad connection get dir");
			}
			if(path.equals(ROOT)){
				folders = res;	
			}
			
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
	public String getFile() throws Exception{
		String get_messages = GetString();
		String res = "";
		Log.i("message",get_messages);
		if(getValue("msg",get_messages).equals("file")){
			res = getValue("url",get_messages);
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
		if(GetAuth()) {
			return true;
			}
		//encrypted
		//res = Aes.encrypt_string(res);
		//SendString(res);
		return false;
	}
	
	public Boolean GetAuth() throws Exception{
		String get_messages = GetString();
		if(get_messages.equals(""))return false;
		if(error_message(get_messages))return false;
		if(getValue("msg",get_messages).equals("auth")){
			session_id = Long.parseLong(getValue("session",get_messages));
			
			if(session_id==0)return false;
			return true;
		}
		return false;
		
	}
	public Boolean error_message(String msg){
		if(getValue("msg",msg).equals("error")){
			if(getValue("code",msg).equals("303")) return true;
			if(getValue("code",msg).equals("304")) return true;
			if(getValue("code",msg).equals("305")) return true;
			return true;
			}
		return false;
	}
	public void SendString(String tosend)throws Exception{
		
		open_connect();
		
		
		
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
        

         /* DataInputStream in = new DataInputStream(sin);
         
         line = in.readUTF();*/
         StringBuilder sb = new StringBuilder();
         String str = "";
         BufferedReader reader = new BufferedReader(new InputStreamReader(sin, Charset.forName("UTF-16LE")),8*1024);
        
        try{
         while((str = reader.readLine())!=null&&!str.equals('\n')&&!str.equals('\r')&&!str.equals("\r\n")){
        	 
        	 sb.append(str);
         }
        line = sb.toString();
        }catch(IOException e){
        	Log.i("Auth",e.getMessage());
        }
         
        reader.close();
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
