package com.bomnuocv1.application.service;

import com.bomnuocv1.application.usecase.DeleteFarmerUseCase;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.exception.FarmerNotFoundException;
import com.bomnuocv1.domain.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeleteFarmerService implements DeleteFarmerUseCase {

    private final FarmerRepository farmerRepository;

    @Override
    @Transactional
    public void execute(UUID farmerId, UUID ownerId) {
        Farmer farmer = farmerRepository.findById(farmerId)
                .orElseThrow(() -> new FarmerNotFoundException("Không tìm thấy hồ sơ nông dân cần xóa."));

        if (!farmer.getOwnerId().equals(ownerId) || farmer.isDeleted()) {
            throw new FarmerNotFoundException("Không tìm thấy hồ sơ nông dân hoặc bạn không có quyền xóa.");
        }

        farmer.markDeleted();
        farmerRepository.save(farmer);
        log.info("Soft-deleted farmer: {} for owner: {}", farmerId, ownerId);
    }
}
