package com.pmsapp.view.vo;

import java.util.ArrayList;
import java.util.List;

public class TicketVO {

	private Long ticketId;
	private String ticketTitle;
	private String description;
	private Long siteId;
	private String siteName;
	private String ticketNumber;
	private Long assetId;
	private String assetName;
	private Long categoryId;
	private String categoryName;
	private Long statusId;
	private String status;
	private String raisedOn;
	private Long assignedTo;
	private String assignedSP;
	private String assignedSPEmail;
	private String raisedBy;
	private Long raisedUser;
	private Long priorityId;
	private String priorityCode;
	private String priorityDescription;
	private String sla;
	private int duration;
	private String unit;
	private int statusCode;
	private String message;
	private String ticketStartTime;
	private Long closeCode;
	private String closedBy;
	private String closeNote;
	private String closedOn;
	private String modifiedOn;
	private String modifiedBy;
	private List<EscalationLevelVO> escalationLevelList=new ArrayList<EscalationLevelVO>();
	private List<CustomerSPLinkedTicketVO> linkedTickets = new ArrayList<CustomerSPLinkedTicketVO>();
	private List<TicketEscalationVO> escalatedTicketList = new ArrayList<TicketEscalationVO>();
	private List<TicketCommentVO> ticketComments = new ArrayList<TicketCommentVO>();
	private double slaPercent;
	private String fileInput;
	private String fileExtension;
	
	public TicketVO() {
		super();
	}
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public String getTicketTitle() {
		return ticketTitle;
	}
	public void setTicketTitle(String ticketTitle) {
		this.ticketTitle = ticketTitle;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRaisedOn() {
		return raisedOn;
	}
	public void setRaisedOn(String raisedOn) {
		this.raisedOn = raisedOn;
	}
	
	public Long getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(Long assignedTo) {
		this.assignedTo = assignedTo;
	}
	public String getAssignedSP() {
		return assignedSP;
	}
	public void setAssignedSP(String assignedSP) {
		this.assignedSP = assignedSP;
	}
	public String getRaisedBy() {
		return raisedBy;
	}
	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}
	public Long getRaisedUser() {
		return raisedUser;
	}
	public void setRaisedUser(Long raisedUser) {
		this.raisedUser = raisedUser;
	}
	public Long getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Long priorityId) {
		this.priorityId = priorityId;
	}
	public String getPriorityCode() {
		return priorityCode;
	}
	public void setPriorityCode(String priorityCode) {
		this.priorityCode = priorityCode;
	}
	public String getPriorityDescription() {
		return priorityDescription;
	}
	public void setPriorityDescription(String priorityDescription) {
		this.priorityDescription = priorityDescription;
	}
	public String getSla() {
		return sla;
	}
	public void setSla(String sla) {
		this.sla = sla;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	public String getTicketStartTime() {
		return ticketStartTime;
	}
	public void setTicketStartTime(String ticketStartTime) {
		this.ticketStartTime = ticketStartTime;
	}
	public Long getCloseCode() {
		return closeCode;
	}
	public void setCloseCode(Long closeCode) {
		this.closeCode = closeCode;
	}
	public String getClosedBy() {
		return closedBy;
	}
	public void setClosedBy(String closedBy) {
		this.closedBy = closedBy;
	}
	public String getCloseNote() {
		return closeNote;
	}
	public void setClosedNote(String closeNote) {
		this.closeNote = closeNote;
	}
	public String getClosedOn() {
		return closedOn;
	}
	public void setClosedOn(String closedOn) {
		this.closedOn = closedOn;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatus() {
		return status;
	}
	public String getAssignedSPEmail() {
		return assignedSPEmail;
	}
	public void setAssignedSPEmail(String assignedSPEmail) {
		this.assignedSPEmail = assignedSPEmail;
	}
	public List<EscalationLevelVO> getEscalationLevelList() {
		return escalationLevelList;
	}
	public void setEscalationLevelList(List<EscalationLevelVO> escalationLevelList) {
		this.escalationLevelList = escalationLevelList;
	}
	public List<CustomerSPLinkedTicketVO> getLinkedTickets() {
		return linkedTickets;
	}
	public void setLinkedTickets(List<CustomerSPLinkedTicketVO> linkedTickets) {
		this.linkedTickets = linkedTickets;
	}
	public List<TicketEscalationVO> getEscalatedTicketList() {
		return escalatedTicketList;
	}
	public void setEscalatedTicketList(List<TicketEscalationVO> escalatedTicketList) {
		this.escalatedTicketList = escalatedTicketList;
	}
	public List<TicketCommentVO> getTicketComments() {
		return ticketComments;
	}
	public void setTicketComments(List<TicketCommentVO> ticketComments) {
		this.ticketComments = ticketComments;
	}
	public double getSlaPercent() {
		return slaPercent;
	}
	public void setSlaPercent(double slaPercent) {
		this.slaPercent = slaPercent;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public String getFileInput() {
		return fileInput;
	}
	public void setFileInput(String fileInput) {
		this.fileInput = fileInput;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	public void setCloseNote(String closeNote) {
		this.closeNote = closeNote;
	}
	@Override
	public String toString() {
		return "TicketVO [ticketId=" + ticketId + ", ticketTitle=" + ticketTitle + ", description=" + description
				+ ", siteId=" + siteId + ", siteName=" + siteName + ", assetId=" + assetId + ", assetName=" + assetName
				+ ", categoryId=" + categoryId + ", categoryName=" + categoryName + ", statusId=" + statusId
				+ ", status=" + status + ", raisedOn=" + raisedOn + ", assignedTo=" + assignedTo
				+ ", assignedSP=" + assignedSP + ", raisedBy=" + raisedBy + ", raisedUser=" + raisedUser
				+ ", priorityId=" + priorityId + ", priorityCode=" + priorityCode + ", priorityDescription="
				+ priorityDescription + ", sla=" + sla + ", status=" + status + ", message=" + message + "]";
	}
	
	
	
	
	
	
}
