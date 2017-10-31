package fit.bstu.lab_05_06.auth;

/**
 * Created by andre on 29.10.2017.
 */

public class EmailPassCredentials {
    private String email;
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public EmailPassCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }


}
