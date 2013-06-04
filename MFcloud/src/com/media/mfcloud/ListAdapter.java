package com.media.mfcloud;


import java.util.ArrayList;
import java.util.HashMap;
 
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
public class ListAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader; 
 
    public ListAdapter(Activity a, ArrayList<HashMap<String, String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // imageLoader=new ImageLoader(activity.getApplicationContext());
    }
 
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.row, null);
 
        TextView title = (TextView)vi.findViewById(R.id.title); // title
       // TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        //TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
 
        HashMap<String, String> file = new HashMap<String, String>();
        file = data.get(position);
 
        // Setting all values in listview
        title.setText(file.get("title"));
        //artist.setText(song.get(CustomizedListView.KEY_ARTIST));
        //duration.setText(song.get(CustomizedListView.KEY_DURATION));
        thumb_image.setImageResource(Integer.parseInt(file.get("resource_id")));
        return vi;
    }
}