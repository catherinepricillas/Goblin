package twice.pbdtest.CollectGem;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import twice.pbdtest.R;

public class CollectGemActivity extends AppCompatActivity {

    public class RetriveGemTask extends AsyncTask<Pair<String,String>, Void, Gem >{

        @Override
        protected Gem doInBackground(Pair<String, String>... params) {
            final GemAPI g = new GemAPI(params[0].first, params[0].second);
            return g.run();
        }

        protected void onPostExecute(Gem result) {
            TextView t = (TextView) findViewById(R.id.message);

            t.setText("You got "+result.getCount() + " " + result.getType() + " gem" );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_gem);
        // a potentially  time consuming task

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            // Send token to your backend via HTTPS
                            // ...
                            new RetriveGemTask().execute(new Pair<String, String>("fake",idToken));

                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
    }
}
