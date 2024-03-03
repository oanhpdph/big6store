package com.poly.service;

import com.poly.dto.BillProRes;
import com.poly.entity.DeliveryNotes;

public interface DeliveryNotesSevice {
    DeliveryNotes add(DeliveryNotes deliveryNotes);

    Boolean delete(Integer id);

    DeliveryNotes update(DeliveryNotes deliveryNotes, Integer id);

    DeliveryNotes getByIdBill(Integer idBill);

    DeliveryNotes save(BillProRes deliveryNotes);
}
