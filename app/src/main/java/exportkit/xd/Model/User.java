package exportkit.xd.Model;

public class User {
    int id ;
    String name ;
    String username ;
    String email ;
    String phoneNumber ;
    String password ;
    String gender ;
    String avatar;

    public User(){}
    public User(String name, String username, String email, String phoneNumber, String password ,String gender) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.gender = gender ;

    }

    public User(String name, String username, String email, String phoneNumber, String password) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;

    }

    public User(int id , String name, String username, String email) {
        this.id= id ;
        this.name = name;
        this.username = username;
        this.email = email;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getAvatar() {
        return avatar;
    }
}
