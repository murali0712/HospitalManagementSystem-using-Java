package HospManagSys;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
	private Connection conn;
	private Scanner scan;
	
	public Patient(Connection conn,Scanner scan) {
		this.conn=conn;
		this.scan=scan;
	}
	
	public void addPatient() {
		System.out.println("Enter Patient Name: ");
		String name= scan.next();
		
		System.out.println("Enter Patient Age: ");
		int age= scan.nextInt();
		
		System.out.println("Enter Patient Gender: ");
		String gend= scan.next();
		
		try {
			String query= "INSERT INTO patients(name,age,gender) VALUES(?,?,?)";
			
			PreparedStatement ps= conn.prepareStatement(query);
			ps.setString(1,name);
			ps.setInt(2,age);
			ps.setString(3, gend);
			
			int affectrows= ps.executeUpdate();
			if(affectrows>0) {
				System.out.println("Patient Added successfully");
			}
			else {
				System.out.println("Failed to add patient!!");
			}
		}
		catch(SQLException e) {
				e.printStackTrace(); 
		}
	}
	
	public void viewPatient() {
		String query= "select * from patients";
		
		try {
			PreparedStatement ps= conn.prepareStatement(query);
			ResultSet rs= ps.executeQuery();
			System.out.println("Patients: ");
			System.out.println("+-----------+------------------+--------+-------------");
			System.out.println("|Patient ID | Name             | Age    | Gender     |");
			while(rs.next()) {
				int id= rs.getInt("id");
				String name= rs.getString("name");
				int age= rs.getInt("age");
				String gend= rs.getString("gender");
				
				System.out.printf("|%-12s|%-18s|%-8s|%-13s|\n",id,name,age,gend);
				System.out.println("+-----------+------------------+--------+-------------");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean getPatientById(int id) {
		String query= "select * from patients where id=?";
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
