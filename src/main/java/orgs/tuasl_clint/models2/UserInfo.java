package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;

public class UserInfo {
    private int user_id;
    private String phone;
    private String password;
    private int isEnabled;
    public static UserInfo userInfo = new UserInfo();

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public UserInfo(String phone, String password) {
        this.phone = phone;
        this.password = password;
    }

    public UserInfo() {
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public boolean isEmpty(){
        return  (phone == null || password == null || phone.isEmpty() || password.isEmpty() || phone.isBlank() || password.isBlank());
    }
    public boolean save() throws SQLException{
        if(this.isEmpty()){
            return false;
        }
        try(Connection conn = DatabaseConnectionSQLite.getInstance().getConnection()){
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO userinfo( phone_number,password,is_enable) VALUES(?,?,?);", Statement.RETURN_GENERATED_KEYS);
            psmt.setString(1,this.phone);
            psmt.setString(2,this.password);
            psmt.setInt(3,this.isEnabled);
            System.out.print("----------From User Info: Trying to save User Login Info To databaase and The result Is : ");
            if (psmt.executeUpdate() > 0){
                System.out.println(true);
                ResultSet rs = psmt.getGeneratedKeys();
                if(rs.next())
                    this.user_id = rs.getInt(0);
                return true;
            }else
            {
                System.out.println(false);
            }
            return false;
        }
    }
    public boolean update()throws  SQLException{
        if(this.isEmpty() || this.user_id <= 0)
            return false;
        System.out.print("--------From UserInfo Class : Trying to Update User Data And Result is : ");
        try(PreparedStatement psmt = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement("UPDATE userinfo SET phone_number = ? , password = ? , is_enable = ? WHERE id = ?")){
            psmt.setString(1,this.phone);
            psmt.setString(2,this.password);
            psmt.setInt(3,this.isEnabled);
            psmt.setInt(4,this.user_id);
            boolean success = psmt.executeUpdate() > 0;
            System.out.println(success);
            return success;
        }
    }

    public boolean getFirst() throws SQLException{
        System.out.print("----------From UserInfo Class : Trying to get the First Row in the Table");
        try(PreparedStatement psmt = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement("SELECT * FROM userinfo LIMIT 2;")) {
            ResultSet rs= psmt.executeQuery();
            boolean result = rs.next();
            System.out.println(" And Result Is :"+result);
            if(result){
                this.user_id = rs.getInt("id");
                this.phone = rs.getString("phone_number");
                this.password = rs.getString("password");
                this.isEnabled = rs.getInt("is_enable");
                System.out.println("______________ Current User from Database Is : "+ this.toString());
                return true;
            }
        }
        return false;
    }
    public static boolean DeleteAll()throws SQLException{
        System.out.print("----------From UserInfo Class : static method delete all is working...");
        try(PreparedStatement psmt = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement("Delete FROM userinfo where id > 0;")) {
            boolean result = psmt.executeUpdate() > 0;
            System.out.println(" And Result Is :"+result);
            return result;
        }
    }

    @Override
    public String toString() {
        return "UserInfo{user_id : "+this.user_id+" , Phone : "+this.phone+" , Password : "+password+", enabled : "+(this.isEnabled > 0)+"}";
    }
}
