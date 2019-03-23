package me.star.springrestapi.events;


import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;

@Component
public class EventValidator {

    public void validate(EventDto eventDto, Errors errors){
        if(eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0){
            errors.reject("wrongPrices","value for Prices are wrong");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime()) ||
                endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())){
            //rejectValue = field error, reject = global error
            errors.rejectValue("endEventDateTime", "wrongValue", "endEventDateTime is wrong");
        }


        // TODO beginEventDateTime
        // TODO CloseEnrollentDateTime

    }
}
