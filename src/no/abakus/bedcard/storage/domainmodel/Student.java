package no.abakus.bedcard.storage.domainmodel;

public class Student {
	private long userId;
	private String username;
	private String firstname;
	private String lastname;
	private Integer cardNumber;
	private Long rfidCardNumber;
	private boolean hasAccess;
	private boolean vip;
	
	
	
	public Student() {
		super();
	}
	public Student(long userId, String username, String firstname,
			String lastname, Integer cardNumber, Long rfidCardNumber,
			boolean hasAccess, boolean vip) {
		super();
		this.userId = userId;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.cardNumber = cardNumber;
		this.rfidCardNumber = rfidCardNumber;
		this.hasAccess = hasAccess;
		this.vip = vip;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public Integer getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(Integer cardNumber) {
		this.cardNumber = cardNumber;
	}
	public Long getRfidCardNumber() {
		return rfidCardNumber;
	}
	public void setRfidCardNumber(Long rfidCardNumber) {
		this.rfidCardNumber = rfidCardNumber;
	}
	public boolean isHasAccess() {
		return hasAccess;
	}
	public void setHasAccess(boolean hasAccess) {
		this.hasAccess = hasAccess;
	}
	public boolean isVip() {
		return vip;
	}
	public void setVip(boolean vip) {
		this.vip = vip;
	}
	@Override
	public Student clone(){
		return new Student(userId, username, firstname, lastname, cardNumber, rfidCardNumber, hasAccess, vip);
	}
	
}
