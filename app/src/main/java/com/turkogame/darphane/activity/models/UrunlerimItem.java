package com.turkogame.darphane.activity.models;

public class UrunlerimItem {
	private String ISTEK_ID;
	private String KULLANICI_ID;
	private String ISTEK_TURU;
	private String DURUM;
	private String GIRIS;
	private String CEVAP_GENEL;
	private String CEVAP_ASK;
	private String CEVAP_KARIYER;
	private String CEVAP_SAGLIK;
	private String SONUC;
	private String ISTEK_TARIHI;
	private String ISTEK_SAATI;
	private String CEVAP_TARIHI;
	private String CEVAP_SAATI;
	private String CEVAP_KULLANICI_ID;
	private String FAL_TURU;
	private String RUYA_METNI;
	private String YORUM;
	private String ISTENEN_FALCI;
	private String FALCI_ADI;
	private String FALCI_SOYADI;


	public UrunlerimItem(String ISTEK_ID, String KULLANICI_ID, String ISTEK_TURU, String DURUM, String GIRIS
			, String CEVAP_GENEL, String CEVAP_ASK, String CEVAP_KARIYER, String CEVAP_SAGLIK, String SONUC
			, String ISTEK_TARIHI, String ISTEK_SAATI, String CEVAP_TARIHI, String CEVAP_SAATI, String CEVAP_KULLANICI_ID
			, String FAL_TURU, String RUYA_METNI, String YORUM, String ISTENEN_FALCI, String FALCI_ADI, String FALCI_SOYADI) {
		this.ISTEK_ID = ISTEK_ID;
		this.KULLANICI_ID = KULLANICI_ID;
		this.ISTEK_TURU = ISTEK_TURU;
		this.DURUM = DURUM;
		this.GIRIS = GIRIS;
		this.CEVAP_GENEL = CEVAP_GENEL;
		this.CEVAP_ASK = CEVAP_ASK;
		this.CEVAP_KARIYER = CEVAP_KARIYER;
		this.CEVAP_SAGLIK = CEVAP_SAGLIK;
		this.SONUC = SONUC;
		this.ISTEK_TARIHI = ISTEK_TARIHI;
		this.ISTEK_SAATI = ISTEK_SAATI;
		this.CEVAP_TARIHI = CEVAP_TARIHI;
		this.CEVAP_SAATI = CEVAP_SAATI;
		this.CEVAP_KULLANICI_ID = CEVAP_KULLANICI_ID;
		this.FAL_TURU = FAL_TURU;
		this.YORUM = YORUM;
		this.RUYA_METNI = RUYA_METNI;
		this.ISTENEN_FALCI = ISTENEN_FALCI;
		this.FALCI_ADI = FALCI_ADI;
		this.FALCI_SOYADI = FALCI_SOYADI;
	}

	public String getISTEK_ID() {
		return ISTEK_ID;
	}
	public String getKULLANICI_ID() {
		return KULLANICI_ID;
	}
	public String getISTEK_TURU() {
		return ISTEK_TURU;
	}
	public String getDURUM() {
		return DURUM;
	}
	public String getGIRIS() {
		return GIRIS;
	}
	public String getCEVAP_GENEL() {
		return CEVAP_GENEL;
	}
	public String getCEVAP_ASK() {
		return CEVAP_ASK;
	}
	public String getCEVAP_KARIYER() {
		return CEVAP_KARIYER;
	}
	public String getCEVAP_SAGLIK() {
		return CEVAP_SAGLIK;
	}
	public String getSONUC() {
		return SONUC;
	}
	public String getISTEK_TARIHI() {
		return ISTEK_TARIHI;
	}
	public String getISTEK_SAATI() {
		return ISTEK_SAATI;
	}
	public String getCEVAP_TARIHI() {
		return CEVAP_TARIHI;
	}
	public String getCEVAP_SAATI() {
		return CEVAP_SAATI;
	}
	public String getCEVAP_KULLANICI_ID() {
		return CEVAP_KULLANICI_ID;
	}
	public String getFAL_TURU() {
		return FAL_TURU;
	}
	public String getYORUM() {
		return YORUM;
	}
	public String getRUYA_METNI() {
		return RUYA_METNI;
	}
	public String getISTENEN_FALCI() {
		return ISTENEN_FALCI;
	}
	public String getFALCI_ADI() {
		return FALCI_ADI;
	}
	public String getFALCI_SOYADI() {
		return FALCI_SOYADI;
	}


	public void setISTEK_ID(String ISTEK_ID) {
		this.ISTEK_ID = ISTEK_ID;
	}
	public void setKULLANICI_ID(String KULLANICI_ID) {
		this.KULLANICI_ID = KULLANICI_ID;
	}
	public void setISTEK_TURU(String ISTEK_TURU) {
		this.ISTEK_TURU = ISTEK_TURU;
	}
	public void setDURUM(String DURUM) {
		this.DURUM = DURUM;
	}
	public void setGIRIS(String GIRIS) {
		this.GIRIS = GIRIS;
	}
	public void setCEVAP_ASK(String CEVAP_ASK) {
		this.CEVAP_ASK = CEVAP_ASK;
	}
	public void setCEVAP_KARIYER(String CEVAP_KARIYER) {
		this.CEVAP_KARIYER = CEVAP_KARIYER;
	}
	public void setCEVAP_SAGLIK(String CEVAP_SAGLIK) {
		this.CEVAP_SAGLIK = CEVAP_SAGLIK;
	}
	public void setSONUC(String SONUC) {
		this.SONUC = SONUC;
	}
	public void setISTEK_TARIHI(String ISTEK_TARIHI) {
		this.ISTEK_TARIHI = ISTEK_TARIHI;
	}
	public void setISTEK_SAATI(String ISTEK_SAATI) {
		this.ISTEK_SAATI = ISTEK_SAATI;
	}
	public void setCEVAP_TARIHI(String CEVAP_TARIHI) {
		this.CEVAP_TARIHI = CEVAP_TARIHI;
	}
	public void setCEVAP_SAATI(String CEVAP_SAATI) {
		this.CEVAP_SAATI = CEVAP_SAATI;
	}
	public void setCEVAP_KULLANICI_ID(String CEVAP_KULLANICI_ID) {this.CEVAP_KULLANICI_ID = CEVAP_KULLANICI_ID;	}
	public void setFAL_TURU(String FAL_TURU) {
		this.FAL_TURU = FAL_TURU;
	}
	public void setYORUM(String YORUM) {
		this.YORUM = YORUM;
	}
	public void setRUYA_METNI(String RUYA_METNI) {
		this.RUYA_METNI = RUYA_METNI;
	}
	public void setISTENEN_FALCI(String ISTENEN_FALCI) {
		this.ISTENEN_FALCI = ISTENEN_FALCI;
	}
	public void setFALCI_ADI(String FALCI_ADI) {
		this.FALCI_ADI = FALCI_ADI;
	}
	public void setFALCI_SOYADI(String FALCI_SOYADI) {
		this.FALCI_SOYADI = FALCI_SOYADI;
	}

}
