package com.polovyi.ivan.tutorials.service;

import com.polovyi.ivan.tutorials.client.CustomerClient;
import com.polovyi.ivan.tutorials.client.PurchaseTransactionClient;
import com.polovyi.ivan.tutorials.dto.CustomerResponse;
import com.polovyi.ivan.tutorials.dto.PurchaseTransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public record CustomerService(
        CustomerClient customerClient,
        PurchaseTransactionClient purchaseTransactionClient) {

    public CustomerResponse getCustomerByIdUsingExceptionally(Integer customerId) {
        log.info("Getting customer by id {} ", customerId);
        int availableProcessors = getAvailableProcessors();
        LocalDateTime startTime = LocalDateTime.now();
        log.info("====> {} available processors. <====", availableProcessors);
        CompletableFuture<CustomerResponse> customerResponseCF = CompletableFuture.supplyAsync(
                () -> CustomerResponse.valueOf(customerClient.getCustomerById(customerId)));

        CompletableFuture<Set<PurchaseTransactionResponse>> purchaseTransactionsCF = CompletableFuture.supplyAsync(
                        () -> purchaseTransactionClient.getPurchaseTransactionsByCustomerId(customerId, isException(customerId)).stream()
                                .map(PurchaseTransactionResponse::valueOf)
                                .collect(Collectors.toSet()))
                //  todo               invoked only when exception thrown
                .exceptionally(ex -> {
                    log.error("Received exception, returning empty list.");
                    return Set.of();
                });
        CompletableFuture<CustomerResponse> customerResponseCompletableFuture = customerResponseCF
                .thenCombine(purchaseTransactionsCF, (customerResponse, purchaseTransactions) -> {
                    customerResponse.setPurchaseTransactions(purchaseTransactions);
                    return customerResponse;
                });
        CustomerResponse response = customerResponseCompletableFuture.join();
        log.info("Operation duration {} sec", Duration.between(startTime, LocalDateTime.now()).toSeconds());
        return response;
    }

    public CustomerResponse getCustomerByIdUsingHandle(Integer customerId) {
        log.info("Getting customer by id {} ", customerId);
        int availableProcessors = getAvailableProcessors();
        LocalDateTime startTime = LocalDateTime.now();
        log.info("====> {} available processors. <====", availableProcessors);
        CompletableFuture<CustomerResponse> customerResponseCF = CompletableFuture.supplyAsync(
                () -> CustomerResponse.valueOf(customerClient.getCustomerById(customerId)));

        CompletableFuture<Set<PurchaseTransactionResponse>> purchaseTransactionsCF = CompletableFuture.supplyAsync(
                        () -> purchaseTransactionClient.getPurchaseTransactionsByCustomerId(customerId, isException(customerId)).stream()
                                .map(PurchaseTransactionResponse::valueOf)
                                .collect(Collectors.toSet()))
                .handle((response, ex) -> {
                    log.info("Executing exception handler for purchase transaction CF...");
                    if (ex != null) {
                        log.error("Received exception, returning empty list.");
                        return Collections.EMPTY_SET;
                    }
                    return response;
                });
        CompletableFuture<CustomerResponse> customerResponseCompletableFuture = customerResponseCF
                .thenCombine(purchaseTransactionsCF, (customerResponse, purchaseTransactions) -> {
                    customerResponse.setPurchaseTransactions(purchaseTransactions);
                    return customerResponse;
                });
        CustomerResponse response = customerResponseCompletableFuture.join();
        log.info("Operation duration {} sec", Duration.between(startTime, LocalDateTime.now()).toSeconds());
        return response;
    }

    public CustomerResponse getCustomerByIdUsingWhenComplete(Integer customerId) {
        log.info("Getting customer by id {} ", customerId);
        int availableProcessors = getAvailableProcessors();
        LocalDateTime startTime = LocalDateTime.now();
        log.info("====> {} available processors. <====", availableProcessors);
        CompletableFuture<CustomerResponse> customerResponseCF = CompletableFuture.supplyAsync(
                () -> CustomerResponse.valueOf(customerClient.getCustomerById(customerId)));

        CompletableFuture<Set<PurchaseTransactionResponse>> purchaseTransactionsCF = CompletableFuture.supplyAsync(
                        () -> purchaseTransactionClient.getPurchaseTransactionsByCustomerId(customerId, isException(customerId)).stream()
                                .map(PurchaseTransactionResponse::valueOf)
                                .collect(Collectors.toSet()))
//           todo     catches the exception but does not recover
                .whenComplete((response, ex) -> {
                    log.error("Received exception...");
                });
        CompletableFuture<CustomerResponse> customerResponseCompletableFuture = customerResponseCF
                .thenCombine(purchaseTransactionsCF, (customerResponse, purchaseTransactions) -> {
                    customerResponse.setPurchaseTransactions(purchaseTransactions);
                    return customerResponse;
                });
        CustomerResponse response = customerResponseCompletableFuture.join();
        log.info("Operation duration {} sec", Duration.between(startTime, LocalDateTime.now()).toSeconds());
        return response;
    }

    public CustomerResponse getCustomerByIdWithOrTimeout(Integer customerId) {
        log.info("Getting customer by id {} ", customerId);
        int availableProcessors = getAvailableProcessors();
        LocalDateTime startTime = LocalDateTime.now();
        log.info("====> {} available processors. <====", availableProcessors);
        int timeOut = getTimeOut(customerId);
        log.info("CF timeout is {}", timeOut);
        CompletableFuture<CustomerResponse> customerResponseCF = CompletableFuture.supplyAsync(
                () -> CustomerResponse.valueOf(customerClient.getCustomerById(customerId)));

        CompletableFuture<Set<PurchaseTransactionResponse>> purchaseTransactionsCF = CompletableFuture.supplyAsync(
                        () -> purchaseTransactionClient.getPurchaseTransactionsByCustomerId(customerId, false).stream()
                                .map(PurchaseTransactionResponse::valueOf)
                                .collect(Collectors.toSet()))
                .orTimeout(timeOut, TimeUnit.SECONDS);

        CompletableFuture<CustomerResponse> customerResponseCompletableFuture = customerResponseCF
                .thenCombine(purchaseTransactionsCF, (customerResponse, purchaseTransactions) -> {
                    customerResponse.setPurchaseTransactions(purchaseTransactions);
                    return customerResponse;
                });
        CustomerResponse response = customerResponseCompletableFuture.join();
        log.info("Operation duration {} sec", Duration.between(startTime, LocalDateTime.now()).toSeconds());
        return response;
    }

    public CustomerResponse getCustomerByIdWithCompleteOnTimeout(Integer customerId) {
        log.info("Getting customer by id {} ", customerId);
        int availableProcessors = getAvailableProcessors();
        LocalDateTime startTime = LocalDateTime.now();
        log.info("====> {} available processors. <====", availableProcessors);
        int timeOut = getTimeOut(customerId);
        log.info("CF timeout is {}", timeOut);
        CompletableFuture<CustomerResponse> customerResponseCF = CompletableFuture.supplyAsync(
                () -> CustomerResponse.valueOf(customerClient.getCustomerById(customerId)));

        CompletableFuture<Set<PurchaseTransactionResponse>> purchaseTransactionsCF = CompletableFuture.supplyAsync(
                        () -> purchaseTransactionClient.getPurchaseTransactionsByCustomerId(customerId, false).stream()
                                .map(PurchaseTransactionResponse::valueOf)
                                .collect(Collectors.toSet()))
                .completeOnTimeout(Set.of(), timeOut, TimeUnit.SECONDS);

        CompletableFuture<CustomerResponse> customerResponseCompletableFuture = customerResponseCF
                .thenCombine(purchaseTransactionsCF, (customerResponse, purchaseTransactions) -> {
                    customerResponse.setPurchaseTransactions(purchaseTransactions);
                    return customerResponse;
                });
        CustomerResponse response = customerResponseCompletableFuture.join();
        log.info("Operation duration {} sec", Duration.between(startTime, LocalDateTime.now()).toSeconds());
        return response;
    }

    private static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    private static int getTimeOut(Integer customerId) {
        if (customerId % 2 == 0) {
            return 2;
        }
        return 5;
    }

    private static boolean isException(Integer customerId) {
        if (customerId % 2 == 0) {
            return true;
        }
        return false;
    }
}
