package com.turkogame.darphane.activity.models;

public class MagazaItem {
	private String PAKET_ID;
	private String PAKET_ADI;
	private String ACIKLAMA;
	private String MIKTAR;
	private String TUTAR;
	private String KREDI_TUTAR;
	private String AKTIF;
	private String PAKET_TURU;
	private String PAKET_RESMI;
	private String ADSENSE_ID;


	public MagazaItem(String PAKET_ID, String PAKET_ADI, String ACIKLAMA, String MIKTAR,
                      String TUTAR, String KREDI_TUTAR, String AKTIF, String PAKET_TURU, String PAKET_RESMI, String ADSENSE_ID) {
		this.PAKET_ID = PAKET_ID;
		this.PAKET_ADI = PAKET_ADI;
		this.ACIKLAMA = ACIKLAMA;
		this.MIKTAR = MIKTAR;
		this.TUTAR = TUTAR;
		this.KREDI_TUTAR = KREDI_TUTAR;
		this.AKTIF = AKTIF;
		this.PAKET_TURU = PAKET_TURU;
		this.PAKET_RESMI = PAKET_RESMI;
		this.ADSENSE_ID = ADSENSE_ID;

	}

	public String getPAKET_ID() {
		return PAKET_ID;
	}

	public String getPAKET_ADI() {
		return PAKET_ADI;
	}

	public String getACIKLAMA() {
		return ACIKLAMA;
	}

	public String getMIKTAR() {
		return MIKTAR;
	}

	public String getTUTAR() {
		return TUTAR;
	}

	public String getKREDI_TUTAR() {
		return KREDI_TUTAR;
	}

	public String getAKTIF() {
		return AKTIF;
	}

	public String getPAKET_TURU() {
		return PAKET_TURU;
	}

	public String getPAKET_RESMI() {
		return PAKET_RESMI;
	}

	public String getADSENSE_ID() {
		return ADSENSE_ID;
	}


	public void setPAKET_ID(String PAKET_ID) {
		this.PAKET_ID = PAKET_ID;
	}

	public void setPAKET_ADI(String PAKET_ADI) {
		this.PAKET_ADI = PAKET_ADI;
	}

	public void setACIKLAMA(String ACIKLAMA) {
		this.ACIKLAMA = ACIKLAMA;
	}

	public void setMIKTAR(String MIKTAR) {
		this.MIKTAR = MIKTAR;
	}

	public void setTUTAR(String TUTAR) {
		this.TUTAR = TUTAR;
	}

	public void setKREDI_TUTAR(String TUTAR) {
		this.KREDI_TUTAR = TUTAR;
	}

	public void setAKTIF(String AKTIF) {
		this.AKTIF = AKTIF;
	}

	public void setPAKET_TURU(String PAKET_TURU) {
		this.PAKET_TURU = PAKET_TURU;
	}

	public void setPAKET_RESMI(String PAKET_RESMI) {
		this.PAKET_RESMI = PAKET_RESMI;
	}

	public void setADSENSE_ID(String ADSENSE_ID) {
		this.ADSENSE_ID = ADSENSE_ID;
	}
}
