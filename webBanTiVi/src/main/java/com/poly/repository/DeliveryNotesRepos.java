package com.poly.repository;


import com.poly.entity.Bill;
import com.poly.entity.DeliveryNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeliveryNotesRepos extends JpaRepository<DeliveryNotes,Integer> {
    @Query(value = "select dn from DeliveryNotes dn where dn.idBill.id=?1")
    Optional<DeliveryNotes> getDeliveryNotesByIdBill(Integer idBill);
}
