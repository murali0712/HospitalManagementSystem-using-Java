package HospManagSys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HospitalManagementSystem {
	private static final String url="jdbc:mysql://localhost:3306/hospital";
	private static final String usrnm="root";
	private static final String pswd="mk123";
	
	public static void main(String args[]) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		Scanner sc=new Scanner(System.in);
		try {
			Connection conn= DriverManager.getConnection(url,usrnm,pswd);
			Patient pat=new Patient(conn,sc);
			Doctor dt= new Doctor(conn);
			
			while(true) {
				System.out.println("HOSPITAL MANAGEMENT SYSTEM ");
				System.out.println("1.Add Patient");
				System.out.println("2.View Patients");
				System.out.println("3.View Doctors");
				System.out.println("4.Book Appointment");
				System.out.println("5.Exit");
				
				System.out.println("Enter your Choice: ");
				int ch= sc.nextInt();
				
				switch(ch) {
				case 1: 
					pat.addPatient();
					System.out.println();
					break;
				
				case 2:
					pat.viewPatient();
					System.out.println();
					break;
				
				case 3:
					dt.viewDoctor();
					System.out.println();
					break;
				
				case 4:
					bookAppointment(pat,dt,conn,sc);
					System.out.println();
					break;
					
				case 5: System.out.println("THANK YOU FOR USING OUR HOSPITAL MANAGEMENT SYSTEM");
					System.exit(0);
				
				default: 
					System.out.println("Enter Valid Choice!!");
					break;
				}
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void bookAppointment(Patient p,Doctor d,Connection conn,Scanner sc) {
		System.out.println("Enter Patient Id: ");
		int pid= sc.nextInt();
		System.out.println("Enter Doctor Id: ");
		int did= sc.nextInt();
		System.out.println("Enter Appointment date(YYYY-MM-DD): ");
		String appdate= sc.next();
		if(p.getPatientById(pid) && d.getDoctorById(did)) {
			if(checkDoctorAvailable(did,appdate,conn)) { 
				String appquery= "INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES(?,?,?)";
				try {
					PreparedStatement ps= conn.prepareStatement(appquery);
					ps.setInt(1, pid);
					ps.setInt(2, did);
					ps.setString(3,appdate);
					
					int rowsaff= ps.executeUpdate();
					if(rowsaff>0) {
						System.out.println("Appointment Booked");
					}
					else
						System.out.println("Failed to Book Appointment");
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
			}
			else {
				System.out.println("Doctor not available on this date!!");
			}
		}
		else {
			System.out.println("Either patient or doctor doesn't exist!!");
		}
	}
	
	
	public static boolean checkDoctorAvailable(int did, String date,Connection conn) {
		String query="SELECT COUNT(*) FROM appointments WHERE doctor_id=? AND appointment_date=?";
		try {
		PreparedStatement ps= conn.prepareStatement(query);
		ps.setInt(1, did);
		ps.setString(2, date);
		ResultSet rs= ps.executeQuery();
		if(rs.next()) {
			int count= rs.getInt(1);
			if(count ==0) {
				return true;
			}
			else 
				return false;
		}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
