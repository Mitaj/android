package com.media.mfcloud;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

//usually, subclasses of AsyncTask are declared inside the activity class.
//that way, you can easily modify the UI thread from here
class DownloadFile extends AsyncTask<String, Integer, String> {
 protected String doInBackground(String sUrl) {
     try {
         URL url = new URL(sUrl);
         URLConnection connection = url.openConnection();
         connection.connect();
         // this will be useful so that you can show a typical 0-100% progress bar
         int fileLength = connection.getContentLength();
         
         int slashIndex = url.toString().lastIndexOf('/');
         String name_file = url.getPath().substring(slashIndex +1);
         // download the file
         InputStream input = new BufferedInputStream(url.openStream());
         OutputStream output = new FileOutputStream("/sdcard/"+name_file);

         byte data[] = new byte[1024];
         long total = 0;
         int count;
         while ((count = input.read(data)) != -1) {
             total += count;
             // publishing the progress....
             publishProgress((int) (total * 100 / fileLength));
             output.write(data, 0, count);
         }

         output.flush();
         output.close();
         input.close();
     } catch (Exception e) {
     }
     return null;
 }

@Override
protected String doInBackground(String... arg0) {
	// TODO Auto-generated method stub
	return null;
}
}