package com.pmsapp.view.vo;


public class SiteLicenceVO  {

	private Long licenseId;
	private String licenseName;
	private String validfrom;
	private String validto;


	public Long getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(final Long licenseId) {
		this.licenseId = licenseId;
	}
	public String getLicenseName() {
		return licenseName;
	}
	public void setLicenseName(final String licenseName) {
		this.licenseName = licenseName;
	}
	public String getValidfrom() {
		return validfrom;
	}
	public void setValidfrom(final String validfrom) {
		this.validfrom = validfrom;
	}
	public String getValidto() {
		return validto;
	}
	public void setValidto(final String validto) {
		this.validto = validto;
	}
	@Override
	public String toString() {
		return "SiteLicenceVO [licenseId=" + licenseId + ", licenseName="
				+ licenseName + ", validfrom=" + validfrom + ", validto="
				+ validto + "]";
	}

}
