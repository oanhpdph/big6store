package com.poly.service.Impl;

import com.poly.dto.CountBillProductReturnDto;
import com.poly.dto.HistoryBillReturnDto;
import com.poly.entity.Bill;
import com.poly.entity.BillProduct;
import com.poly.entity.HistoryBillProduct;
import com.poly.repository.BillRepos;
import com.poly.repository.HistoryBillProductRepository;
import com.poly.service.HistoryBillProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryBillProductImpl implements HistoryBillProductService {


    @Autowired
    HistoryBillProductRepository historyBillProductRepository;

    @Autowired
    BillRepos billRepos;


    @Override
    public HistoryBillProduct save(HistoryBillProduct historyBillProduct) {
        return this.historyBillProductRepository.save(historyBillProduct);
    }

    @Override
    public HistoryBillProduct findByBillProductAndReturnTimes(Integer idBillProduct, Integer idBill) {
        HistoryBillProduct hBillProduct = new HistoryBillProduct();
        Optional<Integer> returnCount = this.historyBillProductRepository.findReturnCountBillById(idBill);
        List<HistoryBillProduct> historyBillProducts = this.historyBillProductRepository.findAll();
        if(returnCount.isPresent()) {
            for (HistoryBillProduct historyBillProduct : historyBillProducts) {
                if (historyBillProduct.getBillProduct().getId().equals(idBillProduct) &&
                        historyBillProduct.getReturnTimes().equals(returnCount.get())) {
                    return historyBillProduct;
                }
            }
        }
        return hBillProduct;
    }

    @Override
    public List<HistoryBillProduct> findAll() {
        return this.historyBillProductRepository.findAll();
    }

    @Override
    public List<HistoryBillProduct> findHistoryBillProductReturn(Integer status, Integer id) {
        return this.historyBillProductRepository.findHistoryBillProductReturn(status, id);
    }

    @Override
    public List<CountBillProductReturnDto> findCountBillProductReturnDtos(String id) {
        return null;
    }

    @Override
    public Bill listBillWhenReturned(String id) {
        List<CountBillProductReturnDto> billProductDTOList = new ArrayList<>();
        List<Object[]> resultList = this.historyBillProductRepository.getReturnedDataForBill(Long.parseLong(id));
        for (Object[] result : resultList) {
            CountBillProductReturnDto count = new CountBillProductReturnDto();
            count.setIdBillProduct((Integer) result[0]);
            count.setTotalAcceptReturn(Integer.parseInt(String.valueOf((long) result[1])));
            billProductDTOList.add(count);
        }
        Bill bill = this.billRepos.findById(Integer.parseInt(id)).get();
        for (CountBillProductReturnDto countBilProductReturnDto : billProductDTOList) {
            for (BillProduct billProduct : bill.getBillProducts()) {
                if (countBilProductReturnDto.getIdBillProduct() == billProduct.getId()) {
                    billProduct.setQuantity(billProduct.getQuantity() - countBilProductReturnDto.getTotalAcceptReturn());
                }

            }
        }
        return bill;
    }

    @Override
    public List<HistoryBillProduct> findAllByStatus(Integer status) {
        return this.historyBillProductRepository.findHistoryBillProductByStatus(status);
    }

    @Override
    public List<Integer> findIbBillByIdUser(Integer id) {
        return this.historyBillProductRepository.findListIdBillByIdUser(id);
    }

    @Override
    public List<Integer> findIbBillByStatusReturn(Integer id) {
        return this.historyBillProductRepository.findListIdBillByStatusReturn(id);
    }

    @Override
    public List<Integer> findIdBillByStatusAndUserReturn(Integer status, Integer idUser) {
        return this.historyBillProductRepository.findListIdBillByStatusAndUserReturn(status,idUser);
    }

    @Override
    public List<Integer> findIdBillByStatusReturn(Integer status) {
        return this.historyBillProductRepository.findListIdBillByStatus(status);
    }

    @Override
    public List<Integer> findAllIdBillReturn() {
        return this.historyBillProductRepository.findAllListIdBillByStatus();
    }

    @Override
    public HistoryBillReturnDto listHistoryBillAndReturnTimes(Integer id, Integer returnTimes) {
        List<HistoryBillProduct> historyBillProducts = this.historyBillProductRepository.findAllHistoryBillReturnByIdBill(id, returnTimes);
        Bill bill = this.billRepos.findById(id).get();
        HistoryBillReturnDto historyBillReturnDto = new HistoryBillReturnDto();
        List<HistoryBillReturnDto> listHistoryDto = this.findAllHistoryBillReturnByIdBill(id);
        BigDecimal totalReturnBill = BigDecimal.ZERO;
        for(HistoryBillReturnDto hBillReturnDto : listHistoryDto){
            if(hBillReturnDto.getReturnTimes().equals(returnTimes)){
                historyBillReturnDto = hBillReturnDto;
            }
        }
        return historyBillReturnDto;
    }

    @Override
    public List<HistoryBillReturnDto> findAllHistoryBillReturnByIdBill(Integer id) {
        List<HistoryBillReturnDto> historyBillReturnDtos = new ArrayList<>();
        Optional<Integer> returnMax = this.historyBillProductRepository.findReturnCountBillById(id);
        if( returnMax.isPresent() && returnMax.get() != 0 ) {
            for (int i = 0; i < returnMax.get(); i++) {
                List<HistoryBillProduct> historyBillProducts = this.historyBillProductRepository.findAllHistoryBillReturnByIdBill(id, i + 1);
                HistoryBillReturnDto historyBillReturnDto = new HistoryBillReturnDto();
                historyBillReturnDto.setReturnTimes(i + 1);
                historyBillReturnDto.setHistoryBillProductList(historyBillProducts);
                BigDecimal total = BigDecimal.ZERO;
                Integer quantityAccept= 0;
                for(HistoryBillProduct historyBillProduct : historyBillProducts){
                    quantityAccept = quantityAccept+ historyBillProduct.getQuantityAcceptReturn();
                    BigDecimal price = historyBillProduct.getBillProduct().getPrice();
                    BigDecimal reducedMoney = historyBillProduct.getBillProduct().getReducedMoney();
                    int quantity = historyBillProduct.getQuantityAcceptReturn();
                   if(price != null && quantity >= 0){
                       BigDecimal productTotal = (price.subtract(reducedMoney)).multiply(BigDecimal.valueOf(quantity));
                       total = total.add(productTotal);
                   }else{
                       System.out.println("Giá trị null hoặc không hợp lệ được đọc từ historyBillProduct");
                        }
                   }
                historyBillReturnDto.setQuantityAccept(quantityAccept);
                historyBillReturnDto.setBill(this.billRepos.findById(id).get());
                historyBillReturnDto.setReturnMoney(total);
                historyBillReturnDtos.add(historyBillReturnDto);
            }
        }
        Bill bill = this.billRepos.findById(id).get();
        BigDecimal totalReturnBill = BigDecimal.ZERO;
        int check = 0;
        if(bill.getVoucher() != null) {
            for (HistoryBillReturnDto hBillReturnDto : historyBillReturnDtos) {
                totalReturnBill = totalReturnBill.add(hBillReturnDto.getReturnMoney());
                if (bill.getTotalPrice().subtract(totalReturnBill).compareTo(bill.getVoucher().getMinimumValue()) < 0 && check == 0) {
                    hBillReturnDto.setReturnMoney(hBillReturnDto.getReturnMoney().subtract(bill.getVoucherValue()));
                    check = 1;
                }
            }
        }
        return historyBillReturnDtos;
    }

    public Boolean compareQuantityBillAndHistoryPoduct(Integer id){
        Boolean check = false;
        Bill bill = this.billRepos.findById(id).get();
        int totalBill = 0;
        for(BillProduct billProduct : bill.getBillProducts()){
           totalBill += billProduct.getQuantity();
        }
        Integer totalQuantityReturn = this.historyBillProductRepository.totalQuantityAcceptReturn(id);
        if(totalQuantityReturn == null) {
            return check;
        }
        if( totalQuantityReturn.equals(totalBill)){
            check=true;
        }
        return check;
    }

}

