package com.poly.controller.admin;

import com.poly.dto.Attribute;
import com.poly.entity.Field;
import com.poly.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/field")
public class FieldController {
    @Autowired
    private FieldService field;

    @GetMapping("/all-active")
    public ResponseEntity<?> getDataFieldActive() {
        List<Field> list = field.findAll(new Attribute()).getContent();

        return ResponseEntity.ok(list.stream().filter(field1 -> field1.isActive() == true).toList());
    }
    @GetMapping("/all")
    public ResponseEntity<?> getDataField() {
        List<Field> list = field.findAll(new Attribute()).getContent();

        return ResponseEntity.ok(list);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAttributes(@RequestBody Field f) {
        return ResponseEntity.ok(field.save(f));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getField(@RequestParam(required = false) Attribute attribute) {
        List<Field> list = field.findAll(new Attribute()).getContent();

        return ResponseEntity.ok(list);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateAttributes(@RequestBody Attribute attribute) {
        Field field1 = field.findById(attribute.getId());
        if (field1 != null) {
            field1.setActive(attribute.isActive());
            if (attribute.getName() != null) {
                field1.setName(attribute.getName());
            }
        }
        return ResponseEntity.ok(field.update(field1));
    }

    @GetMapping("/get-one")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        return ResponseEntity.ok(field.findById(id));
    }

}
