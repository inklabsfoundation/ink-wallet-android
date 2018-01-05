package ink.qtum.org.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import ink.qtum.org.models.TransactionHistory;
import ink.qtum.org.models.response.TransactionsListResponse;
import ink.qtum.org.models.response.Tx;
import ink.qtum.org.models.response.Vin;
import ink.qtum.org.models.response.Vout;

import static ink.qtum.org.models.Extras.QTUM_ID;

/**
 * Created by SV on 04.01.2018.
 */

public class QtumTransactionHistoryConverter {
    public static List<TransactionHistory> convertToFriendlyList(TransactionsListResponse response, String ownAddres) {
        List<TransactionHistory> transactions = new ArrayList<>();
        for (Tx tx : response.getTxs()) {
            calculateChangeInBalance(tx, ownAddres);
            TransactionHistory transaction = new TransactionHistory();
            transaction.setBlockHeight(tx.getBlockheight());
            transaction.setFees(tx.getFees());
            transaction.setTimestamp(tx.getTime() * 1000L);
            transaction.setTxHash(tx.getTxid());
            transaction.setValue(tx.getValueOut());
            transaction.setCoinId(QTUM_ID);
            if (isInTx(tx, ownAddres)) {
                transaction.setFromAddress(tx.getVin().get(0).getAddr());
                transaction.setToAddress(ownAddres);
                transaction.setIsInTx(true);
            } else {
                transaction.setFromAddress(ownAddres);
                transaction.setToAddress(tx.getVout().get(0).getAddress());
                transaction.setIsInTx(false);
            }
            transactions.add(transaction);
        }
        return transactions;
    }

    private static boolean isInTx(Tx tx, String ownAddress) {
        for (Vin vin : tx.getVin()) {
            if (vin.getAddr().equals(ownAddress)) {
                return false;
            }
        }
        return true;
    }

    private static void calculateChangeInBalance(Tx tx, String addresses) {
        BigDecimal changeInBalance = calculateVout(tx, addresses).subtract(calculateVin(tx, addresses));
        tx.setChangeInBalance(changeInBalance);
    }

    private static BigDecimal calculateVin(Tx tx, String ownAddress) {
        BigDecimal totalVin = new BigDecimal("0.0");
        boolean equals = false;
        for (Vin vin : tx.getVin()) {
            if (vin.getAddr().equals(ownAddress)) {
                vin.setOwnAddress(true);
                equals = true;
            }
        }
        if (equals) {
            totalVin = tx.getValueIn();
        }
        return totalVin;
    }

    private static BigDecimal calculateVout(Tx history, String ownAddress) {
        BigDecimal totalVout = new BigDecimal("0.0");
        for (Vout vout : history.getVout()) {
            if (vout.getAddress().equals(ownAddress)) {
                vout.setOwnAddress(true);
                totalVout = totalVout.add(vout.getValue());
            }
        }
        return totalVout;
    }
}
