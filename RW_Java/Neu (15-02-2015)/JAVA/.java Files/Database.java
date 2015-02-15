package rw;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


import com.mysql.jdbc.Statement;

/**
 * Die Controller Klasse verbindet alle Klassen miteinander. 
 * Es wird eine Verbindung zur Datenbank hergestellt und nach dem input vom User entweder ein RM oder ein ERD (mittels Graphviz) erstellt
 * Das RM wird in ein einfaches Textfile geschrieben (<pk> für PrimaryKey & <fk> für ForighnKey)
 * 
 * @author *Bernhard Schmid* & Raphael Simsek
 * @version 26-01-2015 bis 16-02-2015
 */

public class Database {

	// Attribute fuer die DB Connection sowie der Graphviz Pfad
	
	String homed;
	
	
	String graphvizpath="", 
		   passwort = "",
		   db_server = "",
		   user = "",
		   databasename = "",
		   output="",
		   errors = "";
	
	//Alle Tabellen der Hashtable
	
	Hashtable<String, Table> db_Tables = new Hashtable<String, Table>();
	

/**
 * Der Konstruktor ist in unserem Fall die Schaltzentrale welche bestimmt welches File generiert wird
 * @param arg
 */
	
	public Database(String arg[]) {

		checkInput(arg);
		
		Connection conn = databaseConnection();
		getMetaData(conn);

		if(output.equals("er"))
			ERD(graphvizpath);
		
		else if(output.equals("rm_txt"))
			RM_TXT(graphvizpath);
		
		
		/*
		 * Die Methode RM_HTML kann verwendet werden allerdings nicht ohne Darstellungsfehler (Update 15-02-2015)
		 * --> Ergebnis wird 2 mal ausgegeben
		 */
		
		else if(output.equals("rm_html"))
			RM_HTML(graphvizpath);
		System.out.println(errors);
		

		if(output.equals("er")){
			homed = System.getProperty("user.dir");
			
			try {
	            String executable = graphvizpath + "\\dot.exe";
	            String output = homed +"ERD_"+databasename+ ".pdf";
	            String input = homed +"ERD_"+databasename+ ".dot";
	            
	            Runtime rt = Runtime.getRuntime();
	            Process proc = rt.exec(executable + " -Tpfd -O " + output + " " + input);
	            int exitVal = proc.waitFor();
	            System.out.println(exitVal);
	            if (exitVal == 1) {
	                System.out.println("Conversion finished");
	            }
	            System.out.println(executable + " -Tpdf -O " + output + " " + input);
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
		}
			
	}

	private void checkInput(String arg[]) {
		int i = 0;

		if (arg == null || arg.length == 0) {
			
			System.out
					.println("Bitte wiefolgt benutzen: -h <server> -u <user> -p <password> -d <databasename> -o <'rm_txt'|'rm_html (fehlerhaft)'|'er'> -g <graphviz-path>");
			System.exit(-1);
			
		}
		
		while (i < arg.length) {
			
			
			if (i >= arg.length - 1) {
				
				System.out.println("Bitte wiefolgt benutzen: -h <server> -u <user> -p <password> -d <databasename>  -o <'rm_txt'|'rm_html (fehlerhaft)'|'er'> -g <graphviz-path> Fehler1");
				System.exit(-1); // exit programm
				
			} else if (arg[i + 1].equals("-u") || arg[i + 1].equals("-p")|| arg[i + 1].equals("-d") || arg[i + 1].equals("-h")||arg[i+1].equals("-o")) {
				
				System.out.println("Bitte wiefolgt benutzen: -h <server> -u <user> -p <password> -d <databasename>  -o <'rm_txt'|'rm_html (fehlerhaft)'|'er'> -g <graphviz-path> Fehler2");
				
				i++;
				System.exit(-1); 

			} else {
				
				if (arg[i].equalsIgnoreCase("-h")) {
					
					db_server = (arg[i + 1]);
					i++; 
					
				} else if (arg[i].equalsIgnoreCase("-u")) {
					
					user = (arg[i + 1]);
					i++; 
					
				} else if (arg[i].equalsIgnoreCase("-d")) {
					
					databasename = (arg[i + 1]);
					i++; 
					
				} else if (arg[i].equalsIgnoreCase("-p")) {
					
					passwort = (arg[i + 1]);
					i++; 
					
				}
				
				else if (arg[i].equalsIgnoreCase("-g")) {
					
					graphvizpath = (arg[i + 1]);
					i++; 
				}
				
				else if (arg[i].equalsIgnoreCase("-o")) {
					if(arg[i+1].equals("rm_txt")||arg[i+1].equals("rm_html")||arg[i+1].equals("er")){
						output = (arg[i + 1]);
						i++; 
					}
					
					else{
						
						System.out
						.println("Bitte wiefolgt benutzen: -h <server> -u <user> -p <password> -d <databasename>  -o <'rm_txt'|'rm_html' '(fehlerhaft)'|'er'> -g <graphviz-path> Fehler3");
				
						System.exit(-1); 
					}
				}
			}
			++i;
		}

	}

	private Connection databaseConnection() {
		
		Connection conn=null;
		
		try {

			/*
			 * Eine neue Verbindung zu Datenbank herstellen (mit den zuvor vom user bekommenen Parametern)
			 */
			
			com.mysql.jdbc.jdbc2.optional.MysqlDataSource d = new com.mysql.jdbc.jdbc2.optional.MysqlDataSource();
			d.setServerName(db_server);
			d.setDatabaseName(databasename);
			d.setUser(user);
			d.setPassword(passwort);
			conn = d.getConnection();
			} catch (Exception e) {
				

			String error = e.toString();
			if (error != null && error.indexOf(':') >= 0) {
				if (error.contains(";"))
					error = error.substring(error.indexOf(':') + 1,error.indexOf(';')).trim();
				else if (error.contains("("))
					error = error.substring(error.indexOf(':') + 1,error.indexOf('(')).trim();
				else 
					error = error.substring(error.indexOf(':') + 1).trim();
			}
			
			errors = (error);
			
		}
		
		return conn;

	}

	/*
	 * Auslesen der Metadaten (Tabellenname, Spaltenname, ForgeinKEys, Primerykeys,..) mittels DatabaseMetaData 
	 * (diesmal nicht mittels ResultSetMetaData da man sich nicht auf die ResultSets beschraenken darf sonder die Gesamten Metadaten der Datenbank auslesen moechte)
	 */
	
	private void getMetaData(Connection conn) {
		try {

			Statement st = (Statement) conn.createStatement();
			DatabaseMetaData dbMetaData = conn.getMetaData();
			

			ResultSet tables = dbMetaData.getTables(databasename, null, null, null);
			while (tables.next()) {
				String table_name = tables.getString("TABLE_NAME");
				Table t = new Table(table_name);
				
				
				ResultSet rs_attributes = dbMetaData.getColumns(databasename, null, table_name, null);
				while (rs_attributes.next()) {
					t.addAttribute(rs_attributes.getString("COLUMN_NAME"));
				}
				rs_attributes.close();


				Hashtable <String, Attribute> temp_attributes = t.getAttributes();
				ResultSet PrimareKeyResultSet = dbMetaData.getPrimaryKeys(databasename,
						null, table_name);
				while (PrimareKeyResultSet.next()) {
					temp_attributes.get(PrimareKeyResultSet.getString("COLUMN_NAME")).setPrimary(
							true);
				}
				PrimareKeyResultSet.close();
				
				db_Tables.put(table_name, t);
				
			}

			
			tables = dbMetaData.getTables(databasename, null, null, null);
			while (tables.next()) {
				String table_name = tables.getString("TABLE_NAME");
				ResultSet ForgeinKeyResultSet = dbMetaData.getExportedKeys(databasename,
						null, table_name);
				while (ForgeinKeyResultSet.next()) {
					
					
					db_Tables.get(ForgeinKeyResultSet.getString("FKTABLE_NAME"))
							.setForeignAttribute(
									ForgeinKeyResultSet.getString("FKCOLUMN_NAME"),
									ForgeinKeyResultSet.getString("PKTABLE_NAME"),
									ForgeinKeyResultSet.getString("PKCOLUMN_NAME"));
				}
			}

			st.close();
			tables.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("Fehler beim auslesen, db Connection ueberpruefen");
		}
	}

	/**
	 * Die ERD-Methode generiert das ERD.dot File welches dann von Graphviz in ein png umgewandelt wird
	 */
	
	private void ERD(String filename) {
		
		homed = System.getProperty("user.dir");
		filename = "ERD_"+databasename+ ".dot";
		Writer writer = null;
		try {
			
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename)));
			//System.out.println(filename);
			String s = "";
			
