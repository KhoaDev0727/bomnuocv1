package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.FarmerResult;
import com.bomnuocv1.application.usecase.GetFarmersUseCase;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.repository.FarmerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetFarmersService implements GetFarmersUseCase {

    private final FarmerRepository farmerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<FarmerResult> execute(UUID ownerId, String keyword) {
        List<Farmer> farmers = (keyword != null && !keyword.trim().isEmpty())
                ? farmerRepository.searchByOwnerId(ownerId, keyword.trim())
                : farmerRepository.findByOwnerId(ownerId);

        return farmers.stream()
                .map(FarmerResult::fromDomain)
                .collect(Collectors.toList());
    }
}
