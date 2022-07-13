package airlanetickets.service;

import airlanetickets.model.Payment;

public interface PaymentSercvice {

    Payment create(String fullName, String cardNumber, String CVV2, String monthOfValidation, String yearOfValidation);

    Payment findById(Long id);

}
