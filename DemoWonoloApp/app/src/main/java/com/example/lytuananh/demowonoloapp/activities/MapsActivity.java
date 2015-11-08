package com.example.lytuananh.demowonoloapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.example.lytuananh.demowonoloapp.R;
import com.example.lytuananh.demowonoloapp.services.GPSTracker;
import com.example.lytuananh.demowonoloapp.utilities.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GPSTracker mGps;
    private Double mLatitude;
    private Double mLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();

                // Getting reference to the TextView to set latitude
                TextView tvLat = (TextView) v.findViewById(R.id.info_window_tv_lat);

                // Getting reference to the TextView to set longitude
                TextView tvLng = (TextView) v.findViewById(R.id.info_window_tv_lng);

                // Setting the latitude
                tvLat.setText("Latitude:" + latLng.latitude);

                // Setting the longitude
                tvLng.setText("Longitude:"+ latLng.longitude);

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

        // Add a marker in current location and move the camera
        LatLng currentLocation = new LatLng(mLatitude, mLongitude);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title(getResources().getString(R.string.your_location)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(Utilities.DEAFAULT_MAP_ZOOM), 1500, null);
    }
}
