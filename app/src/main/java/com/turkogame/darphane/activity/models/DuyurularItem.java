package com.turkogame.darphane.activity.models;

public class DuyurularItem {
	private String DUYURU_ID;
	private String DUYURU_KONUSU;
	private String DUYURU_METNI;
	private String DUYURU_TARIHI;
	private String KULLANICI_ID;
	private String DUYURU_SAATI;
	private String DUYURU_GRUBU;
	private String BITIS_TARIHI;


	public DuyurularItem(String DUYURU_ID, String DUYURU_KONUSU, String DUYURU_METNI, String DUYURU_TARIHI,
						 String KULLANICI_ID, String DUYURU_SAATI, String DUYURU_GRUBU, String BITIS_TARIHI) {
		this.DUYURU_ID = DUYURU_ID;
		this.DUYURU_KONUSU = DUYURU_KONUSU;
		this.DUYURU_METNI = DUYURU_METNI;
		this.DUYURU_TARIHI = DUYURU_TARIHI;
		this.KULLANICI_ID = KULLANICI_ID;
		this.DUYURU_SAATI = DUYURU_SAATI;
		this.DUYURU_GRUBU = DUYURU_GRUBU;
		this.BITIS_TARIHI = BITIS_TARIHI;

	}

	public String getDUYURU_ID() {
		return DUYURU_ID;
	}

	public String getDUYURU_KONUSU() {
		return DUYURU_KONUSU;
	}

	public String getDUYURU_METNI() {
		return DUYURU_METNI;
	}

	public String getDUYURU_TARIHI() {
		return DUYURU_TARIHI;
	}

	public String getKULLANICI_ID() {
		return KULLANICI_ID;
	}

	public String getDUYURU_SAATI() {
		return DUYURU_SAATI;
	}

	public String getDUYURU_GRUBU() {
		return DUYURU_GRUBU;
	}

	public String getBITIS_TARIHI() {
		return BITIS_TARIHI;
	}



	public void setDUYURU_ID(String DUYURU_ID) {
		this.DUYURU_ID = DUYURU_ID;
	}

	public void setDUYURU_KONUSU(String DUYURU_KONUSU) {
		this.DUYURU_KONUSU = DUYURU_KONUSU;
	}

	public void setDUYURU_METNI(String DUYURU_METNI) {
		this.DUYURU_METNI = DUYURU_METNI;
	}

	public void setDUYURU_TARIHI(String DUYURU_TARIHI) {
		this.DUYURU_TARIHI = DUYURU_TARIHI;
	}

	public void setKULLANICI_ID(String KULLANICI_ID) {
		this.KULLANICI_ID = KULLANICI_ID;
	}

	public void setDUYURU_SAATI(String DUYURU_SAATI) {
		this.DUYURU_SAATI = DUYURU_SAATI;
	}

	public void setDUYURU_GRUBU(String DUYURU_GRUBU) {
		this.DUYURU_GRUBU = DUYURU_GRUBU;
	}

	public void setBITIS_TARIHI(String BITIS_TARIHI) {
		this.BITIS_TARIHI = BITIS_TARIHI;
	}
}
