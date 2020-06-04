package pl.jedrus.finance.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ModelAttribute;
import pl.jedrus.finance.domain.Asset;
import pl.jedrus.finance.domain.Loan;

import pl.jedrus.finance.repository.AssetRepository;
import pl.jedrus.finance.repository.LoanRepository;

import java.math.BigDecimal;


@ControllerAdvice()
public class AnnotationAdvice {

    private final LoanRepository loanRepository;
    private final AssetRepository assetRepository;

    public AnnotationAdvice(LoanRepository loanRepository, AssetRepository assetRepository) {
        this.loanRepository = loanRepository;
        this.assetRepository = assetRepository;
    }

    @ModelAttribute("totalValue")
    public BigDecimal getTotalValue() {
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal sumAllLoans = BigDecimal.ZERO;
        if (loanRepository.sumAllLoans() != null) {
            sumAllLoans = sumAllLoans.add(loanRepository.sumAllLoans());
        }

        BigDecimal sumAllAssets = BigDecimal.ZERO;

        if (assetRepository.sumAllAssets() != null) {
            sumAllAssets = sumAllAssets.add(assetRepository.sumAllAssets());
        }


        return total.add(sumAllAssets).subtract(sumAllLoans);
    }


    @ModelAttribute("loan")
    public Loan loan() {
        return new Loan();
    }

    @ModelAttribute("asset")
    public Asset asset() {
        return new Asset();
    }

    @ModelAttribute("user")
    public String user(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername();
    }
}