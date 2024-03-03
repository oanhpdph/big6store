package com.poly.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class formReturnDto {
    @NotBlank(message = "(*)Số lượng không được để trống!")
    private String quantity;
    @NotBlank(message="(*)Ghi chú không được bỏ trống!")
    private String note;


}
