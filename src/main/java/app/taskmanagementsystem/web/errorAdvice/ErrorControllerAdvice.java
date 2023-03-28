package app.taskmanagementsystem.web.errorAdvice;

import app.taskmanagementsystem.domain.exception.ObjNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ObjNotFoundException.class})
    public ModelAndView onObjectNotFound() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("/error/object-not-found");

        return modelAndView;
    }
}
