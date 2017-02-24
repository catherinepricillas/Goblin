package twice.pbdtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
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

public class ChatHistoryActivity extends AppCompatActivity {
    ListView listView ;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);

        Vector<String> vecStr = new Vector();
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = fbdb.getReference("users");
        databaseReference.child(mAuth.getCurrentUser().getUid() + "/chats").addValueEventListener(new ValueEventListener(){
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
                listView = (ListView) findViewById(R.id.list);
                ((ViewManager)listView.getParent()).removeView(listView);
                initList(vecName,vecUId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("cancelled");
            }
        });
    }

    public void initList(final Vector<String> vecName, final Vector<String> vecUId){
        listView = (ListView) findViewById(R.id.list);

        String[] name = new String[vecName.size()];
        for(int i=0; i<vecName.size(); i++){
            name[i] = vecName.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, name);

        listView.setAdapter(adapter);
    }
}
