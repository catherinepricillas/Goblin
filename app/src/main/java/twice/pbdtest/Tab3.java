package twice.pbdtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Vector;

//Our class extending fragment
public class Tab3 extends Fragment {

    ListView listView ;
    private FirebaseAuth mAuth;
    View view;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        view = inflater.inflate(R.layout.tab3, container, false);

        Vector<String> vecStr = new Vector();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = fbdb.getReference("users");
        databaseReference.child(mAuth.getCurrentUser().getUid() + "/friends").addListenerForSingleValueEvent(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Vector<String> vecName = new Vector();
                Vector<String> vecUId = new Vector();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    String key = postSnapshot.getKey();
                    Object obj = postSnapshot.getValue();
                    vecUId.add(key);
                    vecName.add(obj.toString());
                }
                initList(vecName,vecUId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("cancelled");
            }
        });

        return view;
    }

    public void initList(final Vector<String> vecName, final Vector<String> vecUId){
        listView = (ListView) view.findViewById(R.id.list);

        String[] name = new String[vecName.size()];
        for(int i=0; i<vecName.size(); i++){
            name[i] = vecName.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, name);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                startIntent(vecName.get((int)id),vecUId.get((int)id));
            }
        });
    }

    public void startIntent(String name, String uid){
        Intent intentChat = new Intent(getActivity(), ChatActivity.class);
        intentChat.putExtra("name",name);
        intentChat.putExtra("uid",uid);
        startActivity(intentChat);
    }
}

