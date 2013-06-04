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
 private String root="/";

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
      
        extras = getIntent().getExtras();
        client.setSession(extras.getLong("session_id", 0));
        server_address = extras.getString("address_server");
        server_port = extras.getInt("port_server");
        
       // head = (TextView)findViewById(R.id.head);
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
    
    private void getDir(String dirPath)
    {
    	try {
			client = new ClientWork(server_address, server_port);
		} catch (Exception e) {
			Log.i("Main","error new connect");
			return;
		}
    	
  
     myPath.setText("Location: " + dirPath);
     try {
		client.Send_to_Dir("F:/");
	} catch (Exception e) {
		Log.i("Main","error send dir");
	}
   
     ArrayList<HashMap<String, String>> fileList = new ArrayList<HashMap<String, String>>();
     
     for(int i=0;i<10;i++)
     {
    	 HashMap<String, String> map = new HashMap<String, String>();
    	 
    	 map.put("id", Integer.toString(i));
         map.put("title","file");
         //map.put(KEY_ARTIST, parser.getValue(e, KEY_ARTIST));
         //map.put(KEY_DURATION, parser.getValue(e, KEY_DURATION));
         map.put("resource_id", Integer.toString(R.drawable.folder));
         fileList.add(map);
    	 
     }
     
     list=(ListView)findViewById(R.id.listView);
     
     adapter = new ListAdapter(this, fileList);
     list.setAdapter(adapter);
     
     
     list.setOnItemClickListener(new OnItemClickListener() {

      

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
				long arg3) {
			String file_path_name = "http://mitajz.16mb.com/video.mp4";//path.get(pos);
		
			if(isDirectory(file_path_name)){
					
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
							
							Intent intent = new Intent(MainActivity.this, AudioStream.class);
							intent.putExtra("audio_url", file_path_name);
							intent.putExtra("audio_name","music.mp3" );
							startActivity(intent);
							
						}else{
							
							
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

    
private Boolean isImage(String url){
	//String images = 
	
	return false;
	
}

private Boolean isTxt(String url){
	//String images = 
	
	return false;
	
}

private Boolean isAudio(String url){
	//String images = 
	
	return true;
	
}
private Boolean isVideo(String url){
	//String images = 
	
	return true;
	
}
private Boolean isDirectory(String url){
	 File file = new File(url); 
	 if (file.isDirectory()) return true;
	return true;
	
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