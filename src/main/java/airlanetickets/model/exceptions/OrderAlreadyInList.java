package airlanetickets.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class OrderAlreadyInList extends RuntimeException{

    public OrderAlreadyInList(Long id) {
        super(String.format("Product with id: %d already exists in order", id));

    }
}
