package rw;

import java.util.Hashtable;

/**
 * In der Klasse Tables werden die Tabellen (Attribute) in eine Hashtable gespespeichert
 * 
 *  Hashtable: This class implements a hash table, which maps keys to values. Any non-null object can be used as a key or as a value.
 * 
 * @author *Bernhard Schmid* & Raphael Simsek
 * @version 26-01-2015 bis 16-02-2015
 */

public class Table {
	String tableName;
	Hashtable<String, Attribute> atribute = new Hashtable<String, Attribute>();

	public Table(String name) {
		tableName = name;
	}


	public void addAttribute(String name) {
		atribute.put(name, new Attribute(this, name));
	}


	public void addAttribute(Attribute attribute) {
		atribute.put(attribute.getAttributName(), attribute);
	}


	public Hashtable<String, Attribute> getAttributes() {
		return atribute;
	}


	public String getName() {
		return tableName;
	}


	public void setForeignAttribute(String att, String fk, String fa) {
		atribute.get(att).setFk(fk);
		atribute.get(att).setFa(fa);
	}

}
