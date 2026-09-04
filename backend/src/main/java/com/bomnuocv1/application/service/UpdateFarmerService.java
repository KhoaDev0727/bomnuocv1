package com.bomnuocv1.application.service;

import com.bomnuocv1.application.dto.FarmerResult;
import com.bomnuocv1.application.port.in.UpdateFarmerCommand;
import com.bomnuocv1.application.usecase.UpdateFarmerUseCase;
import com.bomnuocv1.domain.entity.Farmer;
import com.bomnuocv1.domain.exception.FarmerNotFoundException;
import com.bomnuocv1.domain.exception.InvalidFarmerDataException;
import com.bomnuocv1.domain.exception.PhoneAlreadyExistsException;
import com.bomnuocv1.domain.repository.FarmerRepository;
import com.bomnuocv1.domain.repository.UserRepository;
import com.bomnuocv1.domain.valueobject.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdateFarmerService implements UpdateFarmerUseCase {

    private final FarmerRepository farmerRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public FarmerResult execute(UpdateFarmerCommand command) {
        if (command.getFullName() == null || command.getFullName().trim().isEmpty()) {
            throw new InvalidFarmerDataException("Tên nông dân không được để trống.");
        }

        Farmer farmer = farmerRepository.findById(command.getId())
                .orElseThrow(() -> new FarmerNotFoundException("Không tìm thấy hồ sơ nông dân."));

        if (!farmer.getOwnerId().equals(command.getOwnerId()) || farmer.isDeleted()) {
            throw new FarmerNotFoundException("Không tìm thấy hồ sơ nông dân hoặc bạn không có quyền chỉnh sửa.");
        }

        String rawPhone = command.getPhoneNumber();
        String normalizedPhone = null;

        if (rawPhone != null && !rawPhone.trim().isEmpty()) {
            String cleaned = rawPhone.replaceAll("[\\s\\-\\(\\)]", "");
            if (!cleaned.matches("^(0|\\+84)[0-9]{9,10}$")) {
                throw new InvalidFarmerDataException("Số điện thoại không đúng định dạng (phải gồm 10 chữ số).");
            }
            if (cleaned.startsWith("+84")) {
                cleaned = "0" + cleaned.substring(3);
            }
            normalizedPhone = cleaned;

            // 1. Check if phone number is registered as an app user (owner / staff account)
            PhoneNumber phoneVo = PhoneNumber.of(normalizedPhone);
            if (userRepository.existsByPhoneNumber(phoneVo)) {
                throw new PhoneAlreadyExistsException("Số điện thoại này đã được đăng ký tài khoản trong hệ thống.");
            }

            // 2. Check if phone number already exists among other active farmers for this owner
            if (farmerRepository.existsByOwnerIdAndPhoneNumberAndIdNot(command.getOwnerId(), normalizedPhone, farmer.getId())) {
                throw new PhoneAlreadyExistsException("Số điện thoại này đã tồn tại trong danh mục nông dân của bạn.");
            }
        }

        farmer.updateInfo(command.getFullName(), normalizedPhone, command.getAreaNote());
        Farmer saved = farmerRepository.save(farmer);
        log.info("Updated farmer: {} for owner: {}", saved.getId(), saved.getOwnerId());
        return FarmerResult.fromDomain(saved);
    }
}
