package orgs.clint_pages.Models;

import javax.net.ssl.SSLEngineResult;
import java.util.ArrayList;

public class User {

    private  String username;
    private  String bio;
    private  String fullname;
    private  String email;
    private  String profilepicture;
    private Status status ;

    private ArrayList<Chamnel>  chamnels = new ArrayList<Chamnel>();

    public User() {
    }

    public User(String username, String bio, String fullname, String email, String profilepicture, Status status, ArrayList<Chamnel> chamnels) {
        this.username = username;
        this.bio = bio;
        this.fullname = fullname;
        this.email = email;
        this.profilepicture = profilepicture;

        this.chamnels = chamnels;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Chamnel> getChamnels() {
        return chamnels;
    }

    public void setChamnels(ArrayList<Chamnel> chamnels) {
        this.chamnels = chamnels;
    }
}
