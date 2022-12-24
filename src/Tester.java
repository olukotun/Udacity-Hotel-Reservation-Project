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
        //test cases
        // when customer has an existing reservation and wanted to book the same room should be possible when the checkout date of current booking is before checking date reservation
        //When customer with existing reservation want to book the same room should not be possible if checkout date of current is after checking date of reservation
        // when a customer has an existing reservation and want to book another room should be possible if the room is available at the checking date

    }

}