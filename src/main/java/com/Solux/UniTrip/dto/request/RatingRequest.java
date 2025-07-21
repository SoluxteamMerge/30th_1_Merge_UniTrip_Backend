package com.Solux.UniTrip.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RatingRequest {
    @NotNull(message = "별점은 필수입니다.")
    @DecimalMin(value = "0.5", message = "별점은 0.5 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "별점은 5.0 이하여야 합니다.")
    @Digits(integer = 1, fraction = 1, message = "별점은 소수점 첫째 자리까지 허용됩니다.")
    private Double rating;
}
