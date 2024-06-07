package searchCredits;

import creditOffer.CreditOffer;
import command.*;

import java.util.List;

/**
 * Абстрактний клас для пошуку кредитів
 */
public abstract class SearchCredits implements ResultCommand {
    protected List<CreditOffer> creditOffers;

    public SearchCredits(List<CreditOffer> creditOffers) {
        this.creditOffers = creditOffers;
    }

    public abstract List<CreditOffer> execute();
}