			/*
			 * Generieren des .dot Files
			 */
			
			s += "digraph G { overlap=false";
			
			Vector<String> stringvector = new Vector<String>();
			
			/*
			 * Die Tabellen hinzufeugen 
			 */
			
			for (Enumeration<Table> e = db_Tables.elements(); e.hasMoreElements();) {
				Table t_tamp = (e.nextElement());
				s += "\""
						+ t_tamp.getName()
						+ "\"[label=<<TABLE BORDER=\"0\" CELLBORDER=\"0\" CELLSPACING=\"0\" CELLPADDING=\"4\"><TR><TD><B>"
						+ t_tamp.getName() + "</B></TD></TR>";

				Hashtable<String, Attribute> x2 = t_tamp.getAttributes();
				
				for (Enumeration<Attribute> e2 = x2.elements(); e2.hasMoreElements();) {
					Attribute m = e2.nextElement();
					if (m.isPrimary()) {
						s += "<TR><TD><U>" + m.getAttributName()
								+ "</U></TD></TR>";
					}
				}
				
				for (Enumeration<Attribute> e2 = x2.elements(); e2.hasMoreElements();) {
					Attribute m = e2.nextElement();
					if (m.getFa() != null) {
						s += "<TR><TD><I>" + m.getAttributName()
								+ "</I></TD></TR>";
					}
				}
				
				/*
				 * Die Keys generieren (Primary & Forgein) 
				 */
				
				for (Enumeration<Attribute> e2 = x2.elements(); e2.hasMoreElements();) {
					Attribute m = e2.nextElement();
					if (m.getFa() == null && m.isPrimary() == false) {
						s += "<TR><TD>" + m.getAttributName() + "</TD></TR>";
					}
				}
				s += "</TABLE>> ,shape=box];";

				/*
				 * Die verschiedenen Relationen erstellen  
				 */
				
				for (Enumeration<Attribute> e2 = x2.elements(); e2.hasMoreElements();) {
					Attribute m = e2.nextElement();
					if (m.getFk() != null) {
						stringvector.addElement(("\"" + t_tamp.getName()+ "\" -> \"" + m.getFk() + "\" [arrowhead =\"none\" taillabel=\"(0,1)\" , headlabel=\"(0,*)\"];"));
						stringvector.addElement(t_tamp.getName()+"-"+ m.getFk());
					}
				}
			}
			
