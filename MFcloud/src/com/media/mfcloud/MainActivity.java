package com.media.mfcloud;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.media.mfcloud.audio.AudioStream;
import com.media.mfcloud.exceptions.SocketException;
import com.media.mfcloud.images.ImageLoader;
import com.media.mfcloud.video.VideoStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {
	
// private List<String> item = null;
 private List<String> path = null;
 private String server_address = null;
 private Integer server_port = null;
 private Bundle extras;
 private  String root="/";

 private TextView myPath;
// private TextView head;
 private ClientWork client;
 ListView list;
 ListAdapter adapter;

	//private Client client;
    /** Called when the activity is first created. */
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        server_address = getIntent().getExtras().getString("address_server");
        server_port = getIntent().getExtras().getInt("port_server");
        try {
			client = new ClientWork(server_address, server_port);
		} catch (Exception e) {
			return;
		}
        client.setSession(getIntent().getExtras().getLong("session_id", 0));
       
        myPath = (TextView)findViewById(R.id.path);
        getDir(root);
        
        

    }

    
    public boolean onKeyDown(int keyCode, KeyEvent event) {//обрабатываем кнопку назад
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
        	//удалить сессию
        	Intent intent = new Intent();
            intent.setClass(this, Authorization.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    public void exit_main(){
    	Intent intent = new Intent();
        intent.setClass(this, Authorization.class);
        startActivity(intent);
        finish();
    }
    
    private void getDir(String dirPath)
    {
    	
    	
  
     myPath.setText("Location: " + dirPath);
     try {
		path = client.getDirectory(dirPath);
	} catch (Exception e) {
		Log.i("Main","error send dir");
		exit_main();
	}
   root = dirPath;
     ArrayList<HashMap<String, String>> fileList = new ArrayList<HashMap<String, String>>();
     
     ///   ../
     int diff = 0;
     if(!dirPath.equals("/")){
    	 path.add(0,"../");
    	 /*HashMap<String, String> map = new HashMap<String, String>();
    	 map.put("id",Integer.toString(0));
    	 map.put("title","../");
    	 map.put("resource_id",Integer.toString(R.drawable.folder) );
         fileList.add(map);*/
         
     }
     
     if(!path.isEmpty()){
	     for(int i=0;i<path.size();i++)
	     {
	    	 HashMap<String, String> map = new HashMap<String, String>();
	    	 
	    	 map.put("id", Integer.toString(i+1));
	         map.put("title",path.get(i));
	         //map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
	         //map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
	         String image_path = Integer.toString(R.drawable.default_file);
	         if(isDirectory(path.get(i))){
	        	 image_path = Integer.toString(R.drawable.folder);
				}else{
					if(isTxt(path.get(i))){
						 image_path = Integer.toString(R.drawable.document);
					 
					}else{
						if(isVideo(path.get(i))){
							 image_path = Integer.toString(R.drawable.video);
						}else{
							
							if(isAudio(path.get(i))){
								
								 image_path = Integer.toString(R.drawable.music);
							}else{
								if(isImage(path.get(i))){
									image_path = Integer.toString(R.drawable.picture);
								}else{
									image_path = Integer.toString(R.drawable.default_file);
								}
							}
						}
					}
				}
	         map.put("resource_id",image_path );
	         fileList.add(map);
	    	 
	     }
     }
     list=(ListView)findViewById(R.id.listView);
     
     adapter = new ListAdapter(this, fileList);
     list.setAdapter(adapter);
     
     
     list.setOnItemClickListener(new OnItemClickListener() {

      

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			/*if(!root.equals("/")){
				pos--;
			}*/
			String a = path.get(pos);
			String file_path_name =root+a;
				if(isRoot(a)){
					file_path_name = a;
				}
			if(isDirectory(file_path_name)){
				// ../
				if(a.equals("../")){
					if(isRoot(root)){
						getDir("/");
					}else{
						String [] dir = root.split("/");
						file_path_name ="";
						for(int i =0;i<dir.length-1;i++)
						{
							if(a.equals(dir[i]+"/")) break;
							file_path_name+=dir[i]+"/";
						}
						getDir(file_path_name);
					}
				}else getDir(file_path_name);
			}else{
				if(isTxt(file_path_name)){
				
				       
					/*
					  URL url;
					try {
						url = new URL(file_path_name);
						int slashIndex = url.toString().lastIndexOf("/");
				        String name_file = url.getPath().substring(slashIndex +1);
				        Log.i("Main", "string =  "+name_file);
						
						File txt_file = new File("/sdcard/"+name_file);
						Intent i = new Intent();
						i.setAction(android.content.Intent.ACTION_VIEW);
						i.setDataAndType(Uri.fromFile(txt_file), "text/plain");
						startActivity(i);
					} catch (MalformedURLException e) {
						Log.i("MainActivity","error parse url");
					}*/
				}else{
					if(isVideo(file_path_name)){
						Intent intent = new Intent(MainActivity.this, VideoStream.class);
						intent.putExtra("video_url", file_path_name);
						intent.putExtra("audio_name","music.mp3" );
						startActivity(intent);
					}else{
						
						if(isAudio(file_path_name)){
							String name_sound = client.getName(file_path_name);
							String sound_url;
							try {
								sound_url = "http://"+client.getAddress()+":"+client.getPort()+"/"+client.getFile_Url(file_path_name);
							
							
							
							Intent intent = new Intent(MainActivity.this, AudioStream.class);
							intent.putExtra("audio_url", sound_url);
							intent.putExtra("audio_name",name_sound );
							startActivity(intent);
							} catch (SocketException e) {
								Log.i("Main","error send file");
								exit_main();
							}
							
						}else{
							if(isImage(file_path_name)){
								
								Intent intent = new Intent(MainActivity.this, ImageLoader.class);
								intent.putExtra("image_url", file_path_name);
								//intent.putExtra("audio_name","music.mp3" );
								startActivity(intent);
								
							}
							
						}
					}
				}
			}
			 Log.i("Main", "enter "+pos);
			 
		}
     });
    // ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, path);
     //setListAdapter(fileList);
    }

    private Boolean isRoot(String a){
    	List <String> list_folders = client.getRootFolders();
    	if(list_folders.isEmpty())return false;
    	for(int i=0;i<list_folders.size();i++){
    		if(a.equals(list_folders.get(i))) return true;
    	}
    	return false;
    }
    private int getSize(String a){
    	return a.getBytes().length;
    }
    
private Boolean isImage(String url){
	String[] images ={".jpg",".jpeg",".bmp"};
	String res = url.substring(url.lastIndexOf("."));
	for(int i =0;i<images.length;i++){
		if(res.equals(images[i])) return true;
	}
	return false;
	
}

private Boolean isTxt(String url){
	//String images = 
	
	return false;
	
}

private Boolean isAudio(String url){
	String[] video ={".mp3",".aac"};
	String res = url.substring(url.lastIndexOf("."));
	for(int i =0;i<video.length;i++){
		if(res.equals(video[i])) return true;
	}
	return false;
	
}
private Boolean isVideo(String url){
	String[] video ={".avi",".mp4",".3gp"};
	String res = url.substring(url.lastIndexOf("."));
	for(int i =0;i<video.length;i++){
		if(res.equals(video[i])) return true;
	}
	return false;
	
}
private Boolean isDirectory(String url){
	/* File file = new File(url); 
	 if (file.isDirectory()) return true;*/
	if(url.toCharArray()[url.length() - 1]=='/') return true;
	
	return false;
	
}

/* protected void onListItemClick(ListView l, View v, int position, long id) {
	 Log.i("Main", "enter "+position);
  File file = new File(path.get(position));
  if (file.isDirectory())
  {
   if(file.canRead())
    getDir(path.get(position));
   else
   {
    new AlertDialog.Builder(this)
    .setIcon(R.drawable.ic_launcher)
    .setTitle("[" + file.getName() + "] folder can't be read!")
    .setPositiveButton("OK", 
      new DialogInterface.OnClickListener() {

       @Override
       public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
       }
       }).show();

   }
  }
  else
  {
   new AlertDialog.Builder(this)
    .setIcon(R.drawable.ic_launcher)
    .setTitle("[" + file.getName() + "]")
    .setPositiveButton("OK", 
      new DialogInterface.OnClickListener() {
      
       @Override
       public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub
       }
      }).show();
  }
 }*/
}