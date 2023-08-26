package Transaction;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Transaction {

	public static void main(String[] args) {
		Connection con = null;
		try {
			// Connection con = ConnectionDetails.getConnection();
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/trans_jdbc", "root", "root");

			con.setAutoCommit(false);

//			PreparedStatement psmt=con.prepareStatement("insert trans_jdbc.trn_table values values(?,?)");
//			psmt.setString(1, "1000");
//			psmt.setString(2, "2000");
//			psmt.executeUpdate();
			System.out.println(" money is inserted...");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter From Account Id :- ");
			int f_id = Integer.parseInt(br.readLine());

			System.out.println("Enter To Account Id :- ");
			int t_id = Integer.parseInt(br.readLine());

			System.out.println("Enter The amount :- ");
			int amt = Integer.parseInt(br.readLine());

			PreparedStatement psmt1 = con.prepareStatement("update trn_table set amount=amount+? where id=? ");
			PreparedStatement psmt2 = con.prepareStatement("update trn_table set amount=amount-? where id=?");

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select amount from trn_table where id=" + f_id);

			int avlamt = 0;
			if (rs.next()) {
				avlamt = rs.getInt("amount");
			}
			if (avlamt > amt) {
				psmt1.setInt(1, amt);
				psmt1.setInt(2, t_id);
				psmt1.executeUpdate();
				

				//int e = 4 / 0; // Exception

				psmt2.setInt(1, amt);
				psmt2.setInt(2, f_id);
				psmt2.executeUpdate();
				System.out.println();
			
				con.commit();
			 
			} else {
				System.out.println("Insufficient Bal.. In Account sry");
			}
		} catch (Exception e) {
			try {
				System.out.println(e);
				System.out.println("Rolling Back");
				con.rollback();
			} catch (Exception e1) {
				System.out.println(e1);
			}
		} finally {
			try {
				con.close();
			} catch (Exception e2) {
				System.out.println(e2);
			}
		}

	}

}
