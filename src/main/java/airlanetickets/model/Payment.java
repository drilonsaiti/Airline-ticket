package airlanetickets.model;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.transaction.Transactional;

@Data
@Entity
@Transactional
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long code;

    String fullName;

    String cardNumber;

    String CVV2;

    String monthOfValidation;

    String yearOfValidation;

    public Payment(String fullName, String cardNumber, String CVV2, String monthOfValidation, String yearOfValidation) {
        this.fullName = fullName;
        this.cardNumber = cardNumber;
        this.CVV2 = CVV2;
        this.monthOfValidation = monthOfValidation;
        this.yearOfValidation = yearOfValidation;
    }

    public Payment() {
    }

    public String getValidation(){
        return monthOfValidation+"/"+yearOfValidation;
    }

    public String encodeCardNumber(String cardnumberFromInput){
        System.out.println(cardnumberFromInput.length());
        String first = cardnumberFromInput.substring(0,5);
        String second = "*****";
        String third = cardnumberFromInput.substring(10,12);

        return first+second+third;
    }


}
