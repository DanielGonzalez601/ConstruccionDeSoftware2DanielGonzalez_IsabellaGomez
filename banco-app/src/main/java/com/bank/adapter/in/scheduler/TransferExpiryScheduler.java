package com.bank.adapter.in.scheduler;

import com.bank.application.port.input.TransferInputPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
* ADAPTADOR (Controlador) - TransferExpiryScheduler
*
* Comprueba periódicamente si hay transferencias pendientes de aprobación durante
* más de 60 minutos y las cancela automáticamente.
*
* Este es un adaptador controlador: activa el caso de uso como lo haría un controlador REST.
*/
@Component
public class TransferExpiryScheduler {

    private static final Logger log = LoggerFactory.getLogger(TransferExpiryScheduler.class);

    private final TransferInputPort transferInputPort;

    public TransferExpiryScheduler(TransferInputPort transferInputPort) {
        this.transferInputPort = transferInputPort;
    }

    // Se ejecuta cada 5 minutos
    @Scheduled(fixedDelay = 300_000)
    public void expireStaleTransfers() {
        int expired = transferInputPort.processExpiredTransfers();
        if (expired > 0) {
            log.info("[SCHEDULER] transferencias pendientes con vencimiento automático de más de 60 minutos: {}", expired);
        }
    }
}
