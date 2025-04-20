package usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import usermanagement.model.User;

public class UserDAO {
	private String jdbcURL = "jdbc:mysql://localhost:3306/usermanagement";
	private String Username = "root";
	private String Password = "";
	
	
	// CRUD database operations for users's table 
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users (name,email,country) values (?,?,?)";
	
	private static final String SELECT_USERS_BY_ID = "SELECT * from users where id =?";
	private static final String SELECT_ALL_USERS = "SELECT * from users";
	private static final String DELETE_USERS_SQL = "DELETE from users where id = ? ";
	private static final String UPDATE_USERS_SQL = "UPDATE users set name =?, email=?, country = ? where id = ?"; 
						
	 
	
	//get Connection 
	
	protected Connection getConnection() throws SQLException {
		try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(jdbcURL,Username,Password);
		
	}
	
	//Create or insert user 
	
	public void insertUser(User user) throws SQLException{
		try(Connection conn = getConnection();
				PreparedStatement pstmt = conn.prepareStatement(INSERT_USERS_SQL)) {
			pstmt.setString(1, user.getName());
			pstmt.setString(2, user.getEmail());
			pstmt.setString(3, user.getCountry());
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
		//update user
		//select user by id 
		//select users 
		//delete user
	
	
}
