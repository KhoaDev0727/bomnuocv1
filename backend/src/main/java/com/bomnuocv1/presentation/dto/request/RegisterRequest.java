package com.bomnuocv1.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class RegisterRequest {

    @NotBlank(message = "Số điện thoại không được để trống.")
    @Pattern(regexp = "^(0|\\+84)[0-9]{9,10}$", message = "Số điện thoại không đúng định dạng (VD: 0912345678).")
    private String phoneNumber;

    @NotBlank(message = "Mã PIN không được để trống.")
    @Size(min = 4, max = 4, message = "Mã PIN phải gồm đúng 4 chữ số.")
    @Pattern(regexp = "^[0-9]{4}$", message = "Mã PIN chỉ bao gồm các chữ số.")
    private String pinCode;

    @NotBlank(message = "Họ và tên không được để trống.")
    @Size(max = 100, message = "Họ và tên tối đa 100 ký tự.")
    private String fullName;

    private String roleCode;

    private String otpCode;

    private String firebaseIdToken;
}
