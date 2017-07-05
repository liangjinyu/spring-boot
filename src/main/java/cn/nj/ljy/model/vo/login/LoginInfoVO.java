package cn.nj.ljy.model.vo.login;

import java.io.Serializable;

public class LoginInfoVO implements Serializable {

    /**
     */
    private static final long serialVersionUID = -3673701115463220332L;

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
