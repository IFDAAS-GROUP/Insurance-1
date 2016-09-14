package lyit.oscar.insurance;

import java.sql.Connection;
//import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;

/**
 * This class's contains all of the SQL Statements needed to run the program
 * @author Melissa, Daniela, Aodhain, and Fred
 */
public class SqlStatements
{
	/**
	 * Inserts a User and their Policy into the database
	 * @param aUser		The user to be inserted
	 * @param aPolicy	The policy to be inserted
	 */
	public void insertCust(User aUser, PolicyDetails aPolicy)
	{
		PreparedStatement statement = null;
		Connection connection = null;
		ResultSet rs = null;


		try {
			connection = MYSQLconnect.getConnection();
			String addCust = "INSERT INTO person_table (title,First_name,Surname,Date_of_birth,"+
					"telephone,gender,email,address) VALUES (?,?,?,?,?,?,?,?)";

			statement = connection.prepareStatement(addCust, PreparedStatement.RETURN_GENERATED_KEYS);
			statement.setString(1, aUser.getTitle());
			statement.setString(2, aUser.getFname());
			statement.setString(3, aUser.getlname());
			statement.setDate(4, Date.valueOf(aUser.getDOB()));
			statement.setString(5, aUser.getPhone());
			statement.setString(6, aUser.getGender());
			statement.setString(7, aUser.getEmail());
			statement.setString(8, aUser.getAddress());

			int insertRows = statement.executeUpdate();
			if (insertRows > 0) {
				System.out.println("\nPolicy accepted");
			}
			
			rs = statement.getGeneratedKeys();
			if(rs != null && rs.next());
			{
				aUser.setPersonID(rs.getInt(1));
				System.out.println("Your ID is : "+rs.getInt(1));
			}

			String addPolicy = "INSERT INTO policy_table (idpolicy_table,cover_start,cover_finish,payment_type,"+
					"cost,idperson_table) VALUES (?,?,?,?,?,LAST_INSERT_ID())";

			statement = connection.prepareStatement(addPolicy);
			statement.setInt(1, aPolicy.getPolicyID());
			statement.setDate(2, Date.valueOf(aPolicy.getPolicyStart()));
			statement.setDate(3, Date.valueOf(aPolicy.getPolicyEnd()));
			statement.setString(4, aPolicy.getPaymentType());
			statement.setDouble(5, aPolicy.getPolicyCost());
			
			int insertRows1 = statement.executeUpdate();
			if (insertRows1 > 0) {
				System.out.println("\nPolicy accepted and Activated!\nPolicy number is: "+aPolicy.getPolicyID());
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}	
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
						e.printStackTrace();
				}			
			}
		}			
	}

	/**
	 * Deletes a customer based on the policy number given
	 * @param policyNo	The policy number of the person to be deleted
	 * @return			
	 */
	public int delCust(String policyNo)
	{
		Connection connection = null;
		PreparedStatement statement = null;
		int deleteRows = 0;
		


		try {
			connection = MYSQLconnect.getConnection();
			String addCust = "DELETE FROM person_table WHERE idperson_table IN (SELECT idperson_table FROM policy_table WHERE idpolicy_table= ?)";
			statement = connection.prepareStatement(addCust);
			statement.setInt(1, Integer.parseInt(policyNo));
			deleteRows = statement.executeUpdate();
			if(deleteRows > 0)
			{
				System.out.println("DELETED:   policy number: "+ policyNo);
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}		
		return deleteRows;
	}

	/**
	 * This policy returns the user based on their policy number
	 * @param policyNo	The policy number of the desired user
	 * @return			The user details connected to the supplied policy number
	 */
	public User selectCust(String policyNo)
	{
		ResultSet selectResult = null;
		Connection connection = null;
		PreparedStatement statement = null;
		User newUser = new User();


		try {
			connection = MYSQLconnect.getConnection();
			String addCust = ("SELECT * FROM person_table, policy_table WHERE policy_table.idpolicy_table = ?");
			statement = connection.prepareStatement(addCust);
			//statement.setInt(1, Integer.parseInt(aUser.getPolicyNo()));
			statement.setInt(1, Integer.parseInt(policyNo));
			selectResult = statement.executeQuery();
			while (selectResult.next()) {

				newUser.setPersonID(selectResult.getInt("idperson_table"));
				newUser.setTitle(selectResult.getString("title"));
				newUser.setFname(selectResult.getString("first_Name"));
				newUser.setlname(selectResult.getString("surname"));
				newUser.setDOB((selectResult.getDate("date_of_birth")).toLocalDate());
				newUser.setPhone(selectResult.getString("telephone"));
				newUser.setGender(selectResult.getString("gender"));
				newUser.setEmail(selectResult.getString("email"));
				newUser.setAddress(selectResult.getString("address"));
				newUser.setPolicyNo(Integer.toString(selectResult.getInt("idpolicy_table")));

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return newUser;
	}

}
