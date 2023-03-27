package app.taskmanagementsystem.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,reason = "Object was not found...")
public class ObjNotFoundException extends RuntimeException {


}
