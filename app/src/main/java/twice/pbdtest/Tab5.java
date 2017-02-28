package twice.pbdtest;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import twice.pbdtest.CollectGem.CollectGemActivity;
import twice.pbdtest.CollectGem.Gem;
import twice.pbdtest.CollectGem.GemAPI;

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

//Our class extending fragment
public class Tab5 extends Fragment {

    class RetriveGemTask extends AsyncTask<Pair<String,String>, Void, Gem >{

        @Override
        protected Gem doInBackground(Pair<String, String>... params) {
            final GemAPI g = new GemAPI(params[0].first, params[0].second);
            return g.run();
        }

        protected void onPostExecute(Gem result) {
            TextView t = (TextView) getActivity().findViewById(R.id.message);

            t.setText("You got "+result.getCount() + " " + result.getType() + " gem" );

        }
    }

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view = inflater.inflate(R.layout.tab5, container, false);
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
                            // Send token to your backend via HTTPS
                            // ...
                            new Tab5.RetriveGemTask().execute(new Pair<String, String>("fake",idToken));

                        } else {
                            // Handle error -> task.getException();
                        }
                    }
                });
        return view;
    }
}

