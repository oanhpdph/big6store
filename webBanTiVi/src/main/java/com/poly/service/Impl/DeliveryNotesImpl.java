package com.poly.service.Impl;

import com.poly.dto.BillProRes;
import com.poly.entity.DeliveryNotes;
import com.poly.repository.DeliveryNotesRepos;
import com.poly.service.BillService;
import com.poly.service.DeliveryNotesSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeliveryNotesImpl implements DeliveryNotesSevice {
    @Autowired
    private DeliveryNotesRepos deliveryNotesRepos;

    @Autowired
    private BillService billService;

    @Override
    public DeliveryNotes add(DeliveryNotes deliveryNotes) {
        return deliveryNotesRepos.save(deliveryNotes);
    }

    @Override
    public Boolean delete(Integer id) {
        Optional<DeliveryNotes> optional = deliveryNotesRepos.findById(id);
        if (optional.isPresent()) {
            deliveryNotesRepos.delete(optional.get());
            return true;
        }
        return false;
    }

    @Override
    public DeliveryNotes update(DeliveryNotes deliveryNotes, Integer id) {
        Optional<DeliveryNotes> optional = deliveryNotesRepos.findById(id);
        if (optional.isPresent()) {
            DeliveryNotes deliveryNotesUpdate = optional.get();
            deliveryNotesUpdate.setNote(deliveryNotes.getNote());
            deliveryNotesUpdate.setStatus(deliveryNotesUpdate.getStatus());
            deliveryNotesUpdate.setDeliveryFee(deliveryNotes.getDeliveryFee());
            deliveryNotesUpdate.setReceivingAddress(deliveryNotes.getReceivingAddress());
            return deliveryNotesRepos.save(deliveryNotesUpdate);
        }
        return null;
    }

    @Override
    public DeliveryNotes getByIdBill(Integer idBill) {
        Optional<DeliveryNotes> optional = deliveryNotesRepos.getDeliveryNotesByIdBill(idBill);
        System.out.println(optional);
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    @Override
    public DeliveryNotes save(BillProRes deliveryNotes) {
        DeliveryNotes notes = new DeliveryNotes();
        notes.setIdBill(deliveryNotes.getBill());
        notes.setReceived(deliveryNotes.getName());
        notes.setReceivedEmail(deliveryNotes.getEmail());
        notes.setReceiverPhone(deliveryNotes.getPhoneNumber());
        notes.setReceivingAddress(deliveryNotes.getAddress());
        notes.setDeliver("0");
        notes.setDeliveryPhone(deliveryNotes.getPhoneNumber());
        notes.setDeliveryDate(new java.util.Date());
        notes.setDeliveryFee(deliveryNotes.getDeliveryFee());
        notes.setReceivedDate(deliveryNotes.getReceivedDate());
        notes.setNote("");
        notes.setStatus(0);
        return this.deliveryNotesRepos.save(notes);
    }


}
