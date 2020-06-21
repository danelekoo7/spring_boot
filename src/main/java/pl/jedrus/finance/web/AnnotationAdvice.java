package pl.jedrus.finance.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pl.jedrus.finance.domain.*;
import pl.jedrus.finance.repository.LoanRepository;
import pl.jedrus.finance.service.asset.AssetService;

import java.math.BigDecimal;
import java.time.LocalDate;


@ControllerAdvice(assignableTypes = {HomeController.class, Step1Controller.class, Step2Controller.class, Step2IncomeController.class})
public class AnnotationAdvice {

    private final LoanRepository loanRepository;
    private final AssetService assetRepository;

    public AnnotationAdvice(LoanRepository loanRepository, AssetService assetRepository) {
        this.loanRepository = loanRepository;
        this.assetRepository = assetRepository;
    }


    @ModelAttribute("totalValue")
    public BigDecimal getTotalValue(@AuthenticationPrincipal UserDetails user) {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal sumAllLoans = BigDecimal.ZERO;
        if (loanRepository.sumAllLoansByUser(user.getUsername()) != null) {
            sumAllLoans = sumAllLoans.add(loanRepository.sumAllLoansByUser(user.getUsername()));
        }

        BigDecimal sumAllAssets = assetRepository.sumAllAssetByUser(user.getUsername());
        return total.add(sumAllAssets).subtract(sumAllLoans);
    }


    @ModelAttribute("loan")
    public Loan loan() {
        Loan loan = new Loan();
        loan.setValue(BigDecimal.ZERO);
        return loan;
    }

    @ModelAttribute("asset")
    public Asset asset() {
        Asset asset = new Asset();
        asset.setValue(BigDecimal.ZERO);
        return asset;
    }


    @ModelAttribute("income")
    public Income income() {
        Income income = new Income();
        income.setValue(BigDecimal.ZERO);
        return income;
    }

    @ModelAttribute("expense")
    public Expense expense() {
        Expense expense = new Expense();
        expense.setRealValue(BigDecimal.ZERO);
        expense.setPlannedValue(BigDecimal.ZERO);
        return expense;
    }

    @ModelAttribute("expenseRegister")
    public ExpenseRegister expenseRegister() {
        ExpenseRegister expenseRegister = new ExpenseRegister();
        expenseRegister.setCreated(LocalDate.now());
        expenseRegister.setValue(BigDecimal.ZERO);
        return expenseRegister;
    }


    @ModelAttribute("user")
    public String user(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername();
    }
}