package com.turkogame.darphane.activity.models;

public class AktifFalcilarItem{
	private String RESIM;
	private String ADI;
	private String KREDI_BEDELI;
	private String SOYADI;
	private String KULLANICI_ID;
	private String DURUM;


	public AktifFalcilarItem(String ADI, String SOYADI, String KREDI_BEDELI, String DURUM, String KULLANICI_ID,String RESIM) {
		this.ADI = ADI;
		this.SOYADI = SOYADI;
		this.KREDI_BEDELI = KREDI_BEDELI;
		this.DURUM = DURUM;
		this.KULLANICI_ID = KULLANICI_ID;
		this.RESIM = RESIM;

	}

	public String getRESIM() {
		return RESIM;
	}

	public String getADI() {
		return ADI;
	}

	public String getKREDI_BEDELI() {
		return KREDI_BEDELI;
	}

	public String getSOYADI() {
		return SOYADI;
	}

	public String getKULLANICI_ID() {
		return KULLANICI_ID;
	}

	public String getDURUM() {
		return DURUM;
	}

	public void setRESIM(String RESIM) {
		this.RESIM = RESIM;
	}

	public void setADI(String ADI) {
		this.ADI = ADI;
	}

	public void setKREDI_BEDELI(String KREDI_BEDELI) {
		this.KREDI_BEDELI = KREDI_BEDELI;
	}

	public void setSOYADI(String SOYADI) {
		this.SOYADI = SOYADI;
	}

	public void setKULLANICI_ID(String KULLANICI_ID) {
		this.KULLANICI_ID = KULLANICI_ID;
	}

	public void setDURUM(String DURUM) {
		this.DURUM = DURUM;
	}
}
