package com.example.lin01;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private LocationManager lmgr;
    private GoogleMap mMap;

    private SQLiteDatabase database;
    private RequestQueue queue;
    String TAG = MainActivity.class.getSimpleName()+"My";
    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();


    private void catchData(){
        String catchData = " https://datacenter.taichung.gov.tw/swagger/OpenData/91deb8b8-7547-4a60-8cae-7c95c0df2fda";
        ProgressDialog dialog = ProgressDialog.show(this,"讀取中"
                ,"請稍候",true);

        new Thread(()->{
            try {
                URL url = new URL(catchData);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream is = connection.getInputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                String line = in.readLine();
                StringBuffer json = new StringBuffer();
                while (line != null) {
                    json.append(line);
                    line = in.readLine();
                }
                JSONArray jsonArray = new JSONArray(String.valueOf(json));
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String num = jsonObject.getString("ID");
                    String dd = jsonObject.getString("Position");
                    String call = jsonObject.getString("EName");
                    String carof1 = jsonObject.getString("X");
                    String carof2 = jsonObject.getString("Y");
                    String carof3 = jsonObject.getString("CArea");
                    String loa = jsonObject.getString("EArea");
                    String aa = jsonObject.getString("CAddress");
                    String rate = jsonObject.getString("EAddress");
                    String time = jsonObject.getString("AvailableCNT");
                    String ps = jsonObject.getString("EmpCNT");
                    String uptime = jsonObject.getString("UpdateTime");


                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("pos",dd);
                    hashMap.put("xco",carof1);
                    hashMap.put("yco",carof2);
                    hashMap.put("motorcar",time);
                    hashMap.put("pos2",ps);
                    hashMap.put("pos3",uptime);

                    arrayList.add(hashMap);
                }
                Log.d(TAG,""+json);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        catchData();
        queue = Volley.newRequestQueue(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED){
            ;
        }else{
            requestPermissions(
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    8);
        }



    }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 8){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

            }else{
                finish();
            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        moveCamera();
        addMarker();
    }




    private void moveCamera(){
        if (mMap != null) {
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);

        }
    }





    public static  LatLng latlng = new LatLng(,);
    private void  addMarker(){
        int position = 0;
        MarkerOptions options =new MarkerOptions();
        options.position(latlng);

        options.title(arrayList.get(position).get("pos"));
        options.snippet("剩餘:"+arrayList.get(position).get("motorcar")+";總共:"+arrayList.get(position).get("pos2"));
        options.alpha(0.9f);
        options.anchor(0.5f,0.5f);
        options.draggable(false);
        options.flat(false);
        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_bus));
        mMap.addMarker(options);
    }


}