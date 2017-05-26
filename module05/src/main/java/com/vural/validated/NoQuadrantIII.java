package com.vural.validated;

/**
 * Created by vural on 25-May-17.
 */
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {QuadrantIIIValidator.class})
@Documented
public @interface NoQuadrantIII {
    String message() default "Failed quadrant III test";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}