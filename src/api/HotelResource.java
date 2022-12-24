package api;

import exception.ValidateDateException;
import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {

    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();
    private static final HotelResource INSTANCE = new HotelResource();

    private HotelResource(){

    }
    public static HotelResource getInstance(){
        return INSTANCE;
    }

    public Customer getCustomer(String email){
        return customerService.getCustomer(email);
    }

    public void createCustomer(String email, String firstName, String lastName){
       customerService.addCustomer(email,firstName,lastName);
    }

    public IRoom getRoom(String roomNumber){
        return reservationService.getARoom(roomNumber);
    }
    public Reservation bookARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) throws ValidateDateException {
        return reservationService.reserveARoom(customer,room,checkInDate,checkOutDate);

    }
    public Collection<Reservation> getCustomersReservations(Customer customer){
        return reservationService.getCustomersReservation(customer);
    }
    public Collection<IRoom> findARoom(Date checkIn, Date checkOut){
        return reservationService.findRooms(checkIn,checkOut);
    }

}
