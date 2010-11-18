package no.abakus.bedcard.storage.domainmodel;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import no.abakus.naut.entity.news.Type;

public class Event {
	private long id;
	private String title;
	private String location;
	private String description;
	private String summary;
	private int capacity;
	private int price;
	private int memberPrice;
	private boolean visible;
	private boolean binding;
	private Date startTime;
	private Date endTime;
	private int dotAmount;
	private boolean considersDots;
	private boolean allowNonUsers;
	private Type type;
	private HashMap<Long, StudentEntry> listOfParticipants;
	private ArrayList<Student> netWaitingList;
	private ArrayList<Student> netWaitingListBanned;
	private HashMap<Long, StudentEntry> waitingList;
	private Image image;
	
	public Event() {
	}
	

	public Event(long id, String title, String location, String description,
			String summary, int capacity, int price, int memberPrice,
			boolean visible, boolean binding, Date startTime, Date endTime,
			int dotAmount, boolean considersDots, boolean allowNonUsers,
			Type type, HashMap<Long, StudentEntry> listOfParticipants,
			ArrayList<Student> netWaitingList,
			ArrayList<Student> netWaitingListBanned,
			HashMap<Long, StudentEntry> waitingList, Image image) {
		super();
		this.id = id;
		this.title = title;
		this.location = location;
		this.description = description;
		this.summary = summary;
		this.capacity = capacity;
		this.price = price;
		this.memberPrice = memberPrice;
		this.visible = visible;
		this.binding = binding;
		this.startTime = startTime;
		this.endTime = endTime;
		this.dotAmount = dotAmount;
		this.considersDots = considersDots;
		this.allowNonUsers = allowNonUsers;
		this.type = type;
		this.listOfParticipants = listOfParticipants;
		this.netWaitingList = netWaitingList;
		this.netWaitingListBanned = netWaitingListBanned;
		this.waitingList = waitingList;
		this.image = image;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getSummary() {
		return summary;
	}


	public void setSummary(String summary) {
		this.summary = summary;
	}


	public int getCapacity() {
		return capacity;
	}


	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}


	public int getPrice() {
		return price;
	}


	public void setPrice(int price) {
		this.price = price;
	}


	public int getMemberPrice() {
		return memberPrice;
	}


	public void setMemberPrice(int memberPrice) {
		this.memberPrice = memberPrice;
	}


	public boolean isVisible() {
		return visible;
	}


	public void setVisible(boolean visible) {
		this.visible = visible;
	}


	public boolean isBinding() {
		return binding;
	}


	public void setBinding(boolean binding) {
		this.binding = binding;
	}


	public Date getStartTime() {
		return startTime;
	}


	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public int getDotAmount() {
		return dotAmount;
	}


	public void setDotAmount(int dotAmount) {
		this.dotAmount = dotAmount;
	}


	public boolean isConsidersDots() {
		return considersDots;
	}


	public void setConsidersDots(boolean considersDots) {
		this.considersDots = considersDots;
	}


	public boolean isAllowNonUsers() {
		return allowNonUsers;
	}


	public void setAllowNonUsers(boolean allowNonUsers) {
		this.allowNonUsers = allowNonUsers;
	}


	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}


	public HashMap<Long, StudentEntry> getListOfParticipants() {
		return listOfParticipants;
	}


	public void setListOfParticipants(HashMap<Long, StudentEntry> listOfParticipants) {
		this.listOfParticipants = listOfParticipants;
	}


	public ArrayList<Student> getNetWaitingList() {
		return netWaitingList;
	}


	public void setNetWaitingList(ArrayList<Student> netWaitingList) {
		this.netWaitingList = netWaitingList;
	}


	public ArrayList<Student> getNetWaitingListBanned() {
		return netWaitingListBanned;
	}


	public void setNetWaitingListBanned(ArrayList<Student> netWaitingListBanned) {
		this.netWaitingListBanned = netWaitingListBanned;
	}


	public HashMap<Long, StudentEntry> getWaitingList() {
		return waitingList;
	}


	public void setWaitingList(HashMap<Long, StudentEntry> waitingList) {
		this.waitingList = waitingList;
	}


	public Image getImage() {
		return image;
	}


	public void setImage(Image image) {
		this.image = image;
	}


	public String toString() {
		return title;
	}
}
