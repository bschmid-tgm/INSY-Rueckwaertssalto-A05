package JDBC;
import java.sql.*;
import java.util.logging.*;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	
	public String pkey;
	public String fkey;
	public static String[] arr;
	
		public static void main(String[] args) throws Exception{
				
			String url = "jdbc:mysql://localhost/"; 
			String dbName = "premiere";
			String userName = "root"; 
			String password = "DzDh741852963!";
			String sortierung = "";
			String trennzeichen = ";";
			String tabellenname = "";
			//String query = "SELECT * FROM genre";
			arr = null;			
				
				Connection conn = null;
				Statement st = null;
				ResultSet rs = null;
				PreparedStatement pst = null;
				
				try
				{
					//JDBC Treiber laden
					Class.forName("com.mysql.jdbc.Driver");
					System.out.println("JDBC Treiber geladen");
				}
				catch (Exception ex)
				{
					System.err.println("Fehler beim Treiber laden");
				}
				
				try{
					
				conn = DriverManager.getConnection(url+dbName,userName,password);
				}
				catch (SQLException ex)
				{
					System.err.println("Verbindung konnte nicht hergestellt werden");
				} 
				
				try
				{
					
					
					//Inhalt des Query in ResultSetMetaData speichern
					
					//rs = (ResultSet) rs.getMetaData();

					st = conn.prepareStatement("SELECT * FROM genre");
		            rs = st.executeQuery("SELECT * FROM sender");
					
					
					ResultSetMetaData rsMetaData = rs.getMetaData();
					DatabaseMetaData dm = conn.getMetaData( );
					ResultSet PrimareKeyResultSet = null;
					ResultSet ForgeinKeyResultSet = null;
					
					
					//Inhalt aus ResultSetMetaData in die Variable Columns speichern und ausgeben
					
					int Columns = rsMetaData.getColumnCount();
					
					System.out.println(Columns);
					//Schleife 1 gibt die SpaltenNamen aus
					
					//Schleife 2 und 3 gibt den Inhalt der Tabelle aus
					
					System.out.println("Relationshipmodel: ");
					System.out.println("");
					
					
					rs = dm.getTables(null, null, "%", null);
					System.out.println("");
					
					
					
				    	
					while (rs.next()) {
						System.out.println("");
					        
						String em =  rs.getString("TABLE_NAME");
					    arr = em.split("\n");
					    for (int i =0; i < arr.length; i++){
					    	
					    System.out.print(arr[i]+ " ( ");
					    
					    if(arr[i].equals("sender")){
					        
					        	
								  PrimareKeyResultSet = dm.getExportedKeys( "" , "" , arr[i] );
								  ForgeinKeyResultSet = dm.getExportedKeys(conn.getCatalog(), null, arr[i]);
								  
								  while( PrimareKeyResultSet.next( )) 
									{ 
										String pkey = PrimareKeyResultSet.getString("PKCOLUMN_NAME");
										System.out.print("<pk> " + pkey+" , ");
										
										while(ForgeinKeyResultSet.next()){
										
									  
									  String fkey = ForgeinKeyResultSet.getString("FKCOLUMN_NAME");
									  
									  if(fkey==pkey){
										  System.out.print("<fk> ");
									  }else{
										  System.out.print("<fk> " + fkey+" , ");
									  }
									  
									  for ( int y = 1; y <= Columns; y++)
										  
									  {
										  
										  
										  
										  //String columnName = rsMetaData.getColumnName(i);
										  
										 // if( columnName == pkey){
											  
											// System.out.println("asfdsfgds");
										 //}
										  
										// else{
											 
											System.out.print(rsMetaData.getColumnName(y)+ ", ");
											 
										}
									}
									  System.out.print(")");
									
									}
							  }
					    }
					    }
					}
				catch (SQLException ex)
				{
					System.err.println("Fehler");
				}
}
}
