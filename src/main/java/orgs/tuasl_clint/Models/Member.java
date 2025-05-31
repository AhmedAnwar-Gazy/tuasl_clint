package orgs.tuasl_clint.Models;

public class Member {

    private  String name;
    private  String profilepicture;
    private  String rule;
    private Status status;

    public Member(String name, String profilepicture, String rule, Status status) {
        this.name = name;
        this.profilepicture = profilepicture;
        this.rule = rule;

    }

    public Member() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Status getStatus() {
        return status;
    }


}
