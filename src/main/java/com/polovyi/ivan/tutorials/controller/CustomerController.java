package com.polovyi.ivan.tutorials.controller;

import com.polovyi.ivan.tutorials.dto.CustomerResponse;
import com.polovyi.ivan.tutorials.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/customers")
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping("/using-exceptionally/{customerId}")
    public CompletableFuture<CustomerResponse> getCustomerByIdUsingExceptionally(@PathVariable Integer customerId) {
        return customerService.getCustomerByIdUsingExceptionally(customerId);
    }


    @GetMapping("/using-exceptionally-rethrow/{customerId}")
    public CompletableFuture<CustomerResponse> getCustomerByIdUsingException(@PathVariable Integer customerId) {
        return customerService.getCustomerByIdUsingExceptionallyRethrow(customerId);
    }

    @GetMapping("/using-handler/{customerId}")
    public CompletableFuture<CustomerResponse> getCustomerByIdUsingHandle(@PathVariable Integer customerId) {
        return customerService.getCustomerByIdUsingHandle(customerId);
    }

    @GetMapping("/using-when-complete/{customerId}")
    public CompletableFuture<CustomerResponse> getCustomerByIdUsingWhenComplete(@PathVariable Integer customerId) {
        return customerService.getCustomerByIdUsingWhenComplete(customerId);
    }

    @GetMapping("/timeout/{customerId}")
    public CompletableFuture<CustomerResponse> getCustomerByIdWithTimeout(@PathVariable Integer customerId) {
        return customerService.getCustomerByIdWithOrTimeout(customerId);
    }

    @GetMapping("/complete-on-timeout/{customerId}")
    public CompletableFuture<CustomerResponse> getCustomerByIdWithCompleteOnTimeout(@PathVariable Integer customerId) {
        return customerService.getCustomerByIdWithCompleteOnTimeout(customerId);
    }
}
