package com.apm.demo.models;

public class CustomerDetails {
	long id;
	String userName;
	String fname;
	String lname;
	String email;
	String flightName;
	Long time;
	String date;
	public CustomerDetails(long id, String userName, String fname, String lname, String email, String flightName,
			Long time, String date, String source, String destination, int tickets, int amount) {
		super();
		this.id = id;
		this.userName = userName;
		this.fname = fname;
		this.lname = lname;
		this.email = email;
		this.flightName = flightName;
		this.time = time;
		this.date = date;
		this.source = source;
		this.destination = destination;
		this.tickets = tickets;
		this.amount = amount;
	}
	public Long getTime() {
		return time;
	}
	public void setTime(Long time) {
		this.time = time;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	String source;
	String destination;
	int tickets;
	int amount;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFlightName() {
		return flightName;
	}
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public int getTickets() {
		return tickets;
	}
	public void setTickets(int tickets) {
		this.tickets = tickets;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	@Override
	public String toString() {
		return "CustomerDetails [id=" + id + ", userName=" + userName + ", fname=" + fname + ", lname=" + lname
				+ ", email=" + email + ", flightName=" + flightName + ", time=" + time + ", date=" + date + ", source="
				+ source + ", destination=" + destination + ", tickets=" + tickets + ", amount=" + amount + "]";
	}


}
