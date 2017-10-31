package fit.bstu.lab_05_06.auth;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * Created by andre on 29.10.2017.
 */

public interface IComplete {
    void onComplete(Task<AuthResult> result);
}
