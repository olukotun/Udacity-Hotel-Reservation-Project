package service;

import exception.ValidateDateException;

import java.util.Date;

public class ValidationService {
    private static final ValidationService INSTANCE = new ValidationService();
    private ValidationService(){

    }
    public static ValidationService getInstance(){
        return INSTANCE;
    }
    public void validateDate(Date checkInDate, Date checkOutDate) throws ValidateDateException {
        if (checkInDate.after(checkOutDate) || checkInDate.before(new Date()) || checkOutDate.before(new Date())) {
            throw new ValidateDateException("Check out date must be after check In or selected Dates must not be in the past");
        }
    }
}
