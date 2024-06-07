package searchCredits;

import creditOffer.CreditOffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас для пошуку кредитів, які мають можливість дострокового закриття кредиту
 */
public class SearchByIsEarlyRepaymentEnabled extends SearchCredits{
    public SearchByIsEarlyRepaymentEnabled(List<CreditOffer> creditOffers) {
        super(creditOffers);
    }

    public List<CreditOffer> execute() {
        List<CreditOffer> filteredOffers = new ArrayList<>();
        for (CreditOffer offer : creditOffers) {
            if (offer.getEarlyRepaymentEnabled()) {
                filteredOffers.add(offer);
            }
        }
        return filteredOffers;
    }
}
