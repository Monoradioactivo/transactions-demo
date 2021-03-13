package mx.simio.transactionsdemo.api.util;

public class AppConstant {
    private AppConstant() {
        throw new IllegalStateException("Utility class");
    }

    public static final String API_BASE_URI = "/api/v1";
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found";
    public static final String TRANSACTIONS_NOT_FOUND = "Transactions not found";
    public static final String USER_NOT_FOUND = "User not found";
}