			/*
			 * Falls Relationen mehrere Male vorkommen bzw. verbunden sind werden die multiblen Relationen gelöscht 
			 * (es bliebt also nur eine uebrig) 
			 */
			
			for (int i = 0; i < stringvector.size(); ++i) {
				for (int j = 0; j < stringvector.size(); ++j) {
					if (stringvector.elementAt(i).equals(stringvector.elementAt(j))&& (i != j)) 
						stringvector.remove(j);
				}
			}
			
			/*
			 * Bei diesem Schritt werden alle Relationen hinzugefuegt (zum .dot File)
			 */
			
			for(int i=0; i<stringvector.size();++i){
				String temp[] = stringvector.elementAt(i).split("-");	 
				s+= "\""+"rel"+i+"\" [label=\""+"rel"+i+"\" shape=diamond];";
				s+="\""+temp[0]+"\" -> \""+"rel"+i+"\" [arrowhead =\"none\" taillabel=\"(0,1)\"];";
				s+="\""+temp[1]+"\" -> \""+"rel"+i+"\" [arrowhead =\"none\" taillabel=\"(0,*)\"];";
			} 
			
			s += "}";
			writer.write(s);
			writer.close();
			
		} catch (Exception e) {
			
			String error = e.toString();
			if (error != null && error.indexOf(':') >= 0) {
				if (error.contains(";"))
					error = error.substring(error.indexOf(':') + 1,error.indexOf(';')).trim();
				else if (error.contains("("))
					error = error.substring(error.indexOf(':') + 1,error.indexOf('(')).trim();
				else 
					error = error.substring(error.indexOf(':') + 1).trim();
			}
			errors = (error);
			
		} 
	}

	/**
	 * Diese Methode wie der Name offensichtlich schon sagt erzeugt ein RM basierend auf den Tabellen und Attributen die es bekommt
	 * Das entstandene RM wird hier in ein txt File gespeichert.
	 * Das entstandene RM.txt wird im selben Ordner wie das Jar File oder dem Java Projekt (in Eclipse) gespeichert.
	 * 
	 * @param filename homepath of the user
	 */
	
	private void RM_TXT(String filename) {
		
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename + "RM_rucksalt_" + databasename
							+ ".txt"), "utf-8"));
			
			String s = "";
			
			for (Enumeration<Table> e = db_Tables.elements(); e
					.hasMoreElements();) {
				Table t_tamp = (e.nextElement());
				s += t_tamp.getName() + "(";
				Hashtable<String, Attribute> x2 = t_tamp.getAttributes();
				
				for (Enumeration<Attribute> e2 = x2.elements(); e2.hasMoreElements();) {
					Attribute m = e2.nextElement();
					//ForgeinKey
					if (m.getFa() != null) 
						s += m.getFa() + ".";
					//PrimaryKey
					if (m.isPrimary()) 
						s += "<pk>";
					s += m.getAttributName();
					if (m.isPrimary()) 
						s += "";
					//Mehrere Elemente --> Nach jedem Element wird ein , gesetzt
					if (e2.hasMoreElements()) 
						s += ",";
				}
				s += ")";
				s += "\t \n";
				
			}
			s += "\n";
			writer.write(s);
			System.out.println("Info:");
			System.out.println("Filename: RM_rucksalt" + databasename+ ".txt");
			System.out.println("Location: "+filename + "\\RM_rucksalt_" + databasename+ ".txt");
			System.out.println("");
			if(errors.equals("")&&output.equals("rm")){
				System.out.println("Generieren und Speichern des RM ohne Fehler (TXT Version)");
			}
			writer.close();
			
		} catch (Exception e) {
		} 
	}

	
	/**
	 * Diese Methode sollte ebenfalls ein RM erstellen allerdings dieses Mal in einem HTML Format, warum?
	 * Die Idee dahinter war dass es in HTML moeglich ist die Attribute wie zum Beipsiel einen PrimaryKey zu unterstreichen mittels <u> PK </u>
	 * Die ForgeinKeys muessen immer noch mittels "." gekennzeichnet werden da es in HTML nicht moeglich ist ein Element doppelt zu Unterstreichen
	 * Allerdings ist dieser Versuch etwas komplizierter als das erstellen eines TXT Files. Mittlerweile kann man es ausfuehren aber nicht ohne probleme!
	 * Hier unten sehen sie eine kleine Liste mit Fehlern die waerend dem erstellen des Files aufgetreten sind.
	 * 
	 * Fehler vom 12-02-2015:
	 * Das File wird nicht im Zielordner gespeichert
	 * 
	 * Fehler vom 13_02-2015:
	 * Das erstellte HTML File ist Leer.
	 * 
	 * Fehler vom 15-02-2015:
	 * Es ist mittlerweile moeglich die Methode RM_HTML Fehlerfrei zu Kompilieren allerdings wird das ergebnis 2 mal hintereinander in das html File geschrieben
	 *
	 */
	 	
	 	private void RM_HTML(String filename) {
	 
			
		Writer writer = null;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename + "RM_rucksalt_V2_" + databasename
							+ ".html"), "utf-8"));
			
			String s = "";
			s += "<Html><Head>";
			s += "<Title>Bernhard Schmid RM_html</Title>";
			s += "</HEAD><BODY>";
			
			for (Enumeration<Table> e = db_Tables.elements(); e
					.hasMoreElements();) {
				Table t_tamp = (e.nextElement());
				s += t_tamp.getName() + "(";
				Hashtable<String, Attribute> x2 = t_tamp.getAttributes();
				
				for (Enumeration<Attribute> e1 = x2.elements(); e1.hasMoreElements();) {
					Attribute m = e1.nextElement();
					if (m.getFa() != null) 
						s += m.getFk() + ".";
					if (m.isPrimary()) 
						s += "<u>";
					s += m.getAttributName();
					if (m.isPrimary()) 
						s += "</u>";
					if (e1.hasMoreElements()) 
						s += ",";
				}
				s += ")<br>";
			}
			
			s += "</BODY></HTML>";
			
			writer.write(s);
				writer.write(s);
				System.out.println("Info:");
				System.out.println("Filename: RM_rucksalt_V2_" + databasename+ ".html");
				System.out.println("Location: "+filename + "\\RM_rucksalt_V2_" + databasename+ ".html");
				System.out.println();
				
				if(errors.equals("")&&output.equals("rm")){
					System.out.println("Generieren und Speichern des RM ohne Fehler (HTML Version)");
				}
				
				writer.close();
				
			} catch (Exception e) {
		}
	}
}

