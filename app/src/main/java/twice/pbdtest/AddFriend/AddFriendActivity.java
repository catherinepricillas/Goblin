package twice.pbdtest.AddFriend;

import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import twice.pbdtest.R;
import twice.pbdtest.User;

public class AddFriendActivity extends AppCompatActivity {
    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    ListView mListView;
    private static final String TAG = "AddFriendActivity";
    private static final String bluetoothName = "GAFriendshipProtocol";
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private FirebaseAuth mAuth;
    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                if( deviceName.startsWith(bluetoothName) ){
                    attemptFriendship(device);
                }
                Log.v(TAG, deviceName);
            }
        }
    };

    private ConnectThread mConnectThread;
    private AcceptThread mAcceptThread;

    private void attemptFriendship(BluetoothDevice device) {
        final String targetuid = device.getName().substring( bluetoothName.length() );
        final String myuid = mAuth.getCurrentUser().getUid();

        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        final DatabaseReference friendList = fbdb.getReference("users/"+myuid+"/friends");
        final DatabaseReference newFriend = fbdb.getReference("users/"+targetuid);

        newFriend.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user[] = new User[1];
                user[0] = dataSnapshot.getValue(User.class);
                friendList.child(targetuid).setValue(user[0].getName());

                // firebase add friend di sini
                TextView t = (TextView) findViewById(R.id.message);
                t.setText("You are now friend with "+targetuid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                mBluetoothAdapter.cancelDiscovery();
            }
        }, 10000);
    }

    private void initShakeDetector() {
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                handleShakeEvent(count);
            }
        });
    }

    private void handleShakeEvent(int count) {
        TextView t = (TextView) findViewById(R.id.message);
        t.setTextSize(20);
        t.setText("Shaken");
        initiateFriendship();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        initShakeDetector();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(mReceiver);
    }

    protected void initiateFriendship(){
        mAuth = FirebaseAuth.getInstance();
        final String myuid = mAuth.getCurrentUser().getUid();
        Log.v(TAG,myuid);
        mBluetoothAdapter.setName(bluetoothName + myuid);
        requestBluetooth();
    }

    protected void requestBluetooth(){
        /*
        if( !mBluetoothAdapter.isEnabled() ){
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
        }
        else {
            Log.v(TAG, "Bluetooth is ON");
        }
        */
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);

        mBluetoothAdapter.startDiscovery();
    }

    /* register the broadcast receiver with the intent values to be matched */
    @Override
    protected void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);

    }
    /* unregister the broadcast receiver */
    @Override
    protected void onPause() {
        super.onPause();
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
    }



}
