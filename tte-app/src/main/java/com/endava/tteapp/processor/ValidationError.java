package com.endava.tteapp.processor;

import com.endava.tteapp.model.StructureError;
import com.endava.tteapp.model.ErrorModel;
import org.springframework.stereotype.Component;

@Component
public class ValidationError {

    public StructureError getStructureError(int code, String message){
        StructureError structureError = new StructureError();
        structureError.setError(new ErrorModel(code,message));
        return structureError;
    }
}
