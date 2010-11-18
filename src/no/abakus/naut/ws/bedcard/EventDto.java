package no.abakus.naut.ws.bedcard;

import java.util.Date;

import no.abakus.naut.entity.news.Type;

public class EventDto {
	private Long id;
	private String title;
	private String location;
	private String description;
	private String summary;
	private Integer capacity;
	private Integer price;
	private Integer memberPrice;
	private Boolean visible = true;
	private Boolean binding = false;
	private Date startTime;
	private Date endTime;
	private Integer dotAmount = 0;
	private Boolean considersDots = false;
	private Boolean allowNonUsers = false;
	private Type type;
	
	public Boolean getAllowNonUsers() {
		return allowNonUsers;
	}
	public void setAllowNonUsers(Boolean allowNonUsers) {
		this.allowNonUsers = allowNonUsers;
	}
	
	public Boolean getBinding() {
		return binding;
	}
	public void setBinding(Boolean binding) {
		this.binding = binding;
	}
	
	public Integer getCapacity() {
		return capacity;
	}
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}
	
	public Boolean getConsidersDots() {
		return considersDots;
	}
	public void setConsidersDots(Boolean considersDots) {
		this.considersDots = considersDots;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getDotAmount() {
		return dotAmount;
	}
	public void setDotAmount(Integer dotAmount) {
		this.dotAmount = dotAmount;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	
	public Integer getMemberPrice() {
		return memberPrice;
	}
	public void setMemberPrice(Integer memberPrice) {
		this.memberPrice = memberPrice;
	}
	
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public Boolean getVisible() {
		return visible;
	}
	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
