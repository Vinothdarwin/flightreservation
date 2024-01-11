package com.apm.demo.models;

import lombok.*;

import javax.persistence.*;

@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bookings")

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    private Integer tickets;

    @Column(name = "booking_time", nullable = false, columnDefinition = "bigint(20)")
    private Long bookingTime;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    public Integer getTickets() {
        return tickets;
    }

    public void setTickets(Integer tickets) {
        this.tickets = tickets;
    }

    public Long getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(Long bookingTime) {
        this.bookingTime = bookingTime;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Booking [id=" + id + ", customer=" + customer + ", flight=" + flight + ", tickets=" + tickets
				+ ", bookingTime=" + bookingTime + "]";
	}
  
    
    
}