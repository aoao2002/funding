package com.example.funding.service.Expenditure;

import com.example.funding.bean.Expenditure;
import org.springframework.stereotype.Service;

@Service
public interface ExpenditureService {
    public Expenditure getOneExpenditureAllInfo(String expenditureNumber);
}
