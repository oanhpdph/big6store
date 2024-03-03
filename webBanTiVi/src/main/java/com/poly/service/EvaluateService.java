package com.poly.service;

import com.poly.entity.Evaluate;
import com.poly.dto.EvaluateRes;
import org.springframework.data.domain.Page;

public interface EvaluateService {

    public Page<Evaluate> getAll(EvaluateRes evaluateRes);

    public Evaluate add(EvaluateRes evaluate);

    Evaluate findById(Integer id);

    Evaluate update(EvaluateRes evaluateRes);

}
