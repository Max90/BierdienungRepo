package de.ur.mi.parse;

public class ParselistdownloadClass {
	private String name;
	private String preis;
	private String art;
	private String tisch;
	private String kategorie;
	private String user;
	private String id;
	private String kellner;
    private String background;

    public String getKellner() {
		return kellner;
	}
	
	public String getBackground() {
		return background;
	}
	
	public void setBackground(String background) {
		this.background = background;
	}

	public void setKellner(String kellner) {
		this.kellner = kellner;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPreis() {
		return preis;
	}

	public void setPreis(String preis) {
		this.preis = preis;
	}

	public String getArt() {
		return art;
	}

	public void setArt(String art) {
		this.art = art;
	}

	public String getTisch() {
		return tisch;
	}

	public void setTisch(String tisch) {
		this.tisch = tisch;
	}

	public String getKategorie() {
		return kategorie;
	}

	public void setKategorie(String kategorie) {
		this.kategorie = kategorie;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}