package com.polovyi.ivan.tutorials.client;

import com.polovyi.ivan.tutorials.configuration.DataLoader;
import com.polovyi.ivan.tutorials.dto.Customer;
import com.polovyi.ivan.tutorials.utils.SleepUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerClient {

    private final DataLoader dataLoader;

    public Customer getCustomerById(Integer customerId) {
        log.info("Getting customer by id {}", customerId);
        SleepUtils.loadingSimulator(2);
        return dataLoader.getCustomers().get(customerId);
    }
}
