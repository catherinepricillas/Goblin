package twice.pbdtest;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.ActionBar;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import twice.pbdtest.R;

import twice.pbdtest.UselessGPS.LocationFinder;

//Our class extending fragment
public class Tab2 extends Fragment {

    private LocationManager mLocationManager;
    private LocationFinder mLocationFinder;
    private TextView mTextMessage;

    private void initLocationFinder() {
        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mLocationFinder = new LocationFinder();
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab2, container, false);

        mTextMessage = (TextView) view.findViewById(R.id.message);
        initLocationFinder();
        mLocationFinder.getLocation(getActivity(), mLocationManager, new LocationFinder.OnLocationFoundListener() {
            @Override
            public void onLocationFound(Location location) {
                handleLocationChange(location);
            }
        });
        return view;
    }

    private void handleLocationChange(Location location) {
        String latitude = Double.toString(location.getLatitude());
        String longitude = Double.toString(location.getLongitude());

        mTextMessage.setText("You are at lat=" + latitude + ", lon=" + longitude);

    }
}



