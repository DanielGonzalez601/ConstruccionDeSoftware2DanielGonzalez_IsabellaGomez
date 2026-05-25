package com.bank.config;

import com.bank.domain.service.LoanDisbursementDomainService;
import com.bank.domain.service.TransferDomainService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
* CONFIG - AppConfig
*
* Conecta los servicios de dominio como beans de Spring.
* Los servicios de dominio son Java puro; solo necesitan que se inyecten sus parámetros aquí.
*/
@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
public class AppConfig {

    @Value("${app.transfer.approval-threshold}")
    private BigDecimal transferApprovalThreshold;

    @Bean
    public TransferDomainService transferDomainService() {
        return new TransferDomainService(transferApprovalThreshold);
    }

    @Bean
    public LoanDisbursementDomainService loanDisbursementDomainService() {
        return new LoanDisbursementDomainService();
    }
}
