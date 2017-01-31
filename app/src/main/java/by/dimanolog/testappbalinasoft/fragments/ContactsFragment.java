package by.dimanolog.testappbalinasoft.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import by.dimanolog.testappbalinasoft.App;
import by.dimanolog.testappbalinasoft.R;
import by.dimanolog.testappbalinasoft.custom.CustomMapView;


/**
 * Created by Dimanolog on 21.01.2017.
 */

public class ContactsFragment extends Fragment {
    private static final String TAG = ContactsFragment.class.getSimpleName();

    private CustomMapView mGoogleMapView;
    private TextView mContactTel;
    private GoogleMap mGoogleMap;
    private GoogleApiClient mClient;

    private TextView fragmentContactsAddress1;
    private TextView fragmentContactsAddress2;
    private TextView fragmentContactsAddress3;
    private TextView fragmentContactsAddress4;

    private LatLng mUserPoint;
    private LatLng mFarforAddress1;
    private LatLng mFarforAddress2;
    private LatLng mFarforAddress3;
    private LatLng mFarforAddress4;
    private Marker mUserMarker;


    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        startCheckLocation();
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
                .build();

        double lat = Double.parseDouble(getResources().getString(R.string.farfor_addres1_lat));
        double lng = Double.parseDouble(getResources().getString(R.string.farfor_addres1_lng));

        mFarforAddress1 = new LatLng(lat, lng);

        lat = Double.parseDouble(getResources().getString(R.string.farfor_addres2_lat));
        lng = Double.parseDouble(getResources().getString(R.string.farfor_addres2_lng));

        mFarforAddress2 = new LatLng(lat, lng);

        lat = Double.parseDouble(getResources().getString(R.string.farfor_addres3_lat));
        lng = Double.parseDouble(getResources().getString(R.string.farfor_addres3_lng));

        mFarforAddress3 = new LatLng(lat, lng);

        lat = Double.parseDouble(getResources().getString(R.string.farfor_addres4_lat));
        lng = Double.parseDouble(getResources().getString(R.string.farfor_addres4_lng));

        mFarforAddress4 = new LatLng(lat, lng);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        mGoogleMapView = (CustomMapView) view.findViewById(R.id.fragment_contacts_map);
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.fragment_contacts_scrolview);
        mGoogleMapView.onCreate(savedInstanceState);
        mGoogleMapView.setViewParent(scrollView);

        if (mGoogleMapView != null) {
            mGoogleMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mGoogleMap = googleMap;
                    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
                    addMarkers();
                }
            });

        }

        mContactTel = (TextView) view.findViewById(R.id.fragment_contacts_tel);

        mContactTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", getString(R.string.farfor_tel), null));
                startActivity(intent);
            }
        });
        View.OnClickListener addressOnClickListener = new AddressOnClickListener();

        fragmentContactsAddress1 = (TextView) view.findViewById(R.id.fragment_contacts_address1);
        fragmentContactsAddress1.setOnClickListener(addressOnClickListener);
        fragmentContactsAddress2 = (TextView) view.findViewById(R.id.fragment_contacts_address2);
        fragmentContactsAddress2.setOnClickListener(addressOnClickListener);
        fragmentContactsAddress3 = (TextView) view.findViewById(R.id.fragment_contacts_address3);
        fragmentContactsAddress3.setOnClickListener(addressOnClickListener);
        fragmentContactsAddress4 = (TextView) view.findViewById(R.id.fragment_contacts_address4);
        fragmentContactsAddress4.setOnClickListener(addressOnClickListener);

        return view;
    }

    void startCheckLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "ACCESS_FINE_LOCATION permission error");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, App.PERMISSION_ACCESS_FINE_LOCATION);
            }
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "ACCESS_COARSE_LOCATION permission error");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, App.PERMISSION_ACCESS_COARSE_LOCATION);
            }

            return;
        }


        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                        mUserPoint = new LatLng(location.getLatitude(), location.getLongitude());
                        addUserLocation();
                    }
                });

    }

    private void addUserLocation() {
        if (mUserPoint != null) {
            if (mUserMarker != null) {
                mUserMarker.setPosition(mUserPoint);

            } else {
                MarkerOptions userMarkerOptions = new MarkerOptions()
                        .position(mUserPoint)
                        .title(getString(R.string.user_position_marker));
                mUserMarker = mGoogleMap.addMarker(userMarkerOptions);
            }
        }
    }

    private void addMarkers() {

        mGoogleMap.addMarker(new MarkerOptions()
                .position(mFarforAddress1));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mFarforAddress2));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mFarforAddress3));
        mGoogleMap.addMarker(new MarkerOptions()
                .position(mFarforAddress4));

    }

    private void updateCamera(LatLng point) {
        if (mUserPoint != null) {
            boundsCamera(mUserPoint, point);
        } else {
            zoomedCamera(point);
        }
    }

    void boundsCamera(LatLng point1, LatLng point2) {

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(point1)
                .include(point2)
                .build();

        int margin = getResources().getDimensionPixelSize(R.dimen.map_inset_margin);
        CameraUpdate update = CameraUpdateFactory.newLatLngBounds(bounds, margin);
        mGoogleMap.animateCamera(update);

    }

    void zoomedCamera(LatLng point) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(point, 16f);
        mGoogleMap.animateCamera(update);

    }


    @Override
    public void onStart() {
        mGoogleMapView.onStart();
        super.onStart();
        mClient.connect();

    }

    @Override
    public void onResume() {
        mGoogleMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleMapView.onStop();
        mClient.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mGoogleMapView.onLowMemory();
    }

    private class AddressOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.fragment_contacts_address1:
                    updateCamera(mFarforAddress1);
                    break;
                case R.id.fragment_contacts_address2:
                    updateCamera(mFarforAddress2);
                    break;
                case R.id.fragment_contacts_address3:
                    updateCamera(mFarforAddress3);
                    break;
                case R.id.fragment_contacts_address4:
                    updateCamera(mFarforAddress4);
                    break;
                default:
                    throw new IllegalArgumentException();

            }


        }
    }
}

