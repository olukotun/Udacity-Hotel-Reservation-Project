import exception.ValidateDateException;
import menu.MainMenu;
import model.FreeRoom;
import model.Room;
import model.RoomType;
import service.CustomerService;
import service.ReservationService;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Tester {
    public static void main(String[] args) throws ParseException, ValidateDateException, InterruptedException {
        MainMenu menu = new MainMenu();
        menu.run();
//        Scanner scanner = new Scanner(System.in);
//        CustomerService customerService = CustomerService.getInstance();
//        ReservationService reservationService = ReservationService.getInstance();
//        customerService.addCustomer("olukotuno@gmail.com","Oludamilare", "Olukotun");
//        customerService.addCustomer("Erhuvwu@gmail.com","Erhuvwu","Olukotun");
//
//        reservationService.addRoom(new Room("123",23.00, RoomType.DOUBLE));
//        reservationService.addRoom(new Room("124",23.00, RoomType.DOUBLE));
//        reservationService.addRoom(new FreeRoom("345",RoomType.SINGLE, true));
//        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
//        Date checkInDate = formatter.parse("12/10/2022");
//        Date checkOutDate = formatter.parse("12/11/2022");
//        Date anotherCheckInDate = formatter.parse("12/14/2022");
//        Date anotherCheckOutDate = formatter.parse("12/15/2022");
//        Date anotherOneCheckin = formatter.parse("12/16/2022");
//        Date anotherOneCheckout = formatter.parse("12/20/2022");
//
//        Date in = formatter.parse("12/21/2022");
//        Date out = formatter.parse("12/22/2022");
//
//        System.out.println(checkInDate);
//        reservationService.reserveARoom(customerService.getCustomer("olukotuno@gmail.com"), reservationService.getARoom("123"), checkInDate,checkOutDate);
//        reservationService.reserveARoom(customerService.getCustomer("olukotuno@gmail.com"), reservationService.getARoom("123"), checkInDate,checkOutDate);
//        reservationService.reserveARoom(customerService.getCustomer("Erhuvwu@gmail.com"), reservationService.getARoom("123"), anotherOneCheckin,anotherOneCheckout);
//        reservationService.reserveARoom(customerService.getCustomer("olukotuno@gmail.com"), reservationService.getARoom("124"), anotherOneCheckin,anotherOneCheckout);
//
//
//        System.out.println(reservationService.findRooms(in, out));



        //test cases
        // when customer has an existing reservation and wanted to book the same room should be possible when the checkout date of current booking is before checking date reservation
        //When customer with existing reservation want to book the same room should not be possible if checkout date of current is after checking date of reservation
        // when a customer has an existing reservation and want to book another room should be possible if the room is available at the checking date

    }

}