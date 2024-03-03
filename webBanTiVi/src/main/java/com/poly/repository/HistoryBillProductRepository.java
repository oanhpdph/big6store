package com.poly.repository;

import com.poly.entity.HistoryBillProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HistoryBillProductRepository extends JpaRepository<HistoryBillProduct, Integer> {

    @Query(value="Select Max(b.returnTimes) from HistoryBillProduct b where b.bill.id=?1")
    Optional<Integer> findReturnCountBillById(Integer id);

    @Query(value = "select b from HistoryBillProduct b inner join Bill  a on b.bill.id=a.id where  b.statusBillProduct=?1 and a.id=?2")
    List<HistoryBillProduct> findHistoryBillProductReturn(Integer status, Integer id);

    @Query(value="select b.billProduct.id as idBillProduct, sum(b.quantityAcceptReturn) as totalReturned  from HistoryBillProduct b where b.bill.id=?1 group by b.billProduct.id")
    List<Object[]> getReturnedDataForBill(Long idBill);

    @Query(value="Select b from HistoryBillProduct b where b.bill.id=?1 and b.returnTimes=?2")
    List<HistoryBillProduct> findAllHistoryBillReturnByIdBill(Integer id, Integer returnTimes);

    @Query(value="select b from HistoryBillProduct b where b.statusBillProduct=?1")
    List<HistoryBillProduct> findHistoryBillProductByStatus(Integer status);

    @Query(value="select b from HistoryBillProduct  b where b.bill.id=?1")
    List<HistoryBillProduct> findHistoryBillProductByIdBill(Integer id);

    @Query(value="select distinct b.bill.id from HistoryBillProduct  b where b.bill.customer.id=?1")
    List<Integer> findListIdBillByIdUser(Integer id);

    @Query(value="select distinct b.bill.id from HistoryBillProduct  b where b.status=?1")
    List<Integer> findListIdBillByStatusReturn(Integer status);

    @Query(value="select distinct b.bill.id from HistoryBillProduct  b where b.status=?1 and b.bill.customer.id=?2")
    List<Integer> findListIdBillByStatusAndUserReturn(Integer status,Integer idUser);

    @Query(value="select distinct b.bill.id from HistoryBillProduct  b where b.status=?1 ")
    List<Integer> findListIdBillByStatus(Integer status);

    @Query(value="select distinct b.bill.id from HistoryBillProduct b ")
    List<Integer> findAllListIdBillByStatus();

    @Query(value="select sum(b.quantityAcceptReturn) from HistoryBillProduct b where b.bill.id=?1")
    Integer totalQuantityAcceptReturn(Integer idBill);

}
