package com.polovyi.ivan.tutorials.client;

import com.polovyi.ivan.tutorials.configuration.DataLoader;
import com.polovyi.ivan.tutorials.dto.PurchaseTransaction;
import com.polovyi.ivan.tutorials.utils.SleepUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PurchaseTransactionClient {

    private final DataLoader dataLoader;

    public Set<PurchaseTransaction> getPurchaseTransactionsByCustomerId(Integer customerId, boolean isException) {
        log.info("Getting purchase transactions by customerId {}", customerId);
        SleepUtils.loadingSimulator(4);
        if (isException) {
            log.error("The error occurred while trying to retrieve purchase transactions!");
            throw new RuntimeException();
        }
        return dataLoader.getPurchaseTransactionResponses().get(customerId);
    }
}
