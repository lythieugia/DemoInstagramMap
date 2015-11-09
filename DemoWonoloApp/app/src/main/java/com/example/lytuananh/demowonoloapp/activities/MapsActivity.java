package com.example.lytuananh.demowonoloapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lytuananh.demowonoloapp.MyApplication;
import com.example.lytuananh.demowonoloapp.R;
import com.example.lytuananh.demowonoloapp.asynctask.BaseAsyncTask;
import com.example.lytuananh.demowonoloapp.callback.RequestCallback;
import com.example.lytuananh.demowonoloapp.instagram.InstagramApp;
import com.example.lytuananh.demowonoloapp.models.LocationData;
import com.example.lytuananh.demowonoloapp.services.GPSTracker;
import com.example.lytuananh.demowonoloapp.utilities.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker mGps;
    private Double mLatitude;
    private Double mLongitude;
    private GoogleMap.InfoWindowAdapter mAdapter;
    private ArrayList<LocationData> mLocationDataList;


    //Instagram
    private InstagramApp mInsApp;
    private HashMap<String, String> userInfoHashmap = new HashMap<String, String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //get Instance InstagramApp
        mInsApp = ((MyApplication)getApplication()).getInstgramMap();

        //get current Location
        initLocation();

    }

    private void initLocation(){
        mGps = new GPSTracker(MapsActivity.this);
        // check if GPS enabled
        if(mGps.canGetLocation()){

            mLatitude = mGps.getLatitude();
            mLongitude = mGps.getLongitude();

        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            mGps.showSettingsAlert();
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mLocationDataList = new ArrayList<>();
        /*mAdapter = new GoogleMap.InfoWindowAdapter() {
            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.map_info_window, null);

                // Getting reference to the TextView to set Username
                TextView tvUserName = (TextView) v.findViewById(R.id.info_window_tv_username);
                // Getting reference to the TextView to set Your location
                TextView tvYourLocation = (TextView) v.findViewById(R.id.info_window_your_location);
                // Getting reference to the TextView to set Your location
                ImageView imgLocation = (ImageView) v.findViewById(R.id.info_window_img_location);

                String title = arg0.getTitle();
                if(title.equals(getResources().getString(R.string.your_location))){
                    tvYourLocation.setVisibility(View.VISIBLE);
                    tvUserName.setVisibility(View.GONE);
                    imgLocation.setVisibility(View.GONE);
                }else {
                    int idLocation = Integer.parseInt(title);
                    if(mLocationDataList!=null && mLocationDataList.size()>0) {
                        LocationData entry = mLocationDataList.get(idLocation);
                        tvYourLocation.setVisibility(View.GONE);
                        tvUserName.setVisibility(View.VISIBLE);
                        imgLocation.setVisibility(View.VISIBLE);
                        tvUserName.setText(entry.getUser().getUsername());
                        if(entry.getLink()!=null && entry.getLink().trim().length()>0 && entry.getType().equals(Utilities.INS_IMAGE_TYPE)) {
                            String image = String.valueOf(entry.getImages().get(Utilities.INS_IMAGE_STANDARD).getUrl());
                            Log.e("MAGELINK","IMAGE "+image);
                            Picasso.with(getApplicationContext())
                                            .load(image)
                                            .fit()
                                            .centerCrop()
                                            .placeholder(R.mipmap.ic_launcher)
                                            .error(R.mipmap.ic_launcher)
                                            .into(imgLocation,new InfoWindowRefresher(arg0));
                        }
                    }

                }
                // Returning the view containing InfoWindow contents
                return v;

            }


        };*/
        mAdapter = new MyInfoWindowAdapter();
        mMap.setInfoWindowAdapter(mAdapter);

        // Add a marker in current location and move the camera
        LatLng currentLocation = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title(getResources().getString(R.string.your_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(Utilities.DEAFAULT_MAP_ZOOM), 1500, null);

        //get Instagram Data
        String url2 = "https://api.instagram.com/v1/media/search?lat="+mLatitude+"&lng="+mLongitude+"&access_token="+mInsApp.getTOken();
        Log.e("IMAGEURL", "link " + url2);
        new BaseAsyncTask(this,null,null,true,getLocationCallback).execute(url2);
    }

    private class InfoWindowRefresher implements Callback {
        private Marker markerToRefresh;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.markerToRefresh = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            markerToRefresh.showInfoWindow();
        }

        @Override
        public void onError() {}
    }

    /* Callback object */
    RequestCallback getLocationCallback = new RequestCallback() {
        @Override
        public void success(Object result) {

            ArrayList<LocationData> data = parseContent(result.toString());

            if(data!=null&& data.size()>0) {
                for(int i = 0;i<data.size();i++){
                    mLocationDataList.add(data.get(i));
                }
                addMarker(mLocationDataList);
            }else {
                Toast.makeText(getApplicationContext(), "No Data", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void failed(String result) {
            Toast.makeText(getApplicationContext(), "Fail", Toast.LENGTH_SHORT).show();
        }
    };

    private void addMarker(ArrayList<LocationData> data){
        for(int i = 0;i<data.size();i++){
            LocationData entry = data.get(i);
            LatLng location = new LatLng(entry.getLocation().getLatitude(), entry.getLocation().getLongitude());

            MarkerOptions market = new MarkerOptions().position(location).title("" + i).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(market);
        }
    }

    private ArrayList<LocationData> parseContent(String json){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.AUTO_DETECT_FIELDS, true);
        ArrayList<LocationData> contents = new ArrayList<LocationData>();
        JsonNode node;
        try {
            node = mapper.readTree(json);
            TypeReference<ArrayList<LocationData>> reference = new TypeReference<ArrayList<LocationData>>() {};
            contents = mapper.readValue(node.traverse(), reference);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return contents;
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        boolean not_first_time_showing_info_window;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.map_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
            // Getting reference to the TextView to set Username
            TextView tvUserName = (TextView) myContentsView.findViewById(R.id.info_window_tv_username);
            // Getting reference to the TextView to set Your location
            TextView tvYourLocation = (TextView) myContentsView.findViewById(R.id.info_window_your_location);
            // Getting reference to the TextView to set Your location
            ImageView imgLocation = (ImageView) myContentsView.findViewById(R.id.info_window_img_location);

            String title = marker.getTitle();
            if(title.equals(getResources().getString(R.string.your_location))){
                tvYourLocation.setVisibility(View.VISIBLE);
                tvUserName.setVisibility(View.GONE);
                imgLocation.setVisibility(View.GONE);
            }else {
                int idLocation = Integer.parseInt(title);
                if(mLocationDataList!=null && mLocationDataList.size()>0) {
                    LocationData entry = mLocationDataList.get(idLocation);
                    tvYourLocation.setVisibility(View.GONE);
                    tvUserName.setVisibility(View.VISIBLE);
                    imgLocation.setVisibility(View.VISIBLE);
                    tvUserName.setText(entry.getUser().getUsername());
                    if (entry.getLink() != null && entry.getLink().trim().length() > 0 && entry.getType().equals(Utilities.INS_IMAGE_TYPE)) {
                        String image = String.valueOf(entry.getImages().get(Utilities.INS_IMAGE_STANDARD).getUrl());
                        if (not_first_time_showing_info_window) {
                            Picasso.with(MapsActivity.this).load(image).fit()
                                    .centerCrop()
                                    .into(imgLocation);
                        } else { // if it's the first time, load the image with the callback set
                            not_first_time_showing_info_window = true;
                            Picasso.with(MapsActivity.this).load(image).fit()
                                    .centerCrop()
                                   .into(imgLocation, new InfoWindowRefresher(marker));
                        }
                    }
                }
            }
            return myContentsView;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
