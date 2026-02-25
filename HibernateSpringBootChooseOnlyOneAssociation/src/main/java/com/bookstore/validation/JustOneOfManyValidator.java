package com.bookstore.validation;

import com.bookstore.entity.Review;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.stream.Stream;

public class JustOneOfManyValidator implements ConstraintValidator<JustOneOfMany, Review> {

    @Override
    public boolean isValid(Review review, ConstraintValidatorContext ctx) {

        return Stream.of(review.getBook(), review.getArticle(), review.getMagazine())
                .filter(Objects::nonNull)
                .count() == 1;
    }

}
