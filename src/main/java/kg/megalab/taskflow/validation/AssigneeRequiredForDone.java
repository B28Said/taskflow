
package kg.megalab.taskflow.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AssigneeRequiredForDoneValidator.class)
public @interface AssigneeRequiredForDone {

    String message() default "Нельзя закрыть задачу без исполнителя";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
