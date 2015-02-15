package datengenerierung;

import java.io.*;

/*
 * Programm das eine immer unterschiedliche Kombination an Inserts ausgeben soll.
 * Dieses Programm gibt für jede Tabelle des DDL einen dazu passenden Wert im Insert aus.
 * @author: Raphael Simsek
 * @version: 2014-09-29
 */
public class Dateierstell{
	private File f = null;
	private String filename="";
	public Dateierstell(String filename){
		this.filename=filename;
		f = new File(filename);
		try {
			f.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writefile(String a){
		BufferedWriter b=null;
		try {
			b = new BufferedWriter(new FileWriter(filename, true));
			b.write(a);
			b.newLine();
			b.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if(b!=null){
				try {
					b.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
