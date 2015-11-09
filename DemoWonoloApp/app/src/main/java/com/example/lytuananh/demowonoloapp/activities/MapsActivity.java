package com.example.lytuananh.demowonoloapp.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraChangeListener {

    private GoogleMap mMap;
    private GPSTracker mGps;
    private Double mLatitude;
    private Double mLongitude;
    private GoogleMap.InfoWindowAdapter mAdapter;
    private ArrayList<LocationData> mLocationDataList;
    private Boolean isMovingCam = false;
    private LatLng mCurrentViewLatLng;

    //Instagram
    private InstagramApp mInsApp;

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
        mAdapter = new MyInfoWindowAdapter();
        mMap.setInfoWindowAdapter(mAdapter);
        mMap.setOnCameraChangeListener(this);

        // Add a marker in current location and move the camera
        LatLng currentLocation = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title(getResources().getString(R.string.your_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

        //get Instagram Data
        String url2 = Utilities.BASE_URL+"lat="+mLatitude+"&lng="+mLongitude+"&access_token="+mInsApp.getTOken();
        new BaseAsyncTask(this,false,getLocationCallback).execute(url2);
    }

    /* Callback object */
    RequestCallback getLocationCallback = new RequestCallback() {
        @Override
        public void success(Object result) {

            ArrayList<LocationData> data = parseContent(result.toString());
            ArrayList<LocationData> data2 = new ArrayList<>();

            if(data!=null&& data.size()>0) {

                for(int i = 0;i<data.size();i++){
                    if(!mLocationDataList.contains(data.get(i))) {
                        mLocationDataList.add(data.get(i));
                        data2.add(data.get(i));
                    }
                }

                addMarker(data2);
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
            MarkerOptions market = new MarkerOptions().position(location).title("" + data.get(i).getId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
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

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        //get Instagram Data
        mCurrentViewLatLng = cameraPosition.target;
        handler.removeMessages(Utilities.GET_IMAGE);
        handler.sendEmptyMessageAtTime(Utilities.GET_IMAGE, Utilities.DELAY_TIME_MOVING_APP);
    }

    private Handler handler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message m) {
            if(m.what==Utilities.GET_IMAGE) {
                String url2 = Utilities.BASE_URL + "lat=" + mCurrentViewLatLng.latitude + "&lng=" + mCurrentViewLatLng.longitude + "&access_token=" + mInsApp.getTOken();
                new BaseAsyncTask(getApplicationContext(), false, getLocationCallback).execute(url2);
            }
        }
    };

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;
        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        MyInfoWindowAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.map_info_window, null);
            imageLoader = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder()
                    .showStubImage(R.mipmap.ic_launcher)		//	Display Stub Image
                    .showImageForEmptyUri(R.mipmap.ic_launcher)	//	If Empty image found
                    .cacheInMemory()
                    .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
        }

        @Override
        public View getInfoContents(Marker marker) {
            return  null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            // Getting reference to the TextView to set Username
            TextView tvUserName = (TextView) myContentsView.findViewById(R.id.info_window_tv_username);
            // Getting reference to the TextView to set Your location
            TextView tvYourLocation = (TextView) myContentsView.findViewById(R.id.info_window_your_location);
            // Getting reference to the TextView to set caption
            TextView tvCaption = (TextView) myContentsView.findViewById(R.id.info_window_tv_caption);
            // Getting reference to the TextView to set Your location
            ImageView imgLocation = (ImageView) myContentsView.findViewById(R.id.info_window_img_location);
            String title = marker.getTitle();
            if(title.equals(getResources().getString(R.string.your_location))){
                tvYourLocation.setVisibility(View.VISIBLE);
                tvUserName.setVisibility(View.GONE);
                imgLocation.setVisibility(View.GONE);
                tvCaption.setVisibility(View.GONE);
            }else {
                String idLocation = title;
                if(mLocationDataList!=null && mLocationDataList.size()>0) {
                    LocationData entry = null;
                    for(int i = 0;i<mLocationDataList.size();i++){
                        if(idLocation.equals(mLocationDataList.get(i).getId())){
                            entry = mLocationDataList.get(i);
                        }
                    }
                    if(entry!=null) {
                        tvYourLocation.setVisibility(View.GONE);
                        tvUserName.setVisibility(View.VISIBLE);
                        imgLocation.setVisibility(View.VISIBLE);
                        tvCaption.setVisibility(View.VISIBLE);
                        tvUserName.setText(entry.getUser().getUsername());
                        if (entry.getCaption() != null && entry.getCaption().getText() != null) {
                            tvCaption.setText(entry.getCaption().getText());
                        }
                        if (entry.getLink() != null && entry.getLink().trim().length() > 0 && entry.getType().equals(Utilities.INS_IMAGE_TYPE)) {
                            String image = String.valueOf(entry.getImages().get(Utilities.INS_IMAGE_STANDARD).getUrl());
                            imageLoader.displayImage(image, imgLocation, options, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri,
                                                              View view, Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view,
                                            loadedImage);
                                    if (marker.isInfoWindowShown()) {
                                        marker.hideInfoWindow();
                                        Handler handler1 = new Handler();
                                        handler1.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                marker.showInfoWindow();
                                            }
                                        }, 60);
                                    }
                                }
                            });
                        }
                    }
                }
            }
            return myContentsView;
        }


    }
}
