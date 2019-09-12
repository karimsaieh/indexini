package tn.insat.pfe.filemanagementservice.validators;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFileTypesValidator.class)
public @interface ValidFileTypes {

    String message() default "file types should be in pdf|doc|zip|tar.gz|rar|docx|txt|rtf|png|jpg|jpeg";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
