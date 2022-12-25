package service;

import exception.ValidateDateException;
import model.*;

import java.util.*;
import java.util.stream.Collectors;

public class ReservationService {
    private static final ReservationService INSTANCE = new ReservationService();
    private final Map<String, IRoom> roomDB = new HashMap<>();
    private final Map<String, Collection<Reservation>> reservationDB = new HashMap<>();

    private ReservationService() {

    }

    public void addRoom(IRoom room) {
        roomDB.put(room.getRoomNumber(), room);
    }

    public IRoom getARoom(String roomId) {
        return roomDB.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) throws ValidateDateException {
        // validate dates are not in past
        ValidationService.getInstance().validateDate(checkInDate,checkOutDate);
        // Get existing reservation for current customer
        Collection<Reservation> existingCustomerReservation = reservationDB.get(customer.getEmail());

        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        if (existingCustomerReservation != null) {
            //customer already has a room reserved adding to existing reservation
            List<String> roomNumberCurrentlyReserved = getExistingReservedRoom();
            if (roomNumberCurrentlyReserved.contains(room.getRoomNumber())) {
                // Get reservations for that particular room
                List<Reservation> currentReservationContainingRoom = getReservation(room.getRoomNumber());
                // get the latest checkout date before room is available
                Date latestCheckoutDate = getCheckOutDateBeforeRoomIsAvailable(currentReservationContainingRoom);
                // customer can book the same room if check in date is in the future
                if (checkInDate.after(latestCheckoutDate))
                    reserveRoomForCustomerWithExistingReservation(existingCustomerReservation, reservation, customer.getEmail());
                else
                    throw new ValidateDateException("The earliest date this room is free is " + dateRoomIsAvailable(latestCheckoutDate));
            } else {
                // existing customer booking a different room
                // add to existing customer reservation
                reserveRoomForCustomerWithExistingReservation(existingCustomerReservation, reservation, customer.getEmail());
            }
        } else {
            // guarding against any sneaker new customer who want to book an existing room
            List<Reservation> currentReservationContainingRoom = getReservation(room.getRoomNumber());
            if (currentReservationContainingRoom.isEmpty()) { // if the room is free
                reserveRoomForCustomerWithoutExistingReservation(reservation, customer.getEmail());
            } else {
                // if not free check the latest checkout date for the room
                Date latestCheckoutDate = getCheckOutDateBeforeRoomIsAvailable(currentReservationContainingRoom);
                if (checkInDate.after(latestCheckoutDate))
                    reserveRoomForCustomerWithoutExistingReservation(reservation, customer.getEmail());
                else
                    throw new RuntimeException("The earliest date this room is free is " + dateRoomIsAvailable(latestCheckoutDate));
            }
        }
        return reservation;

    }

    private void reserveRoomForCustomerWithoutExistingReservation(Reservation reservation, String customerEmail) {
        Collection<Reservation> newReservation = new ArrayList<>();
        newReservation.add(reservation);
        reservationDB.put(customerEmail, newReservation);
        updateRoomInfo(reservation.getRoom().getRoomNumber());
    }

    private void reserveRoomForCustomerWithExistingReservation(Collection<Reservation> existingReservations, Reservation reservation, String customerEmail) {
        existingReservations.add(reservation);
        reservationDB.put(customerEmail, existingReservations);
        updateRoomInfo(reservation.getRoom().getRoomNumber());
    }
    void updateRoomInfo(String roomNumber){
        IRoom room = roomDB.get(roomNumber);
        room.setIsFree(false);
        roomDB.put(room.getRoomNumber(),room);

    }

    private Date dateRoomIsAvailable(Date latestDateRoomIsAvailable) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(latestDateRoomIsAvailable);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    private Date getCheckOutDateBeforeRoomIsAvailable(List<Reservation> reservations) {
        List<Date> checkOutDates = reservations.stream().map(Reservation::getCheckoutDate).sorted().toList();
        return checkOutDates.get(checkOutDates.size() - 1);
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date CheckOutDate) {
        //Get all reservations
        Collection<IRoom> allAvailableRooms = new ArrayList<>();
        List<Reservation> allReservations = reservationDB.values().stream().flatMap(Collection::stream).toList();

        Map<String, Date> mapOfRoomAndLatestCheckoutDate = new HashMap<>();
        allReservations.forEach(reservation -> {
            // for each reservation, get room number
            String roomNumber = reservation.getRoom().getRoomNumber();
            if (mapOfRoomAndLatestCheckoutDate.get(roomNumber) != null) {
                Date currentCheckOutDate = mapOfRoomAndLatestCheckoutDate.get(roomNumber);
                if (currentCheckOutDate.before(reservation.getCheckoutDate())) {
                    //replace with the latest check out date
                    mapOfRoomAndLatestCheckoutDate.replace(roomNumber, reservation.getCheckoutDate());
                }
            } else {
                Date checkOutDate = reservation.getCheckoutDate();
                mapOfRoomAndLatestCheckoutDate.put(roomNumber, checkOutDate);
            }
        });

        Collection<IRoom> freeRoomsFromReservation = new ArrayList<>();
        mapOfRoomAndLatestCheckoutDate.forEach((roomNumber, date) -> {
            IRoom room = getARoom(roomNumber);
            if (date.before(checkInDate)) {
                room.setIsFree(true);
                freeRoomsFromReservation.add(room);
            }
        });
        // get all available room in the room
        allAvailableRooms.addAll(roomDB.values().stream().filter(IRoom::isFree).toList());
        allAvailableRooms.addAll(freeRoomsFromReservation);

        return new HashSet<>(allAvailableRooms);

    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {
        if(customer == null){
            return new ArrayList<>();
        }
        Collection<Reservation> reservations = reservationDB.get(customer.getEmail());
        if(reservations == null){
            return new ArrayList<>();
        }
        return reservations;
    }

    public Collection<Reservation> printAllReservations() {
     return reservationDB.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    public static ReservationService getInstance() {
        return INSTANCE;
    }

    private List<String> getExistingReservedRoom() {
        return reservationDB.values().stream().flatMap(reservation -> reservation.stream().map(Reservation::getRoom)).map(IRoom::getRoomNumber).collect(Collectors.toList());
    }

    private List<Reservation> getReservation(String roomId) {
        return reservationDB.values()
                .stream()
                .flatMap(reservation -> reservation.stream().filter(reservation1 -> reservation1.getRoom().getRoomNumber().equals(roomId)))
                .collect(Collectors.toList());
    }
    public Collection<IRoom> getAllRoom(){
        return roomDB.values();
    }

}
