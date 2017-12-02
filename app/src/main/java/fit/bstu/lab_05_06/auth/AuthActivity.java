package fit.bstu.lab_05_06.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;

import fit.bstu.lab_05_06.Constants;
import fit.bstu.lab_05_06.MainActivity;
import fit.bstu.lab_05_06.shared_modules.list_of_items.ListOfItems;
import fit.bstu.lab_05_06.R;

/**
 * Created by andre on 21.10.2017.
 */

public class AuthActivity extends AppCompatActivity {

    private static boolean RESUME_FROM_BACKGROUND = false;
    private AuthManager authManager;
    ISignIn listener;

    @Override
    public void onResume(){
        super.onResume();
        if(RESUME_FROM_BACKGROUND) {
            authManager.tryLogin();
        } else {
            RESUME_FROM_BACKGROUND = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        authManager = AuthManager.getInstance();
        authManager.initialize(this);
        listener = (user) -> {
            finish();
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        };
        authManager.addSignInListener(listener);
        authManager.addLogOutListener(() -> {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (listener != null) {
            authManager.removeSignInListener(listener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth);
        setButtonsListeners();

    }

    private void generateToastMessage(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    private EmailPassCredentials getCredentials() {
        EditText twEmail = (EditText) findViewById(R.id.edit_email);
        EditText twPassword = (EditText) findViewById(R.id.edit_pw);
        return new EmailPassCredentials(twEmail.getText().toString(), twPassword.getText().toString());
    }

    private void handleResult(Task<AuthResult> result) {
        if(result.isSuccessful()) {
            //TODO: open main activity
        } else {
            generateToastMessage(result.getException().getLocalizedMessage());
        }
    }

    private void setButtonsListeners() {
        final AuthActivity activity = this;
        Button btnIn = (Button) findViewById(R.id.btn_sign_in);
        ImageButton btnInGoogle = (ImageButton) findViewById(R.id.btn_sign_in_google);
        Button btnUp = (Button) findViewById(R.id.btn_sign_up);

        btnIn.setOnClickListener(v -> {
            authManager.signIn(getCredentials(), this::handleResult);

        });

        btnUp.setOnClickListener(v -> {
            authManager.signUp(getCredentials(), this::handleResult);
        });

        btnInGoogle.setOnClickListener(v -> {
            authManager.getGoogleAuthIntent(this);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                authManager.signIn(credential, this::handleResult);
            } else {
                generateToastMessage("Google sign in error");
            }
        }
    }

}
