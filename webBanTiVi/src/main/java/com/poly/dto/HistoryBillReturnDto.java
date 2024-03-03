package com.poly.dto;

import com.poly.entity.Bill;
import com.poly.entity.HistoryBillProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryBillReturnDto {
    private Bill bill;
    private Integer returnTimes;
    private List<HistoryBillProduct> historyBillProductList;
    private BigDecimal returnMoney;
    private Integer quantityAccept;
}
