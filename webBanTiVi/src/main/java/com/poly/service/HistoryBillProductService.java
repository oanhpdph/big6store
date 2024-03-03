package com.poly.service;

import com.poly.dto.CountBillProductReturnDto;
import com.poly.dto.HistoryBillReturnDto;
import com.poly.entity.Bill;
import com.poly.entity.HistoryBillProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HistoryBillProductService {

    HistoryBillProduct save(HistoryBillProduct historyBillProduct);

    HistoryBillProduct findByBillProductAndReturnTimes(Integer idBillProduct,Integer idBill);

    List<HistoryBillProduct> findAll();

   List<HistoryBillProduct> findHistoryBillProductReturn(Integer status, Integer id);

   List<CountBillProductReturnDto> findCountBillProductReturnDtos(String id);

   List<HistoryBillReturnDto> findAllHistoryBillReturnByIdBill(Integer id);

   Bill  listBillWhenReturned(String id);

   List<HistoryBillProduct> findAllByStatus(Integer status);

   List<Integer> findIbBillByIdUser(Integer id);

    List<Integer> findIbBillByStatusReturn(Integer id);

    List<Integer> findIdBillByStatusAndUserReturn(Integer status, Integer idUser);

    List<Integer> findIdBillByStatusReturn(Integer status);

    List<Integer> findAllIdBillReturn();

    HistoryBillReturnDto listHistoryBillAndReturnTimes(Integer id, Integer status);

    Boolean compareQuantityBillAndHistoryPoduct(Integer id);
}
