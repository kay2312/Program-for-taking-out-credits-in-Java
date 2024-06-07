package searchCredits;

import creditOffer.CreditOffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас для пошуку кредитних пропозицій за мінімальною сумою
 */
public class SearchByMinLoanAmount extends SearchCredits {
    private int minLoanAmount;

    /**
     * Конструктор
     * @param creditOffers - список кредитних пропозицій
     * @param minLoanAmount - мінімальна сума
     */
    public SearchByMinLoanAmount(List<CreditOffer> creditOffers, int minLoanAmount) {
        super(creditOffers);
        this.minLoanAmount = minLoanAmount;
    }


    public List<CreditOffer> execute() {
        List<CreditOffer> filteredOffers = new ArrayList<>();
        for (CreditOffer offer : creditOffers) {
            if (offer.getSumCredit() >= minLoanAmount) {
                filteredOffers.add(offer);
            }
        }
        return filteredOffers;
    }
}
