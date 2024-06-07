package searchCredits;

import creditOffer.CreditOffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас для пошуку кредитів, які мають можливість збільшення кредитної лінії
 */
public class SearchByIsCreditLineIncreaseEnabled extends SearchCredits{

    /**
     * Конструктор
     * @param creditOffers - список кредитних пропозицій
     */
    public SearchByIsCreditLineIncreaseEnabled(List<CreditOffer> creditOffers) {
        super(creditOffers);
    }

    /**
     * Метод для пошуку кредитів, які мають можливість збільшення кредитної лінії
     * @return фідвільтрований список
     */
    public List<CreditOffer> execute() {
        List<CreditOffer> filteredOffers = new ArrayList<>();
        for (CreditOffer offer : creditOffers) {
            if (offer.getCreditLineIncreaseEnabled()) {
                filteredOffers.add(offer);
            }
        }
        return filteredOffers;
    }
}
