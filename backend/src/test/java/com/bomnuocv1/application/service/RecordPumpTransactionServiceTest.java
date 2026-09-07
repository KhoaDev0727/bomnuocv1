package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.PumpTransactionResult;
import com.bomnuocv1.application.port.in.RecordPumpTransactionCommand;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.entity.Payment;
import com.bomnuocv1.domain.entity.PumpTransaction;
import com.bomnuocv1.domain.exception.FarmerNotFoundException;
import com.bomnuocv1.domain.repository.FarmerRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecordPumpTransactionServiceTest {

    @Mock
    private PumpTransactionRepository pumpTransactionRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private FarmerRepository farmerRepository;

    @InjectMocks
    private RecordPumpTransactionService recordPumpTransactionService;

    private UUID ownerId;
    private UUID farmerId;
    private Farmer mockFarmer;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        farmerId = UUID.randomUUID();
        mockFarmer = Farmer.createNew(ownerId, "Nguyễn Văn Bảy", "0912345678", "Ruộng 1", null);
    }

    @Test
    @DisplayName("Ghi lượt bơm mới không trả trước (initialPaidAmount = 0) -> thành công và trạng thái UNPAID")
    void shouldRecordTransactionWithoutInitialPayment() {
        when(farmerRepository.findById(farmerId)).thenReturn(Optional.of(mockFarmer));
        when(pumpTransactionRepository.save(any(PumpTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecordPumpTransactionCommand command = RecordPumpTransactionCommand.builder()
                .ownerId(ownerId)
                .farmerId(farmerId)
                .transactionDate(LocalDate.now())
                .quantity(new BigDecimal("3.0"))
                .quantityUnit("công nhỏ (1.000m²)")
                .unitPrice(new BigDecimal("90000"))
                .initialPaidAmount(BigDecimal.ZERO)
                .note("Bơm vụ hè thu")
                .build();

        PumpTransactionResult result = recordPumpTransactionService.execute(command);

        assertNotNull(result);
        assertEquals(new BigDecimal("270000.0"), result.getAmountDue());
        assertEquals(BigDecimal.ZERO, result.getPaidAmount());
        assertEquals(new BigDecimal("270000.0"), result.getRemainingDebt());
        assertEquals("UNPAID", result.getPaymentStatus());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("Ghi lượt bơm mới có trả trước một phần -> tự động sinh Payment và trạng thái PARTIAL")
    void shouldRecordTransactionWithPartialPayment() {
        when(farmerRepository.findById(farmerId)).thenReturn(Optional.of(mockFarmer));
        when(pumpTransactionRepository.save(any(PumpTransaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RecordPumpTransactionCommand command = RecordPumpTransactionCommand.builder()
                .ownerId(ownerId)
                .farmerId(farmerId)
                .transactionDate(LocalDate.now())
                .quantity(new BigDecimal("2.0"))
                .quantityUnit("giờ")
                .unitPrice(new BigDecimal("100000"))
                .initialPaidAmount(new BigDecimal("80000"))
                .note("Bơm đêm")
                .build();

        PumpTransactionResult result = recordPumpTransactionService.execute(command);

        assertNotNull(result);
        assertEquals(new BigDecimal("200000.0"), result.getAmountDue());
        assertEquals(new BigDecimal("80000"), result.getPaidAmount());
        assertEquals(new BigDecimal("120000.0"), result.getRemainingDebt());
        assertEquals("PARTIAL", result.getPaymentStatus());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    @DisplayName("Ghi lượt bơm với nông dân không tồn tại -> ném FarmerNotFoundException")
    void shouldThrowExceptionWhenFarmerNotFound() {
        when(farmerRepository.findById(farmerId)).thenReturn(Optional.empty());

        RecordPumpTransactionCommand command = RecordPumpTransactionCommand.builder()
                .ownerId(ownerId)
                .farmerId(farmerId)
                .transactionDate(LocalDate.now())
                .quantity(new BigDecimal("1.0"))
                .quantityUnit("giờ")
                .unitPrice(new BigDecimal("50000"))
                .build();

        assertThrows(FarmerNotFoundException.class, () -> recordPumpTransactionService.execute(command));
    }
}
