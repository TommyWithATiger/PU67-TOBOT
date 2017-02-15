package users;

import javax.persistence.*;

@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String username;
    private String email;
    private String hashword;
    private String salt;

    protected User() {
    }

    protected User(String username, String email, String hashword, String salt) {
        this.username = username;
        this.email = email;
        this.hashword = hashword;
        this.salt = salt;
    }

    protected User(int id, String username, String email, String hashword, String salt) {
      this(username, email, hashword, salt);
      this.id = id;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    public String getHashword() {
        return hashword;
    }

    void setHashword(String hash) {
        this.hashword = hash;
    }

    public String getSalt() {
        return salt;
    }

    void setSalt(String salt) {
        this.salt = salt;
    }
}
