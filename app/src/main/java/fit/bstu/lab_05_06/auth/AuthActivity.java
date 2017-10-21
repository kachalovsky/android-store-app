package fit.bstu.lab_05_06.auth;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import fit.bstu.lab_05_06.R;
import fit.bstu.lab_05_06.models.Product;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.architecture.ChainOfActivitiesController;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.BaseInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.count_fragment.CountInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.image_fragment.ImageInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.name_fragment.NameInputFragment;
import fit.bstu.lab_05_06.shared_modules.chain_of_activities.chain_fragments.price_fragment.PriceInputFragment;

/**
 * Created by andre on 21.10.2017.
 */

public class AuthActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);
        mAuth = FirebaseAuth.getInstance();
        setButtonsListeners();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            public static final String TAG = "1";

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    private void setButtonsListeners() {
        Button btnIn = (Button) findViewById(R.id.btn_sign_in);
        Button btnUp = (Button) findViewById(R.id.btn_sign_up);
        btnIn.setOnClickListener(v -> {
            TextView twEmail = (TextView) findViewById(R.id.tw_email);
            TextView twPassword = (TextView) findViewById(R.id.tw_password);
            mAuth.signInWithEmailAndPassword(twEmail.getText().toString(), twPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        public String TAG = "TAG";

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());
//                                Toast.makeText(this, "auth_failed",
//                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        });

        btnUp.setOnClickListener(v -> {
            TextView twEmail = (TextView) findViewById(R.id.tw_email);
            TextView twPassword = (TextView) findViewById(R.id.tw_password);
            mAuth.createUserWithEmailAndPassword(twEmail.getText().toString(), twPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        public static final String TAG = "TAG";

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
//                                Toast.makeText(EmailPasswordActivity.this, R.string.auth_failed,
//                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
