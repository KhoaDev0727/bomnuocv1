package com.bomnuocv1.application.service;

import com.bomnuocv1.domain.entity.Payment;
import com.bomnuocv1.domain.entity.PumpTransaction;
import com.bomnuocv1.domain.repository.PaymentRepository;
import com.bomnuocv1.domain.repository.PumpTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletePumpTransactionServiceTest {

    @Mock
    private PumpTransactionRepository pumpTransactionRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private DeletePumpTransactionService deletePumpTransactionService;

    private UUID ownerId;
    private UUID transactionId;
    private PumpTransaction mockTransaction;
    private Payment mockPayment;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        transactionId = UUID.randomUUID();
        UUID farmerId = UUID.randomUUID();

        mockTransaction = PumpTransaction.createNew(
                ownerId, farmerId, null, LocalDate.now(),
                new BigDecimal("2.5"), "công lớn", new BigDecimal("120000"), "Ghi chú", null
        );

        mockPayment = Payment.createNew(
                ownerId, farmerId, transactionId, new BigDecimal("100000"),
                LocalDate.now(), "Thanh toán lúc bơm", null
        );
    }

    @Test
    @DisplayName("Xóa mềm giao dịch -> đánh dấu deleted = true cho cả transaction và payment liên kết")
    void shouldSoftDeleteTransactionAndLinkedPayments() {
        when(pumpTransactionRepository.findById(transactionId)).thenReturn(Optional.of(mockTransaction));
        when(paymentRepository.findByTransactionId(transactionId)).thenReturn(List.of(mockPayment));

        deletePumpTransactionService.execute(transactionId, ownerId);

        assertTrue(mockTransaction.isDeleted());
        assertTrue(mockPayment.isDeleted());
        verify(pumpTransactionRepository).save(mockTransaction);
        verify(paymentRepository).save(mockPayment);
    }
}
