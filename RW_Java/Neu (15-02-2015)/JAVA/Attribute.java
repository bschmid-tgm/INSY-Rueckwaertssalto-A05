package rw;

/**
 * Die Attribut-Klasse ist mit dem Model des MVC Modells vergleichbar
 * Diese Klasse ist fuer die Kapselung der Attribute zustaendig (desswegen die getter und setter Methoden)
 * Es werden alle Attribute (private) in getter und setter Methoden verpackt damit man sie nicht direkt aufruchen kann.
 * 
 * @author *Bernhard Schmid* & Raphael Simsek
 * @version 26-01-2015 bis 16-02-2015
 */

public class Attribute {
	
	private boolean isPrimary;
	private String attributName;
	private Table tabelle;
	private String fk,fa;
	

	public Attribute(Table table, String name) {
		tabelle=table;
		this.setAttributName(name);
	}
	

	public String getFk() {
		return fk;
	}


	public void setFk(String foreignTable) {
		this.fk = foreignTable;
	}


	public String getFa() {
		return fa;
	}


	public void setFa(String foreignAttribut) {
		this.fa = foreignAttribut;
	}

	
	public Table getTable() {
		return tabelle;
	}


	public boolean isPrimary() {
		return isPrimary;
	}


	public void setPrimary(boolean isPrimary) {
		this.isPrimary = isPrimary;
	}


	public String getAttributName() {
		return attributName;
	}


	public void setAttributName(String attributeName) {
		attributName = attributeName;
	}



}