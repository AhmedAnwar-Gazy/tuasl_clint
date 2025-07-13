package orgs.tuasl_clint.models2;

import orgs.tuasl_clint.utils.DatabaseConnectionSQLite;

import java.sql.*;

public class UserInfo {
    private int user_id;
    private String phone;
    private String password;

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
            PreparedStatement psmt = conn.prepareStatement("INSERT INTO userinfo( phone_number,password) VALUES(?,?) ;", Statement.RETURN_GENERATED_KEYS);
            psmt.setString(1,this.phone);
            psmt.setString(2,this.password);
            if (psmt.executeUpdate() > 0){
                ResultSet rs = psmt.getGeneratedKeys();
                if(rs.next())
                    this.user_id = rs.getInt(0);
                return true;
            }
            return false;
        }
    }
    public boolean update()throws  SQLException{
        if(this.isEmpty() || this.user_id <= 0)
            return false;
        try(PreparedStatement psmt = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement("UPDATE user_info SET phone_number = ? , password = ? WHERE id = ?")){
            psmt.setString(1,this.phone);
            psmt.setString(2,this.password);
            psmt.setInt(3,this.user_id);
            return psmt.executeUpdate() > 0;
        }
    }

    public boolean getFirst() throws SQLException{
        try(PreparedStatement psmt = DatabaseConnectionSQLite.getInstance().getConnection().prepareStatement("SELECT * FROM userinfo LIMIT 1;")) {
            ResultSet rs= psmt.executeQuery();
            if(rs.next()){
                this.user_id = rs.getInt(1);
                this.phone = rs.getString(2);
                this.password = rs.getString(3);
                return true;
            }
        }
        return false;
    }
}
