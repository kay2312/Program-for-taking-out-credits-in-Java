package command;

import java.util.List;
import creditOffer.CreditOffer;

public interface ResultCommand {
    List<CreditOffer> execute();
}
