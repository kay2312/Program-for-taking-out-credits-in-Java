package searchCredits;

import creditOffer.CreditOffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас для пошуку кредитів за максимальним відсотковою ставкою
 */
public class SearchByMaxInterestRate extends SearchCredits {
    private int maxInterestRate;

    /**
     * Конструктор
     * @param creditOffers - список кредитних пропозицій
     * @param maxInterestRate - максимальна відсоткова ставка
     */
    public SearchByMaxInterestRate(List<CreditOffer> creditOffers, int maxInterestRate) {
        super(creditOffers);
        this.maxInterestRate = maxInterestRate;
    }


    public List<CreditOffer> execute() {
        List<CreditOffer> filteredOffers = new ArrayList<>();
        for (CreditOffer offer : creditOffers) {
            if (offer.getInterestRate() <= maxInterestRate) {
                filteredOffers.add(offer);
            }
        }
        return filteredOffers;
    }
}
