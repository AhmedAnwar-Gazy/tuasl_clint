package orgs.clint_pages.Models;

import java.util.ArrayList;

public class Chamnel {

    private  String profilepicture;
    private  String bio;
    private ArrayList<Member> members = new ArrayList<Member>() ;
    private  String content;
    private  ArrayList<Message> messages = new ArrayList<Message>();

    public Chamnel(String profilepicture, String bio, ArrayList<Member> members, String content, ArrayList<Message> messages) {
        this.profilepicture = profilepicture;
        this.bio = bio;
        this.members = members;
        this.content = content;
        this.messages = messages;

    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public String getBio() {
        return bio;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }
}
