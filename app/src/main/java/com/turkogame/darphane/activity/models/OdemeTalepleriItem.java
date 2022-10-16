package com.turkogame.darphane.activity.models;

public class OdemeTalepleriItem {
	private String TALEP_ID;
	private String KULLANICI_ID;
	private String TARIH;
	private String TUTAR;
	private String DURUM;
	private String ACIKLAMA;



	public OdemeTalepleriItem(String TALEP_ID, String KULLANICI_ID, String TARIH, String TUTAR,
                              String DURUM, String ACIKLAMA) {

		this.TALEP_ID = TALEP_ID;
		this.KULLANICI_ID = KULLANICI_ID;
		this.TARIH = TARIH;
		this.TUTAR = TUTAR;
		this.DURUM = DURUM;
		this.ACIKLAMA = ACIKLAMA;

	}

	public String getTALEP_ID() {
		return TALEP_ID;
	}
	public String getKULLANICI_ID() {
		return KULLANICI_ID;
	}
	public String getTARIH() {
		return TARIH;
	}
	public String getTUTAR() {
		return TUTAR;
	}
	public String getDURUM() {
		return DURUM;
	}
	public String getACIKLAMA() {
		return ACIKLAMA;
	}



	public void setTALEP_ID(String TALEP_ID) {
		this.TALEP_ID = TALEP_ID;
	}
	public void setKULLANICI_ID(String KULLANICI_ID) {
		this.KULLANICI_ID = KULLANICI_ID;
	}
	public void setTARIH(String TARIH) {
		this.TARIH = TARIH;
	}
	public void setTUTAR(String TUTAR) {
		this.TUTAR = TUTAR;
	}
	public void setDURUM(String DURUM) {
		this.DURUM = DURUM;
	}
	public void setACIKLAMA(String ACIKLAMA) {
		this.ACIKLAMA = ACIKLAMA;
	}

}
