package de.ur.mi.parse;

public class ParselistdownloadClass {
	private String name;
	private double preis;
	private String art;
	private String tisch;
	private String kategorie;
	private String user;
	private String used;
	private String id;
    private String kellner;

    public String getKellner() {
        return kellner;
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

	public double getPreis() {
		return preis;
	}

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public void setPreis(Double double1) {
        this.preis = double1;
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

	public String getUsed() {
		return used;
	}

	public void setUsed(String used) {
		this.used = used;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}