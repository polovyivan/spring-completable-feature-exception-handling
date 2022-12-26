package com.polovyi.ivan.tutorials.configuration;

import com.github.javafaker.CreditCardType;
import com.github.javafaker.Faker;
import com.polovyi.ivan.tutorials.dto.Customer;
import com.polovyi.ivan.tutorials.dto.PurchaseTransaction;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Component
@RequiredArgsConstructor
public class DataLoader {
    private Map<Integer, Customer> customers = new HashMap<>();
    private Map<Integer, Set<PurchaseTransaction>> purchaseTransactionResponses = new HashMap<>();

    @Bean
    private InitializingBean initClients() {
        Faker faker = new Faker();
        return () ->
                IntStream.range(1, 10)
                        .forEach(i -> {
                            Customer customer = createCustomer(i, faker);
                            customers.put(customer.getId(), customer);
                            Set<PurchaseTransaction> purchaseTransactionList = generatePurchaseTransactionList(
                                    faker);
                            purchaseTransactionResponses.put(customer.getId(), purchaseTransactionList);
                        });
    }

    private Customer createCustomer(int customerId, Faker faker) {
        return Customer.builder()
                .id(customerId)
                .createdAt(
                        LocalDate.now().minus(Period.ofDays((new Random().nextInt(365 * 10)))))
                .fullName(faker.name().fullName())
                .phoneNumber(faker.phoneNumber().cellPhone())
                .build();
    }

    private Set<PurchaseTransaction> generatePurchaseTransactionList(Faker faker) {
        return IntStream.range(0, new Random().nextInt(10 - 1 + 1) + 1)
                .mapToObj(i -> PurchaseTransaction.builder()
                        .id(UUID.randomUUID().toString())
                        .createdAt(
                                LocalDate.now().minus(Period.ofDays((new Random().nextInt(365 * 10)))))
                        .amount(new BigDecimal(faker.commerce().price().replaceAll(",", ".")))
                        .paymentType(List.of(CreditCardType.values())
                                .get(new Random().nextInt(CreditCardType.values().length)).toString())
                        .build())
                .collect(Collectors.toSet());
    }
}


