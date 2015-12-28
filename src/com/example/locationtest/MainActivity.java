package com.example.locationtest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.R.integer;
import android.animation.AnimatorSet.Builder;
import android.app.Activity;
import android.content.Context;
import android.database.CursorJoiner.Result;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView position;
	private LocationManager locationManager;
	private String provider;
	private static final int SHOW_LOCATION=1;
		
	private Handler handler=new Handler(){
		public void handleMessage(Message msg){
		   switch(msg.what){
		   case SHOW_LOCATION:
			   String current=(String)msg.obj;
			   position.setText(current);
		   }
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		position=(TextView)findViewById(R.id.textView1);
		locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		List<String> providerList=locationManager.getProviders(true);
		if(providerList.contains(LocationManager.GPS_PROVIDER)){
			provider=LocationManager.GPS_PROVIDER;
		}
		else if(providerList.contains(LocationManager.NETWORK_PROVIDER)){
			provider=LocationManager.NETWORK_PROVIDER;
		}
		else{
			Toast.makeText(this, "No location provider to use", Toast.LENGTH_LONG).show();
			return;
		}
		Location location=locationManager.getLastKnownLocation(provider);
		if(location!=null){
			showLocation(location);
		}
		locationManager.requestLocationUpdates(provider, 5000, 1, locationListener);
	}
	
	LocationListener locationListener=new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			showLocation(location);
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
	}
	private void showLocation(Location location){
		//String currentPosition="latitude is "+location.getLatitude()+"\n"
		//		+"longitude is "+location.getLongitude();
		//position.setText(currentPosition);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				StringBuilder buider=new StringBuilder();
				buider.append("http://api.map.baidu.com/geocoder?address=%E4%B8%8A%E5%9C%B0%E5%8D%81%E8%A1%9710%E5%8F%B7&output=json&key=for11fKwBNsL6FG40PKRYUC7");
				URL url;
				try {
					url = new URL(buider.toString());
					HttpURLConnection connection=(HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream inputStream=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(inputStream));
					String line;
					StringBuilder stringBuilder=new StringBuilder();
					while((line=reader.readLine())!=null){
						stringBuilder.append(line);
					}
					Message msg=new Message();
					msg.obj=stringBuilder.toString();
					msg.what=SHOW_LOCATION;
					handler.sendMessage(msg);
					//JSONObject jsonObject=new JSONObject(stringBuilder.toString());
					//JSONArray array=new JSONArray(stringBuilder.toString());
					//Log.e("MainActivity", stringBuilder.toString());
					//Toast.makeText(MainActivity.this, stringBuilder.toString(), Toast.LENGTH_SHORT).show();
					//for(int i=0;i<array.length();i++){
					//	JSONObject jsonObject=array.getJSONObject(i);
					//	String result=jsonObject.getString("result");
					//	Log.e("MainActivity", result);
						
					//}
					
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		}).start();
		
	}	
	
	
	
}
