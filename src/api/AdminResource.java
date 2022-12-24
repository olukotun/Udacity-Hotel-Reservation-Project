package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    private final CustomerService customerService = CustomerService.getInstance();
    private final ReservationService reservationService = ReservationService.getInstance();
    private static final AdminResource INSTANCE = new AdminResource();

    private AdminResource() {

    }

    public static AdminResource getInstance() {
        return INSTANCE;
    }

    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addRoom(Collection<IRoom> rooms) {
        for (IRoom room : rooms) {
            reservationService.addRoom(room);
        }
    }
    public Collection<Customer> getAllCustomer(){
        return customerService.getAllCustomer();
    }
    public void displayAllReservations(){
        Collection<Reservation> getAllReservation =reservationService.printAllReservations();
        if (getAllReservation.isEmpty()) {
            System.out.println("There are no current reservation");
        } else {
            getAllReservation.forEach(this::printReservationDetails);
        }
        reservationService.printAllReservations();
    }
    public Collection<IRoom> getAllRoom(){
       return reservationService.getAllRoom();
    }
    public void printReservationDetails(Reservation reservation){
        String reservationInfo = "Reservation" + "\n---------------------------------------------\n"
                + reservation.getCustomer().getFirstName() + " " + reservation.getCustomer().getLastName() + "\n"
                + "Room: " + reservation.getRoom().getRoomNumber() + " - " + reservation.getRoom().getRoomType() + " bed" + "\n"
                + "Price: " + "$"+reservation.getRoom().getRoomPrice() + " price per night" + "\n"
                + "Checkin Date: " + reservation.getCheckInDate() + "\n"
                + "Checkout Date: " + reservation.getCheckoutDate();
        System.out.println(reservationInfo);

    }

}
