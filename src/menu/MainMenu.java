package menu;

import api.AdminResource;
import api.HotelResource;
import exception.ValidateDateException;
import model.*;
import service.CustomerService;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainMenu {
    Scanner scanner = new Scanner(System.in);

    private final AdminResource adminResource = AdminResource.getInstance();
    private final HotelResource hotelResource = HotelResource.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

    public void run() throws InterruptedException, ParseException, ValidateDateException {
        while(true) {
            displayMainMenu();
            String userChoice = scanner.nextLine();
            switch (userChoice) {
                case "1" -> findAndReserveARoom();
                case "2" -> customerReservation();
                case "3" -> createAccount();
                case "4" -> adminMenu();
                case "5" -> exitApplication();
                default -> System.out.println("Invalid selection. Routing to main menu");
            }
        }
    }


    private void createAccount() throws InterruptedException {
        System.out.println("Loading customer registration form");
        Thread.sleep(2000);
        System.out.println("Enter email format: name@gmail.com");
        String email = scanner.nextLine();
        System.out.println("First Name");
        String firstName = scanner.nextLine();
        System.out.println("Last Name");
        String lastName = scanner.nextLine();
        CustomerService.getInstance().addCustomer(email, firstName, lastName);
        System.out.println("Account created successfully");
    }

    private void findAndReserveARoom() throws InterruptedException {
        System.out.println("Loading reservation page................");
        Thread.sleep(2000);
        Date checkIn = null;
        Date checkout = null;
        while(checkIn == null) {
            try {
                System.out.println("Enter Checkin Date mm/dd/yyyy example 02/01/2020");
                String checkInDate = scanner.nextLine();
                checkIn = formatter.parse(checkInDate);
            } catch (Exception e) {
                System.out.println("Invalid checkin date entered");
            }
        }
        while(checkout == null){
            try {
                System.out.println("Enter CheckOut Date mm/dd/yyyy example 02/01/2020");
                String checkOutDate = scanner.nextLine();
                checkout = formatter.parse(checkOutDate);
            } catch (Exception e) {
                System.out.println("Invalid checkout date");
            }
        }
        System.out.println("Loading all available rooms .....");
        Thread.sleep(2000);
        Collection<IRoom> availableRooms = hotelResource.findARoom(checkIn, checkout);
        if (availableRooms.isEmpty()) {
            System.out.println("There are no rooms available for these days");
            System.out.println("would you like to search another date y/n");
            String response = scanner.nextLine();
            validateUserResponse(response);
            if (response.equalsIgnoreCase("y")){
                findAndReserveARoom();
            }
        }else {
            getAvailableRooms(availableRooms);
            reserveARoom(checkIn, checkout);
        }
    }

    private void getAvailableRooms(Collection<IRoom> rooms)  {
        displayAvailableRooms(rooms);

    }

    private void reserveARoom(Date checkIn, Date checkOut) throws InterruptedException {
        System.out.println("Would you like to book a room? y/n");
        String response = scanner.nextLine();
        response = validateUserResponse(response);
        if (response.equalsIgnoreCase("y")) {
            bookARoomForUser(checkIn,checkOut);
        } else {
            System.out.println("Taking you back to the main menu");
            displayMainMenu();
        }

    }

    private void displayAvailableRooms(Collection<IRoom> rooms){
            printRoomInformation(rooms);
    }

    private void printRoomInformation(Collection<IRoom> rooms){
        AtomicInteger counter = new AtomicInteger(1);
        rooms.forEach(room -> {
            String roomInfo =
                    counter.get() + ". "
                            + "Room Number: " + room.getRoomNumber() + " " + room.getRoomType() + " bed Room "
                            + "Price: " + "$"+room.getRoomPrice();
            System.out.println(roomInfo);
            counter.getAndIncrement();
        });
        System.out.println("\n---------------------------------------------\n");
    }

    private void customerReservation() throws InterruptedException {
        System.out.println("Please enter your email id to retrieve all your reservations");
        String email = scanner.nextLine();
        System.out.println("Retrieving all reservation for " + email + " In the database");
        Thread.sleep(2000);
        Customer customer = CustomerService.getInstance().getCustomer(email);
        Collection<Reservation> customerReservation = hotelResource.getCustomersReservations(customer);
        if (customerReservation.isEmpty()) {
            System.out.println("There are currently no reservation for this customer in the DB");
            Thread.sleep(2000);
            System.out.println("Redirecting you to the Main Menu");
            Thread.sleep(2000);

        } else {
            customerReservation.forEach(adminResource::printReservationDetails);
            System.out.println("\n---------------------------------------------\n");
        }
    }

    private void adminChoice() throws InterruptedException, ParseException {
        String userChoice = scanner.nextLine();
        switch (userChoice) {

            case "1" -> {
                getAllCustomers();
                adminMenu();
            }
            case "2" -> {
                getAllRooms();
                adminMenu();
            }
            case "3" -> {
                getAllReservation();
                adminMenu();
            }
            case "4" -> {
                addARoom();
                adminMenu();
            }
            case "5" -> {
                addTestData();
                adminMenu();
            }

        }
    }

    private void getAllCustomers() {
        Collection<Customer> customerList = AdminResource.getInstance().getAllCustomer();
        if (customerList.isEmpty()) {
            System.out.println("There are currently no customer in the DB");
        } else {
            System.out.println("""
                    Customers
                    ---------------------------------------------
                    """);
            AtomicInteger counter = new AtomicInteger(1);
            customerList.forEach(customer -> {
                String customerInfo =
                        counter.get() + " "
                        +"First Name: " + customer.getFirstName() + " "
                        +"Last Name: " + customer.getLastName() + " "
                        + "Email: " + customer.getEmail();
                System.out.println(customerInfo);
                counter.getAndIncrement();
            });
            System.out.println("\n---------------------------------------------\n");
        }

    }

    private void getAllRooms() {
        Collection<IRoom> rooms = AdminResource.getInstance().getAllRoom();
        System.out.println("Rooms" + "\n---------------------------------------------\n");
        if (rooms.isEmpty()) {
            System.out.println("There are no current room in the DB");
        } else {
           printRoomInformation(rooms);
        }
    }

    private void getAllReservation() {
       adminResource.displayAllReservations();
    }

    public String validateUserResponse(String response) {
        boolean notValid = true;
        while(notValid){
            if (!(response.equalsIgnoreCase("y") || response.equalsIgnoreCase("n"))){
                System.out.println("Enter Y (Yes) or N (No)");
                response = scanner.nextLine();
            }else notValid = false;
        }
        return response;
    }

    private void addARoom() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter room Number");
        String roomNumber = scanner.nextLine();

        System.out.println("Enter price per night");
        double roomPrice = scanner.nextDouble();

        System.out.println("Enter the room type: 1 for single, 2 for double");
        scanner.nextLine();
        String roomType = scanner.nextLine();

        while(true){
            if (!(roomType.equals("1") || roomType.equals("2"))){
                System.out.println("Enter 1 or 2");
                roomType = scanner.nextLine();
            }else break;
        }
        RoomType roomTypeEnum;
        if (roomType.equals("1")) {
            roomTypeEnum = RoomType.SINGLE;
        } else {
            roomTypeEnum = RoomType.DOUBLE;
        }
        IRoom roomToAdd = new Room(roomNumber, roomPrice, roomTypeEnum,true);
        adminResource.addRoom(Collections.singleton(roomToAdd));
        System.out.println("Would you like to add another room y/n");
        String response = scanner.nextLine();
        validateUserResponse(response);
        if(response.equalsIgnoreCase("y")){
            addARoom();
        }
    }


    public void adminMenu() throws InterruptedException, ParseException {
        System.out.println("Loading Admin page Menu...............");
        Thread.sleep(3000);
        String adminMenu = """
                Admin Menu
                ---------------------------------------------
                1. See all customers
                2. See all rooms
                3. See all reservations
                4. Add a room
                5. Add a test data
                6. Back to main menu\s
                ---------------------------------------------
                Please select a number for the menu option""";
        System.out.println(adminMenu);
        adminChoice();
    }

    private void displayMainMenu() throws InterruptedException {
        System.out.println("Loading Main menu page Menu...............");
        Thread.sleep(2000);
        String menuSB = """

                Welcome to the Hotel Reservation Application
                ---------------------------------------------
                1. Find and reserve a room
                2. See my reservations
                3. Create an account
                4. Admin
                5. Exit
                ---------------------------------------------
                Please select a number for the menu option""";
        System.out.println(menuSB);
    }

    private void exitApplication() {
        System.exit(0);
    }

    private void bookARoomForUser(Date checkIn, Date checkOut) throws InterruptedException {
        System.out.println("Do you have an account with us? y/n");
        String userHasAccountResponse = scanner.nextLine();
        validateUserResponse(userHasAccountResponse);
        if (userHasAccountResponse.equalsIgnoreCase("y")) {
            System.out.println("Enter your email address");
            String emailAddress = scanner.nextLine();
            System.out.println("Loading the db to get customer info...");
            Thread.sleep(2000);
            Customer customer = hotelResource.getCustomer(emailAddress);
            if (customer == null) {
                System.out.println("You are not a registered member");
                System.out.println("Redirecting you to the main menu where you can create an account");
                Thread.sleep(2000);
                displayMainMenu();
            } else {
                System.out.println("user found for " + emailAddress);
                Thread.sleep(2000);
                System.out.println("what room would you like to reserve");
                String roomNumber = scanner.nextLine();
                try {
                    IRoom room = hotelResource.getRoom(roomNumber);
                    while(room == null){
                        System.out.println("Please enter a valid room number");
                        roomNumber = scanner.nextLine();
                        room = hotelResource.getRoom(roomNumber);
                    }
                    Reservation reservation = hotelResource.bookARoom(customer, room, checkIn, checkOut);
                    System.out.println("Room " + roomNumber + " Successfully Reserved loading reservation details.....");
                    adminResource.printReservationDetails(reservation);
                    Thread.sleep(2000);
                }catch (ValidateDateException e){
                    System.out.println(e.getMessage());
                }
            }

        } else {
            System.out.println("Would you want to register an account? y/n");
            String registerAccountResponse = scanner.nextLine();
            validateUserResponse(registerAccountResponse);
            if (registerAccountResponse.equalsIgnoreCase("y")) {
                System.out.println("Redirecting you to the main menu where you can create an account");
                Thread.sleep(2000);
                displayMainMenu();
            }
        }
    }
    private void addTestData() throws ParseException, InterruptedException {
        System.out.println("Populating room db with test data..........");
        Collection<IRoom> roomsToBeAdded = Arrays.asList(
                new Room("100",67.23,RoomType.DOUBLE,true),
                new Room("200",67.23,RoomType.SINGLE,true),
                new Room("300",67.23,RoomType.DOUBLE,true),
                new Room("400",67.23,RoomType.SINGLE,true),
                new Room("500",67.23,RoomType.DOUBLE,true),
                new FreeRoom("600",RoomType.SINGLE,true),
                new FreeRoom("600",RoomType.DOUBLE,true)
                );
        Thread.sleep(2000);
        System.out.println("populating customer db with test data..........");
        adminResource.addRoom(roomsToBeAdded);
        Collection<Customer> customers = Arrays.asList(
                new Customer("Oludamilare", "Olukotun","olukotuno@gmail.com"),
                new Customer("Jeff", "keneth","jeff@gmail.com"));
        customers.forEach(customer -> hotelResource.createCustomer(customer.getEmail(), customer.getFirstName(), customer.getLastName()));
        Thread.sleep(2000);
        Date checkIn = formatter.parse("12/24/2022");
        Date checkOut = formatter.parse("12/25/2022");
        System.out.println("populating reservation with a test data.......");
        try {
            hotelResource.bookARoom(hotelResource.getCustomer("olukotuno@gmail.com"),hotelResource.getRoom("100"),checkIn,checkOut);
            hotelResource.bookARoom(hotelResource.getCustomer("jeff@gmail.com"),hotelResource.getRoom("300"),checkIn,checkOut);

        } catch (ValidateDateException e) {
            System.out.println(e.getMessage());
        }
        Thread.sleep(2000);
        System.out.println("Done with populating test data");

    }
}
