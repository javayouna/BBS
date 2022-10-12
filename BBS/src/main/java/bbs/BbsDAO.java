package bbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BbsDAO {
	private Connection conn;
	private ResultSet rs;
	

	public BbsDAO() {
		try {
			//String dbURL="jdbc:mysql://umj7-009.cafe24.com/moko2";
			String dbURL="jdbc:mysql://localhost/moko2";
			String dbID="moko2";
		    String dbPassword="spdlxlqm2@";
		    Class.forName("com.mysql.jdbc.Driver");
		    conn=DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
	}
	//현재 시간
	public String getDate() {
		String sql = "select now()";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		}catch(Exception e) {
			System.out.println(e);
		}
		
		return ""; //데이터 베이스 오류
	}
	
	public int getNext() {
		String sql = "select bbsID from BBS order by bbsID desc";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			rs=pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) +1;
			}
			return 1; //첫 번째 게시글일 경우
		
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
		
		return -1; //데이터 베이스 오류
	}
	
	public int write(String bbsTitle, String userID, String bbsContent) {
		String sql ="insert into BBS values (?,?,?,?,?,?)";
				try {
					PreparedStatement pstmt=conn.prepareStatement(sql);
					pstmt.setInt(1, getNext());
					pstmt.setString(2, bbsTitle);
					pstmt.setString(3, userID);
					pstmt.setString(4, getDate());
					pstmt.setString(5, bbsContent);
					pstmt.setInt(6, 1);
					return pstmt.executeUpdate();
					
				}catch(Exception e) {
					e.printStackTrace();
					System.out.println(e);
				}
				
				return -1; //데이터 베이스 오류
	}
	//게시판 페이징
	public ArrayList<Bbs> getList(int pageNumber){
		 String sql = "select * from BBS where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
		 ArrayList<Bbs> list = new ArrayList<Bbs>();
		 try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1, getNext()-(pageNumber-1)*10);
				rs=pstmt.executeQuery();
				while(rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					list.add(bbs);				
				}	
				System.out.println(pstmt);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
			
			return list; 
	}
	//특정한 페이지가 존재하는지 확인
	public boolean nextPage(int pageNumber) {
		String sql = "select * from BBS where bbsID < ? and bbsAvailable =1";
		 try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1, getNext()-(pageNumber-1)*10);
				rs=pstmt.executeQuery();
				if(rs.next()) {
					System.out.println(pstmt);
					return true;//페이지 넘기기
				}	
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
			
			return false; //게시글 못넘기기
	}
	//게시판 view
	public Bbs getBbs(int bbsID) {
		String sql = "select * from BBS where bbsID = ?";
		 try {
				PreparedStatement pstmt=conn.prepareStatement(sql);
				pstmt.setInt(1,bbsID);
				rs=pstmt.executeQuery();
				if(rs.next()) {
					Bbs bbs = new Bbs();
					bbs.setBbsID(rs.getInt(1));
					bbs.setBbsTitle(rs.getString(2));
					bbs.setUserID(rs.getString(3));
					bbs.setBbsDate(rs.getString(4));
					bbs.setBbsContent(rs.getString(5));
					bbs.setBbsAvailable(rs.getInt(6));
					return bbs;
				}	
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println(e);
			}
			
			return null; 
	}
	
	
	
	//게시판 update
	public int update(int bbsID, String bbsTitle, String bbsContent) {
		String sql ="update BBS set bbsTitle = ?, bbsContent= ? where bbsID= ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setString(1, bbsTitle);
			pstmt.setString(2, bbsContent);
			pstmt.setInt(3, bbsID);
			return pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return -1;//오류
	}
	
	
	//게시판 delete
	public int delete(int bbsID) {
		String sql="update BBS set bbsAvailable = 0 where bbsID= ?";
		try {
			PreparedStatement pstmt=conn.prepareStatement(sql);
			pstmt.setInt(1, bbsID);
			return pstmt.executeUpdate();
			
		}catch(Exception e) {
			e.printStackTrace();
			
		}
		return -1; //데이터베이스오류
		
	}
	
}
		

