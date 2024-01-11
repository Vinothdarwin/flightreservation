package com.apm.demo.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "flight_id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "source", nullable = false)
    private String source;
    @Column(name = "destination", nullable = false)
    private String destination;
    @Column(name = "date", nullable = false)
    private String date;
    @Column(name = "time", nullable = false)
    private Long time;
    @Column(name = "fare", nullable = false)
    private Integer fare;
    @Column(name = "seats", nullable = false)
    private Integer seats;
    private String image;
    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinTable(name = "flight_bookings", joinColumns = @JoinColumn(name = "flight_id"), inverseJoinColumns = @JoinColumn(name = "booking_id"))
    List<Booking> bookings;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Flight [id=" + id + ", name=" + name + ", source=" + source + ", destination=" + destination + ", date="
				+ date + ", time=" + time + ", fare=" + fare + ", seats=" + seats + ", bookings=" + bookings + "]";
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Integer getFare() {
		return fare;
	}

	public void setFare(Integer fare) {
		this.fare = fare;
	}

	public Integer getSeats() {
		return seats;
	}

	public void setSeats(Integer seats) {
		this.seats = seats;
	}

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
    
    
    
    
}
