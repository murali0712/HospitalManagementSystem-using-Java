package HospManagSys;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




public class Doctor {


	private Connection conn;
	
	public Doctor(Connection conn) {
		this.conn=conn;
	}
	
	
	
	public void viewDoctor() {
		String query= "select * from doctors";
		
		try {
			PreparedStatement ps= conn.prepareStatement(query);
			ResultSet rs= ps.executeQuery();
			System.out.println("Doctors : ");
			System.out.println("+-----------+------------------+--------+----------");
			System.out.println("|Doctor ID | Name              |Specialization    |");
			while(rs.next()) {
				int id= rs.getInt("id");
				String name= rs.getString("dname");
				
				String spec= rs.getString("specialization");
				
				System.out.printf("|%-12s|%-18s|%-13s|\n",id,name,spec);
				System.out.println("+-----------+------------------+--------+----------");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getDoctorById(int id) {
		String query= "select * from doctors where id=?";
		try {
			PreparedStatement ps= conn.prepareStatement(query);
			ps.setInt(1,id);
			ResultSet rs= ps.executeQuery();
			if(rs.next()) {
				return true;
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
