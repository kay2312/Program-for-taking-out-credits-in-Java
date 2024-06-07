package creditOffer;

/**
 * Клас, який містить гетери, сетери змінних та метод toString для даних про кредити
 */
public class CreditOffer {
    private int id;
    private final String bankName;
    private final String creditName;
    private final int interestRate;
    private int loanTerm;
    private int sumCredit;
    private final boolean earlyRepaymentEnabled;
    private final boolean creditLineIncreaseEnabled;

    public CreditOffer(int id, String bankName, String creditName, int interestRate, int loanTerm,
                       boolean earlyRepaymentEnabled, boolean creditLineIncreaseEnabled, int sumCredit) {
        this.id = id;
        this.bankName = bankName;
        this.creditName = creditName;
        this.interestRate = interestRate;
        this.loanTerm = loanTerm;
        this.earlyRepaymentEnabled = earlyRepaymentEnabled;
        this.creditLineIncreaseEnabled = creditLineIncreaseEnabled;
        this.sumCredit = sumCredit;
    }

    public int getId() {
        return id;
    }
    public String getBankName() {
        return bankName;
    }
    public String getCreditName() {
        return creditName;
    }

    public int getInterestRate() {
        return interestRate;
    }

    public int getLoanTerm() {
        return loanTerm;
    }
    public int getSumCredit() {
        return sumCredit;
    }


    public boolean getEarlyRepaymentEnabled() {
        return earlyRepaymentEnabled;
    }

    public boolean getCreditLineIncreaseEnabled() {
        return creditLineIncreaseEnabled;
    }
}
