package it.unicam.cs.mpgc.jbudget109164.model.account;

public enum AccountType {
    ASSET {
        @Override
        public boolean isValidOpeningBalance(double amount) {
            return amount >= 0;
        }
    },

    LIABILITY {
        @Override
        public boolean isValidOpeningBalance(double amount) {
            return amount <= 0;
        }
    };

    /**
     * Validates the opening balance for the account accountType.
     *
     * @param amount the opening balance to validate
     * @return true if the opening balance is valid, false otherwise
     */
    public abstract boolean isValidOpeningBalance(double amount);

}