package twice.pbdtest;

import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import static android.content.Context.SENSOR_SERVICE;

//Our class extending fragment
public class Tab1 extends Fragment implements SensorEventListener {

    private ImageView image;

    private float currentDegree = 0f;

    private SensorManager mSensorManager;
    private Sensor sensor_acc;
    private Sensor sensor_mag;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;

    //Overriden method onCreateView
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view = inflater.inflate(R.layout.tab1, container, false);
        image = (ImageView) view.findViewById(R.id.imgCompass);

        mSensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensor_acc = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor_mag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        return view;
    }



    float[] mGravity;
    float[] mGeomagnetic;

    public void onSensorChanged(SensorEvent event) {
        float degree;
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                float azimuthInRadians = orientation[0]; // orientation contains: azimut, pitch and roll
                degree = (float)(Math.toDegrees(azimuthInRadians)+360)%360;
                System.out.println(degree);

                RotateAnimation ra = new RotateAnimation(
                        currentDegree,
                        -degree,
                        Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                ra.setDuration(210);
                ra.setFillAfter(true);
                image.startAnimation(ra);
                currentDegree = -degree;
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, sensor_acc, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, sensor_mag, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
}
