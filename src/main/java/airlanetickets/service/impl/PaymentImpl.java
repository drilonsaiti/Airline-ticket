package airlanetickets.service.impl;

import airlanetickets.model.Payment;
import airlanetickets.model.exceptions.InvalidPaymentIdException;
import airlanetickets.repository.PaymentRepository;
import airlanetickets.service.PaymentSercvice;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class PaymentImpl implements PaymentSercvice {

    private final PaymentRepository paymentRepository;

    private final PasswordEncoder passwordEncoder;


    public PaymentImpl(PaymentRepository paymentRepository, PasswordEncoder passwordEncoder) {
        this.paymentRepository = paymentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Payment findById(Long id) {
        Payment payment = this.paymentRepository.findById(id).orElseThrow(InvalidPaymentIdException::new);

        return payment;
    }

    @Override
    public Payment create(String fullName, String cardNumber, String CVV2, String monthOfValidation, String yearOfValidation) {
        String encrpytedCVV2 = this.passwordEncoder.encode(CVV2);
        Payment payment = new Payment(fullName,cardNumber,encrpytedCVV2,monthOfValidation,yearOfValidation);


        return this.paymentRepository.save(payment);
    }
}
