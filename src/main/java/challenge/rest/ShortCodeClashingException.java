package challenge.rest;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;

@ResponseStatus( code = HttpStatus.NOT_FOUND, reason = "Unable to create a unique shortCode")
public class ShortCodeClashingException extends RuntimeException{
}

