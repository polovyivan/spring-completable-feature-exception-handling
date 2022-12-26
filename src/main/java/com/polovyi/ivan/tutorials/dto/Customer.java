package com.polovyi.ivan.tutorials.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private Integer id;

    private String fullName;

    private String phoneNumber;

    private String address;

    private LocalDate createdAt;

}