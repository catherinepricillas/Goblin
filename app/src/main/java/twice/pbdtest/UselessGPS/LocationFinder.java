package twice.pbdtest.UselessGPS;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import twice.pbdtest.Tab2;

/**
 * Created by MaximaXL on 2/21/2017.
 */

public class LocationFinder implements LocationListener {
    private OnLocationFoundListener mListener;
    private LocationManager mLocationManager;
    private Activity mActivity;

    private static final String TAG = "LocationFinder";

    public void getLocation(Activity mActivity, LocationManager mLocationManager, OnLocationFoundListener listener) {
        this.mLocationManager = mLocationManager;
        this.mListener = listener;
        this.mActivity = mActivity;

        this.startLocationFinder();
    }

    private void startLocationFinder() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, this);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, this);
    }

    private void stopLocationFinder() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLocationManager.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mListener.onLocationFound(location);
        this.stopLocationFinder();

        // remove pointer
        this.mListener = null;
        this.mLocationManager = null;
        this.mActivity = null;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v(TAG, String.valueOf(status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.v(TAG, "Provider Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.v(TAG, "Provider Disabled");
    }

    public interface OnLocationFoundListener {
        public void onLocationFound(Location location);
    }
}