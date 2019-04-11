package tn.insat.pfe.filemanagementservice.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class ValidFileTypesValidator implements ConstraintValidator<ValidFileTypes, String[]> {

    @Override
    public void initialize(ValidFileTypes constraintAnnotation) {
        // sonar lint wants me to put a comment in here
    }

    @Override
    public boolean isValid(String[] strings, ConstraintValidatorContext constraintValidatorContext) {
        String[] values = {"pdf", "doc", "zip", "tar.gz", "rar", "docx", "txt", "rtf", "png", "jpg", "jpeg"};
        for(String str: strings) {
            if (Arrays.stream(values).noneMatch(str::equals)) {
                return false;
            }
        }
        return true;
    }
}