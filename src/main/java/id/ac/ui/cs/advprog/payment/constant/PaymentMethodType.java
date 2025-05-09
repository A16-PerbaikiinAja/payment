package id.ac.ui.cs.advprog.payment.constant;

public enum PaymentMethodType {
    COD,
    BANK_TRANSFER,
    E_WALLET,
    INVALID;

    public static PaymentMethodType fromString(String type) {
        return switch (type.toUpperCase()) {
            case "COD" -> COD;
            case "BANK_TRANSFER" -> BANK_TRANSFER;
            case "E_WALLET" -> E_WALLET;
            default -> INVALID;
        };
    }
}