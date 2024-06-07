import creditOffer.CreditOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import searchCredits.SearchByMaxInterestRate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchByMaxInterestRateTest {

    private List<CreditOffer> creditOffers;
    private SearchByMaxInterestRate searchByMaxInterestRate;

    @BeforeEach
    public void setUp() {
        creditOffers = new ArrayList<>();
        searchByMaxInterestRate = new SearchByMaxInterestRate(creditOffers, 10);
    }

    @Test
    public void testExecute_ReturnsFilteredOffers() {
        List<CreditOffer> creditOfferList = new ArrayList<>();
        creditOfferList.add(new CreditOffer(1,"Універсал Банк", "Автокредит", 11, 36, true, true, 400000));
        creditOfferList.add(new CreditOffer(2,"Фінансова Група", "Житловий кредит", 13, 48, false, true, 3000000));
        creditOfferList.add(new CreditOffer(3,"Економія Банк", "Освітній кредит", 7, 60, true, false, 100000));
        creditOfferList.add(new CreditOffer(4,"Фінанс Брокер", "Бізнес-кредит", 8, 24, true, false, 2000000));
        creditOfferList.add(new CreditOffer(5,"Прогрес Банк", "Бізнес-кредит", 6, 36, false, true, 5000000));

        creditOffers.addAll(creditOfferList);

        List<CreditOffer> expectedFilteredOffers = new ArrayList<>();
        expectedFilteredOffers.add(creditOfferList.get(2));
        expectedFilteredOffers.add(creditOfferList.get(3));
        expectedFilteredOffers.add(creditOfferList.get(4));

        List<CreditOffer> filteredOffers = searchByMaxInterestRate.execute();

        assertEquals(expectedFilteredOffers.size(), filteredOffers.size());
        assertEquals(expectedFilteredOffers, filteredOffers);
    }
}
