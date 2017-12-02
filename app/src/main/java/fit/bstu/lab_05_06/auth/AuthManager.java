package fit.bstu.lab_05_06.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import fit.bstu.lab_05_06.Constants;
import fit.bstu.lab_05_06.R;

/**
 * Created by andre on 27.10.2017.
 */

public class AuthManager {
    private static final AuthManager instance = new AuthManager();
    public static AuthManager getInstance() {return instance;}
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ArrayList<ILogOut> logoutListeners = new ArrayList<>();
    private ArrayList<ISignIn> signInListeners = new ArrayList<>();

    private void notifySignIn(FirebaseUser user) {
        for (ISignIn listener: signInListeners) {
            listener.onSignIn(user);
        }
    }

    private void notifyLogOut() {
        for (ILogOut listener: logoutListeners) {
            listener.onLogout();
        }
    }

    private AuthManager() {
        //mAuth = FirebaseAuth.getInstance();
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                notifySignIn(user);
            } else {
                notifyLogOut();
            }
        };
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    public void initialize(Context context) {
        mGoogleApiClient = getGoogleApiClient(context);
    }

    public String getUserEmail() {
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) return user.getEmail();
        return null;
    }

    public boolean tryLogin() {
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            notifySignIn(user);
            return true;
        } else return false;
    }

    public void addSignInListener(ISignIn listener) {
        signInListeners.add(listener);
    }

    public void addLogOutListener(ILogOut listener) {
        logoutListeners.add(listener);
    }

    public void removeSignInListener(ISignIn listener) {
        signInListeners.remove(listener);
    }

    public void removeLogOutListener(ILogOut listener) {
        logoutListeners.remove(listener);
    }

    void signIn(AuthCredential credentials, IComplete completeClosure) {
        FirebaseAuth.getInstance().signInWithCredential(credentials).addOnCompleteListener(completeClosure::onComplete);
    }

    void signIn(EmailPassCredentials emailPassCredentials, IComplete completeClosure) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailPassCredentials.getEmail(), emailPassCredentials.getPassword()).addOnCompleteListener(completeClosure::onComplete);
    }

    void signUp(EmailPassCredentials emailPassCredentials, IComplete completeClosure) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailPassCredentials.getEmail(), emailPassCredentials.getPassword()).addOnCompleteListener(completeClosure::onComplete);
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();
    }

    void getGoogleAuthIntent(Activity activity) {
        activity.startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient), Constants.RC_SIGN_IN);
    }

    private GoogleApiClient getGoogleApiClient(Context context) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        return mGoogleApiClient;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
    }
}
