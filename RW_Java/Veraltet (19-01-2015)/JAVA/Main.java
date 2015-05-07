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
	
		public static void main(String[] args) throws Exception{
				
			String url = "jdbc:mysql://localhost/"; 
			String dbName = "premiere";
			String userName = "root"; 
			String password = "";
			String sortierung = "";
			String trennzeichen = ";";
			String tabellenname = "";
			String query = "SELECT * FROM sender;";
			
/**			
				CommandLineParser parser = new BasicParser();
				Options options = new Options();
				
				options.addOption("h", true, "jdbc:mysql://localhost/"); //Hostname
				options.addOption("u", true, "root"); //Benutzername
				options.addOption("p", true, ""); //Passwort
				options.addOption("d", true, "premiere"); //DB-Name
				options.addOption("s", true, "");
				options.addOption("r", true, "ASC"); // Sortierrichtung
				options.addOption("w", true, "");
				options.addOption("t", true, ";"); //Trennzeichen
				options.addOption("f", true, ""); //Kommagetrennte Liste
				options.addOption("o", true, ""); // Name der Ausgabedatei
				options.addOption("T", true, ""); // Tabellenname
				
				try{
					CommandLine cmd = parser.parse(options, args);
					
					url = cmd.getOptionValue("h");
					dbName = cmd.getOptionValue("d");
					userName = cmd.getOptionValue("u");
					password = cmd.getOptionValue("p");
					sortierung = cmd.getOptionValue("r");
					trennzeichen = cmd.getOptionValue("t");
					tabellenname = cmd.getOptionValue("T");
					
				}
				catch(ParseException e){
					e.printStackTrace();
				}
				
**/			
				
				Connection conn = null;
				Statement st = null;
				ResultSet rs = null;
				
				
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
					
					st = conn.createStatement();
					rs = st.executeQuery(query);
					
					//Inhalt des Query in ResultSetMetaData speichern
					
					//rs = (ResultSet) rs.getMetaData();
					
					ResultSetMetaData rsMetaData = rs.getMetaData();
					ResultSet resultSet = conn.getMetaData().getCatalogs();
					DatabaseMetaData dm = conn.getMetaData( );
					ResultSet PrimareKeyResultSet = dm.getExportedKeys( "" , "" , "sender" );
					ResultSet ForgeinKeyResultSet = dm.getExportedKeys(conn.getCatalog(), null, "sender");
					
					
					//Inhalt aus ResultSetMetaData in die Variable Columns speichern und ausgeben
					
					int Columns = rsMetaData.getColumnCount();
					
					System.out.println(Columns);
					//Schleife 1 gibt die SpaltenNamen aus
					
					//Schleife 2 und 3 gibt den Inhalt der Tabelle aus
					
					System.out.println("Relationshipmodel: ");
					System.out.println("");
					System.out.print(rsMetaData.getTableName(1)+"( ");
						
						
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
							  
							  for ( int i = 1; i <= Columns; i++)
								  
							  {
								  
								  //String columnName = rsMetaData.getColumnName(i);
								  
								 // if( columnName == pkey){
									  
									// System.out.println("asfdsfgds");
								 //}
								  
								// else{
									 
									System.out.print(rsMetaData.getColumnName(i)+ ", ");
									 
								}
							}
							  System.out.print(")");
							
							}
							
					/*
					while (rs.next())
					{
						System.out.println();
						
						for ( int i = 1; i <= Columns; i++)
						{
							//System.out.print(rs.getString(i)+" , ");
							System.out.print(rsMetaData.getColumnLabel(i));
							System.out.print(rsMetaData.getTableName(i));
						}
					}
					*/
				}
				catch (SQLException ex)
				{
					System.err.println("Fehler");
				}
			}

	}
