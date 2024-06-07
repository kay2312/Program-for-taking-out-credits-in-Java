package creditOffer;

public class Credit {
    String bank, loanType;
    int id, percentRate, loanTerm, creditAmount;
    boolean earlyRepayment, creditLineIncrease;

    public Credit(int id, String bank, String loanType, int percentRate, int loanTerm, boolean earlyRepayment, boolean creditLineIncrease, int creditAmount) {
        this.id = id;
        this.bank = bank;
        this.loanType = loanType;
        this.percentRate = percentRate;
        this.loanTerm = loanTerm;
        this.earlyRepayment = earlyRepayment;
        this.creditLineIncrease = creditLineIncrease;
        this.creditAmount = creditAmount;
    }

    // Гетери
    public String getBank() {
        return bank;
    }

    public String getLoanType() {
        return loanType;
    }

    public int getPercentRate() {
        return percentRate;
    }
    public int getId() {
        return id;
    }

    public int getLoanTerm() {
        return loanTerm;
    }

    public boolean getEarlyRepayment() {
        return earlyRepayment;
    }

    public boolean getCreditLineIncrease() {
        return creditLineIncrease;
    }

    // Сетери
    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setPercentRate(int percentRate) {
        this.percentRate = percentRate;
    }
    public void setId(int percentRate) {
        this.id = id;
    }

    public void setLoanTerm(int loanTerm) {
        this.loanTerm = loanTerm;
    }

    public void setEarlyRepayment(boolean earlyRepayment) {
        this.earlyRepayment = earlyRepayment;
    }

    public void setCreditLineIncrease(boolean creditLineIncrease) {
        this.creditLineIncrease = creditLineIncrease;
    }

    public int getCreditAmount() {
        return creditAmount;
    }
    public void setCreditAmount(int creditAmount) {
        this.creditAmount = creditAmount;
    }
}
