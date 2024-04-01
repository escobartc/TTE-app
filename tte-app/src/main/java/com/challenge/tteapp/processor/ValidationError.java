package com.challenge.tteapp.processor;

import com.challenge.tteapp.model.StructureError;
import com.challenge.tteapp.model.ErrorModel;
import org.springframework.stereotype.Component;

@Component
public class ValidationError {

    public StructureError getStructureError(String message, String code) {
        StructureError structureError = new StructureError();
        structureError.setError(new ErrorModel(message, code));
        return structureError;
    }
}
