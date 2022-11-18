package Classe;

import java.io.Serializable;

public class Utilisateur  implements Serializable {
    private String _nomUser;
    private String _password;

    public String get_nomUser() {
        return _nomUser;
    }
    public void set_nomUser(String nomUser) {
        this._nomUser = nomUser;
    }

    public String get_password() {
        return _password;
    }
    public void set_password(String password) {
        this._password = password;
    }

}
