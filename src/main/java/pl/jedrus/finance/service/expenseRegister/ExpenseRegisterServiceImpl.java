package pl.jedrus.finance.service.expenseRegister;

import org.springframework.stereotype.Service;
import pl.jedrus.finance.domain.DateIndicator;
import pl.jedrus.finance.domain.Expense;
import pl.jedrus.finance.domain.ExpenseRegister;
import pl.jedrus.finance.repository.ExpenseRegisterRepository;
import pl.jedrus.finance.service.dateIndicator.DateIndicatorService;
import pl.jedrus.finance.service.expense.ExpenseService;
import pl.jedrus.finance.service.user.UserService;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExpenseRegisterServiceImpl implements ExpenseRegisterService {

    private final ExpenseRegisterRepository expenseRegisterRepository;
    private final UserService userService;
    private final ExpenseService expenseService;
    private final DateIndicatorService dateIndicatorService;

    public ExpenseRegisterServiceImpl(ExpenseRegisterRepository expenseRegisterRepository, UserService userService, ExpenseService expenseService, DateIndicatorService dateIndicatorService) {
        this.expenseRegisterRepository = expenseRegisterRepository;
        this.userService = userService;
        this.expenseService = expenseService;
        this.dateIndicatorService = dateIndicatorService;
    }


    @Override
    public List<ExpenseRegister> findAllByUser_Username(String username) {
        DateIndicator dateIndicator = dateIndicatorService.findByUser_Username(username);

        int monthId = dateIndicator.getCurrentDateIndicator().getMonthValue();
        int yearId = dateIndicator.getCurrentDateIndicator().getYear();

        List<ExpenseRegister> allExpenses = expenseRegisterRepository.findAllByUser_Username(username, monthId, yearId);

        allExpenses.sort((o1, o2) -> o2.getCreated().compareTo(o1.getCreated()));

        return allExpenses;
    }

    @Override
    public ExpenseRegister findById(Long id) {
        return expenseRegisterRepository.findAllById(id).orElseThrow();
    }

    @Override
    public BigDecimal sumAllExpensesInRegister(String username) {
        List<ExpenseRegister> expenseRegisterList = findAllByUser_Username(username);
        BigDecimal expenseRegisterSum = BigDecimal.ZERO;
        for (ExpenseRegister expenseRegister : expenseRegisterList) {
            expenseRegisterSum = expenseRegisterSum.add(expenseRegister.getValue());
        }
        return expenseRegisterSum;
    }

    @Override
    public void saveExpenseRegister(ExpenseRegister expenseRegister, String username) {
        expenseRegister.setUser(userService.findByUserName(username));
        expenseRegister.setCurrentDateIndicator(dateIndicatorService.findByUser_Username(username).getCurrentDateIndicator());
        Expense expense = expenseService.findById(expenseRegister.getExpense().getId());
        expense.setRealValue(expense.getRealValue().add(expenseRegister.getValue()));
        expenseService.updateExpense(expense);
        expenseRegisterRepository.save(expenseRegister);
    }

    @Override
    public void updateExpenseRegister(ExpenseRegister expenseRegister) {
        ExpenseRegister expenseRegisterInDB = findById(expenseRegister.getId());
        expenseRegisterInDB.setDescription(expenseRegister.getDescription());
        expenseRegisterInDB.setComment(expenseRegister.getComment());
        expenseRegisterInDB.setCreated(expenseRegister.getCreated());
        expenseRegisterInDB.setValue(expenseRegister.getValue());
        expenseRegisterInDB.setExpense(expenseRegister.getExpense());
        expenseRegisterRepository.save(expenseRegisterInDB);
    }


    @Override
    public void deleteExpenseRegisterById(Long id) {
        expenseRegisterRepository.deleteById(id);
    }
}
