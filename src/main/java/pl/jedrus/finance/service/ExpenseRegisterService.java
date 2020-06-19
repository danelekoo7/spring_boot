package pl.jedrus.finance.service;

import org.springframework.stereotype.Repository;
import pl.jedrus.finance.domain.ExpenseRegister;

import java.util.List;

@Repository
public interface ExpenseRegisterService {
    List<ExpenseRegister> findAllByUser_Username(String username);

    ExpenseRegister findAllById(Long id);

    void saveExpenseRegister(ExpenseRegister expenseRegister);

    void updateExpenseRegister(ExpenseRegister expenseRegister);

    void deleteExpenseRegisterById(Long id);

}
