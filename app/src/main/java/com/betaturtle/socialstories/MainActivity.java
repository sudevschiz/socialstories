package com.betaturtle.socialstories;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private DrawerLayout drawerLayout;
    private ListView listView;
    private String[] sidebar;
    public TextView story_blank,story_load;

    public int state =  1;
    public String domain = "http://socialstories.in";

    class AsyncHttpGetStories extends AsyncTask<String, String, String> {

        int success=0;

        protected String doInBackground(String... params) {
            String content = "";
            HttpClient hClient = new DefaultHttpClient();
            HttpGet hGet = new HttpGet(params[0]);
            ResponseHandler<String> rHand = new BasicResponseHandler();
            try {
                Log.i("fetching url", params[0]);
                    content = hClient.execute(hGet,rHand);
                success=1;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnknownHostException e){
                Log.i("UnknownHostException", "internet??");
            }catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }
        protected void onPostExecute(String result) {
            if(state==1){
                //Do the whole refresher thing!
                story_load.setText("Server Connection Error. I need to learn how to do this better");
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONArray array = obj.getJSONArray("objects");

                    if(array.length()==0){
                        story_blank.setText("Looks like there are no new stories! Go out there and do something for the society!");
                    }
                    List<RowItem> rowItems;
                    rowItems = new ArrayList<RowItem>();
                    for(int i = 0 ; i < array.length() ; i++){
                        if(array.getJSONObject(i).getString("status").length()==4){//Open requests
//listContents.add(array.getJSONObject(i).getString("blood_type") + "ve at " + array.getJSONObject(i).getString("location"));
                            if(Integer.parseInt(array.getJSONObject(i).getString("current_requirement"))==1){
                                RowItem item = new RowItem(array.getJSONObject(i).getString("blood_type"), array.getJSONObject(i).getString("current_requirement") + " Unit still required", array.getJSONObject(i).getString("location"));
                                rowItems.add(item);
                            }else{
                                RowItem item = new RowItem(array.getJSONObject(i).getString("blood_type"), array.getJSONObject(i).getString("current_requirement") + " Units still required", array.getJSONObject(i).getString("location"));
                                rowItems.add(item);
                            }
                        }
                    }

//                    CustomListViewAdapter adapter = new CustomListViewAdapter(,
//                            R.layout.list_item, rowItems);
//                    listView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{
                story_load.setText("Connection error!");
                success=1;
            }
// Log.i("Oops","it worked till now then!");
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
                {

                }
            });
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SocialStories");
        sidebar = getResources().getStringArray(R.array.sidebar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        listView = (ListView) findViewById(R.id.drawerList);
        listView.setAdapter(new ArrayAdapter(this,android.R.layout.simple_list_item_1,sidebar));
        listView.setOnItemClickListener(this);

        story_load = (TextView) findViewById(R.id.story_load);
        story_blank = (TextView) findViewById(R.id.story_blank);
        final String api_key = "Get this";
        final String username = "Get this";
        new AsyncHttpGetStories().execute(domain + "api/m/stories/?format=json&api_key="+api_key+"&username="+username);

        }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        setTitle(sidebar[position]);
        Toast.makeText(this,sidebar[position]+" clicked",Toast.LENGTH_LONG).show();
    }
}
