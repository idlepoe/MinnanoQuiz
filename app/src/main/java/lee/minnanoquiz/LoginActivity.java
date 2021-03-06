package lee.minnanoquiz;

import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.Iterator;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
    private String uid;
    private User user;
    private boolean uidCheck;
    private Button btnNameInput;
    private EditText editNameInput;
    private TextView txtNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnNameInput = findViewById(R.id.btnNameInput);
        editNameInput = findViewById(R.id.editNameInput);
        txtNotification = findViewById(R.id.txtNotification);

        uid = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);


        this.isUser();

        btnNameInput.setOnClickListener(this);
    }

    private void goMain() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("user", user.getUserName());
        startActivity(intent);
    }

    private void createUid() {

    }

    //implement the onClick method here
    public void onClick(View v) {
        // Perform action on click
        switch (v.getId()) {
            case R.id.btnNameInput:
                if (editNameInput.getText().toString().length() == 0) {
                    txtNotification.setText("名前欄に入力されてないです。");
                    return;
                }
                //DatabaseReference targetUserRef = userRef.child(uid);
                //user.setUserName(editNameInput.getText().toString());
                //targetUserRef.setValue(user);
                this.goMain();
                break;
        }
    }

    private void isUser() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterator<DataSnapshot> child = dataSnapshot.getChildren().iterator();

                while (child.hasNext()) {
                    String targetId = child.next().getKey();
                    if (targetId.equals(uid)) {
                        System.out.println("douichiIdgaaruyo");
                        uidCheck = true;
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                      }
                }
                    if(!uidCheck) {
                        DatabaseReference targetUserRef = userRef.child(uid);
                        targetUserRef.setValue(new User(uid, ""));
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


}
