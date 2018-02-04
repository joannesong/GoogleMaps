package nyc.c4q.googlemaps;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;

public class InClassMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_class_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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


        // Add a marker in Sydney and move the camera
        LatLng cupcakes = new LatLng(38.905262, -77.066174);
        mMap.addMarker(new MarkerOptions().position(cupcakes).title("Georgetown Cupcakes"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cupcakes));

        LatLng milkBar = new LatLng(40.731900, -73.985748);
        mMap.addMarker(new MarkerOptions().position(milkBar).title("Momofuku Milk Bar"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(milkBar));

        LatLng strand = new LatLng(40.733218, -73.990670);
        mMap.addMarker(new MarkerOptions().position(strand).title("Strand Bookstore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(strand));

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1020);
        } else {
            mMap.setMyLocationEnabled(true);
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                double lat = location.getLatitude();
                                double lng = location.getLongitude();
                                //mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker in NYC"));
                                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Marker in NYC").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                            }
                        }
                    });
        }
        Geocoder coder = new Geocoder(getApplicationContext());

        List<Address> address;
        LatLng p1 = null;
        try {
            // May throw an IOException
            address = coder.getFromLocationName("111 8th Avenue New York, NY 10011", 5);
            if (address != null) {
                Address location = address.get(0);
                p1 = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(p1).title("Google, NYC").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round)));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //these ui settings help with zooming in
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.isIndoorLevelPickerEnabled();
        //uiSettings.setAllGesturesEnabled(true);
    }
}
