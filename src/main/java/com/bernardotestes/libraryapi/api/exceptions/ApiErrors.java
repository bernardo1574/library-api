package com.bernardotestes.libraryapi.api.exceptions;

import com.bernardotestes.libraryapi.exception.BusinessException;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ApiErrors {

    private final List<String> errors;
    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(objectError -> errors.add(objectError.getDefaultMessage()));
    }

    public ApiErrors(BusinessException ex) {
        this.errors = Collections.singletonList(ex.getMessage());
    }

    public ApiErrors(ResponseStatusException ex){
        this.errors = Collections.singletonList(ex.getReason());
    }

    public List<String> getErrors(){
        return errors;
    }
}
