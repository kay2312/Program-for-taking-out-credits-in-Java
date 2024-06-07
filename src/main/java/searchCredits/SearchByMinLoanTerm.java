package searchCredits;

import creditOffer.CreditOffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас для пошуку кредитів за мінімальним терміном кредиту
 */
public class SearchByMinLoanTerm extends SearchCredits {
    private int minLoanTerm;

    /**
     * Конструктор
     * @param creditOffers - список кредитних пропозицій
     * @param minLoanTerm - мінімальний термін для кредиту
     */
    public SearchByMinLoanTerm(List<CreditOffer> creditOffers, int minLoanTerm) {
        super(creditOffers);
        this.minLoanTerm = minLoanTerm;
    }


    public List<CreditOffer> execute() {
        List<CreditOffer> filteredOffers = new ArrayList<>();
        for (CreditOffer offer : creditOffers) {
            if (offer.getLoanTerm() >= minLoanTerm) {
                filteredOffers.add(offer);
            }
        }
        return filteredOffers;
    }
}