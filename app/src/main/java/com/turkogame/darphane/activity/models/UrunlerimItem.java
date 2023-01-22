package com.turkogame.darphane.activity.models;

public class UrunlerimItem {
	private String KAYIT_ID;
	private String PAKET_ID;
	private String TEKIL_URUN_KODU;
	private String DURUM;
	private String SATIN_ALAN_KULLANICI;
	private String ISLEM_ZAMANI;
	private String ACIKLAMA;
	private String PAKET_ADI;
	private String PAKET_RESMI;



	public UrunlerimItem(String KAYIT_ID, String PAKET_ID, String TEKIL_URUN_KODU, String DURUM, String SATIN_ALAN_KULLANICI
			, String ISLEM_ZAMANI, String ACIKLAMA, String PAKET_ADI, String PAKET_RESMI) {
		this.KAYIT_ID = KAYIT_ID;
		this.PAKET_ID = PAKET_ID;
		this.TEKIL_URUN_KODU = TEKIL_URUN_KODU;
		this.DURUM = DURUM;
		this.SATIN_ALAN_KULLANICI = SATIN_ALAN_KULLANICI;
		this.ISLEM_ZAMANI = ISLEM_ZAMANI;
		this.ACIKLAMA = ACIKLAMA;
		this.PAKET_ADI = PAKET_ADI;
		this.PAKET_RESMI = PAKET_RESMI;
	}

	public String getKAYIT_ID() {
		return KAYIT_ID;
	}
	public String getPAKET_ID() {
		return PAKET_ID;
	}
	public String getTEKIL_URUN_KODU() {
		return TEKIL_URUN_KODU;
	}
	public String getDURUM() {
		return DURUM;
	}
	public String getSATIN_ALAN_KULLANICI() {
		return SATIN_ALAN_KULLANICI;
	}
	public String getISLEM_ZAMANI() {
		return ISLEM_ZAMANI;
	}
	public String getACIKLAMA() {
		return ACIKLAMA;
	}
	public String getPAKET_ADI() {
		return PAKET_ADI;
	}
	public String getPAKET_RESMI() {
		return PAKET_RESMI;
	}



	public void setKAYIT_ID(String KAYIT_ID) {
		this.KAYIT_ID = KAYIT_ID;
	}
	public void setPAKET_ID(String PAKET_ID) {
		this.PAKET_ID = PAKET_ID;
	}
	public void setTEKIL_URUN_KODU(String TEKIL_URUN_KODU) {
		this.TEKIL_URUN_KODU = TEKIL_URUN_KODU;
	}
	public void setDURUM(String DURUM) {
		this.DURUM = DURUM;
	}
	public void setSATIN_ALAN_KULLANICI(String SATIN_ALAN_KULLANICI) {
		this.SATIN_ALAN_KULLANICI = SATIN_ALAN_KULLANICI;
	}
	public void setISLEM_ZAMANI(String ISLEM_ZAMANI) {
		this.ISLEM_ZAMANI = ISLEM_ZAMANI;
	}
	public void setACIKLAMA(String ACIKLAMA) {
		this.ACIKLAMA = ACIKLAMA;
	}
	public void setPAKET_ADI(String PAKET_ADI) {
		this.PAKET_ADI = PAKET_ADI;
	}
	public void setPAKET_RESMI(String PAKET_RESMI) {
		this.PAKET_RESMI = PAKET_RESMI;
	}


}
