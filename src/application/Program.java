package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {

		Connection conn = null;
		Statement st = null;
		try {
			conn = DB.getConnection();

			conn.setAutoCommit(false);// It's not to confirm the affirmations automatically

			st = conn.createStatement();

//============== UPDATE SALARY ==============

			int rows1 = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");

//================= demonstrating transaction error ================
/*
			int x = 1;
			if (x < 2) {
				throw new SQLException("FAKE ERROR");
			}
*/
			
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 3090 WHERE DepartmentId = 2");

			conn.commit();// confirm transaction

			System.out.println("rows1 " + rows1);
			System.out.println("rows2 " + rows2);

//================ exception handling ================

		} catch (SQLException e) {
			try {
				conn.rollback();//(can generate an exception, use try catch) back database updates
				throw new DbException("Transaction rolled back! Caused by: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error trying to rollback! Caused by: " + e1.getMessage());
			}
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}