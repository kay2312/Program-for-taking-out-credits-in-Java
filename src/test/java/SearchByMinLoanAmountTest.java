import creditOffer.CreditOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import searchCredits.SearchByMinLoanAmount;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchByMinLoanAmountTest {
    private List<CreditOffer> creditOffers;
    protected SearchByMinLoanAmount searchByMinLoanAmount;

    @BeforeEach
    public void setUp() {
        creditOffers = new ArrayList<>();
        searchByMinLoanAmount = new SearchByMinLoanAmount(creditOffers, 1000000);
    }

    @Test
    public void testExecute_ReturnsFilteredOffers() {
        List<CreditOffer> creditOfferList = new ArrayList<>();
        creditOfferList.add(new CreditOffer(1, "Універсал Банк", "Автокредит", 11, 36, true, true, 400000));
        creditOfferList.add(new CreditOffer(2, "Фінансова Група", "Житловий кредит", 13, 48, false, true, 3000000));
        creditOfferList.add(new CreditOffer(3,"Економія Банк", "Освітній кредит", 7, 60, true, false, 100000));
        creditOfferList.add(new CreditOffer(4,"Фінанс Брокер", "Бізнес-кредит", 8, 24, true, false, 2000000));

        creditOffers.addAll(creditOfferList);

        List<CreditOffer> expectedFilteredOffers = new ArrayList<>();
        expectedFilteredOffers.add(creditOfferList.get(1));
        expectedFilteredOffers.add(creditOfferList.get(3));

        List<CreditOffer> filteredOffers = searchByMinLoanAmount.execute();

        assertEquals(expectedFilteredOffers.size(), filteredOffers.size());
        assertEquals(expectedFilteredOffers, filteredOffers);
    }
}
