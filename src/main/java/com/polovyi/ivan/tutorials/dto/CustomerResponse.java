package com.polovyi.ivan.tutorials.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerResponse {

    private Integer id;

    private String fullName;

    private String phoneNumber;

    private LocalDate createdAt;

    private Set<PurchaseTransactionResponse> purchaseTransactions;

    public static CustomerResponse valueOf(Customer customer) {
        return builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .phoneNumber(customer.getPhoneNumber())
                .createdAt(customer.getCreatedAt())
                .build();
    }
}
