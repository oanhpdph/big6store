package com.poly.controller.admin;

import com.poly.entity.DeliveryNotes;
import com.poly.service.DeliveryNotesSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAnyAuthority('ADMIN','STAFF')")
public class DeliveryNotesController {

    @Autowired
    private DeliveryNotesSevice deliveryNotesSevice;

    @PostMapping("/bill/bill_detail/update_delivery_notes/{id}")
    public String updateDelivery(@PathVariable("id") Integer idDelivery,
                                 @RequestParam(name = "receivingAddress") String receivingAddress,
                                 @RequestParam(name = "deliveryFee") BigDecimal deliveryFee,
                                 @RequestParam(name = "note") String note) {
        DeliveryNotes deliveryNotes = new DeliveryNotes();
        deliveryNotes.setReceivingAddress(receivingAddress);
        deliveryNotes.setDeliveryFee(deliveryFee);
        deliveryNotes.setNote(note);
        DeliveryNotes deliveryNotes1 = deliveryNotesSevice.update(deliveryNotes, idDelivery);
        return "redirect:/admin/bill/bill_detail/" + deliveryNotes1.getIdBill().getId();
    }
}
