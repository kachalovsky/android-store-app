package fit.bstu.lab_05_06.auth;

import com.google.firebase.auth.FirebaseUser;

/**
 * Created by andre on 29.10.2017.
 */

public interface ISignIn {
    void onSignIn(FirebaseUser user);
}
