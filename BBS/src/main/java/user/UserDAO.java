package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
	
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	

	public UserDAO() {
		try {
			//String dbURL="jdbc:mysql://umj7-009.cafe24.com/moko2";
			String dbURL="jdbc:mysql://localhost/moko2";
			String dbID="moko2";
		    String dbPassword="spdlxlqm2@";
		    Class.forName("com.mysql.jdbc.Driver");
		    conn=DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch(Exception e) {
			System.out.println(e);
		}
		
	}
	
	
	public int login(String userID, String userPassword) {
		String sql="select userPassword from user where userID=?";
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1,userID);
			rs=pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; //로그인성공
				}
				else
					return 0; //비밀번호 불일치
					
			}
			return -1; //아이디 없음
			
		}catch(Exception e) {
			System.out.println(e);
		}
		return -2; //데이터베이스 오류
	}
	
	public int join(User user) {
		String sql="insert into user values (?,?,?,?,?)";
		
		try {
			pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserID());
			pstmt.setString(2, user.getUserPassword());
			pstmt.setString(3, user.getUserName());
			pstmt.setString(4, user.getUserGender());
			pstmt.setString(5, user.getUserEmail());
			return pstmt.executeUpdate();
		}catch(Exception e) {
			System.out.println(e);
		}
		return -1; //데이터베이스 오류
		
	}
}
