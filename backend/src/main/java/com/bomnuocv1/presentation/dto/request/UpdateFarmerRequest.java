package com.bomnuocv1.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFarmerRequest {

    @NotBlank(message = "Tên nông dân không được để trống.")
    @Size(max = 100, message = "Tên không được vượt quá 100 ký tự.")
    private String fullName;

    @Size(max = 15, message = "Số điện thoại không được vượt quá 15 ký tự.")
    private String phoneNumber;

    @Size(max = 255, message = "Ghi chú khu vực/thửa ruộng không được vượt quá 255 ký tự.")
    private String areaNote;
}
