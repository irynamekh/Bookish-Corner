package com.bookstore.dto.order;

import com.bookstore.model.Order;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequestDto {
    @NotNull
    private Order.Status status;
}
