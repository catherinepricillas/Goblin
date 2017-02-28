package twice.pbdtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import twice.pbdtest.AddFriend.AddFriendActivity;
import twice.pbdtest.CollectGem.CollectGemActivity;
import twice.pbdtest.UselessGPS.LocationViewer;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    private String user_email;
    private FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String channel = (sharedpreferences.getString("Brightness", ""));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                if(channel.isEmpty()==false) {
                    // To handle the auto
                    Settings.System.putInt(this.getContentResolver(),
                            Settings.System.SCREEN_BRIGHTNESS, 20);

                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    float f = Float.valueOf(channel);

                    lp.screenBrightness = f;// 100 / 100.0f;
                    getWindow().setAttributes(lp);
                }
            }
            else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }

    public void shareIntent(View view){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://goblin-attack.herokuapp.com/");
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share to"));
    }

    public void callCompass(View view){
        Intent intentCompass = new Intent(this, CompassActivity.class);
        startActivity(intentCompass);
    }


    public void addFriendIntent(View view) {
        Intent intentAddFriend = new Intent(this, AddFriendActivity.class);
        startActivity(intentAddFriend);
    }
    public void callFriendList(View view){
        Intent intentFriendList = new Intent(this, FriendListActivity.class);
        startActivity(intentFriendList);
    }

    public void callSetBrightness(View view){
        Intent intentBrightness = new Intent(this, BrightnessActivity.class);
        startActivity(intentBrightness);
    }

    public void viewMyLocation(View view){
        Intent i = new Intent(this, LocationViewer.class);
        startActivity(i);
    }

    public void collectGem(View view){
        Intent i = new Intent(this, CollectGemActivity.class);
        startActivity(i);
    }

    public void getFragment(View view){
        Intent i = new Intent(this, GetFragment.class);
        startActivity(i);
    }

    public Context getActivity() {
        return this;
    }
}
