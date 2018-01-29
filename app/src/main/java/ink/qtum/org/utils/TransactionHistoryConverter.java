package ink.qtum.org.utils;

import org.bitcoinj.core.Coin;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import ink.qtum.org.models.TransactionHistory;
import ink.qtum.org.models.response.TransactionsInkListResponse;
import ink.qtum.org.models.response.TransactionsQtumListResponse;
import ink.qtum.org.models.response.Tx;
import ink.qtum.org.models.response.TxInk;
import ink.qtum.org.models.response.Vin;
import ink.qtum.org.models.response.Vout;

import static ink.qtum.org.models.Constants.BALANCE_SHOW_PATTERN;
import static ink.qtum.org.models.Extras.INK_ID;
import static ink.qtum.org.models.Extras.QTUM_ID;

/**
 * Created by SV on 04.01.2018.
 */

public class TransactionHistoryConverter {
    public static List<TransactionHistory> convertQtumToFriendlyList(TransactionsQtumListResponse response, String ownAddres) {
        List<TransactionHistory> transactions = new ArrayList<>();
        for (Tx tx : response.getTxs()) {
            calculateChangeInBalance(tx, ownAddres);
            TransactionHistory transaction = new TransactionHistory();
            transaction.setBlockHeight(tx.getBlockheight());
            transaction.setFees(tx.getFees());
            transaction.setTimestamp(tx.getTime() * 1000L);
            transaction.setTxHash(tx.getTxid());
            transaction.setCoinId(QTUM_ID);
            if (isInTx(tx, ownAddres)) {
                transaction.setFromAddress(tx.getVin().get(0).getAddr());
                transaction.setToAddress(ownAddres);
                transaction.setIsInTx(true);
                transaction.setRawValue(getInTxValue(tx, ownAddres));
                transaction.setFriendlyValue(getFriendlyValueQtum(getInTxValue(tx, ownAddres)));
            } else {
                transaction.setFromAddress(ownAddres);
                transaction.setToAddress(tx.getVout().get(0).getAddress());
                transaction.setIsInTx(false);
                transaction.setRawValue(tx.getValueOut());
                transaction.setFriendlyValue(getFriendlyValueQtum(tx.getValueOut()));
            }
            transactions.add(transaction);
        }
        return transactions;
    }

    public static List<TransactionHistory> convertInkToFriendlyList(TransactionsInkListResponse response, String ownAddres) {
        List<TransactionHistory> transactions = new ArrayList<>();
        for (TxInk tx : response.getTxs()) {
            TransactionHistory transaction = new TransactionHistory();
            transaction.setBlockHeight(tx.getBlockHeight());
            transaction.setFees(new BigDecimal(0));
            transaction.setTimestamp(System.currentTimeMillis());
            transaction.setTxHash(tx.getTxHash());
            transaction.setCoinId(INK_ID);
            transaction.setFromAddress(tx.getFromAddress());
            transaction.setToAddress(tx.getToAddress());
            transaction.setIsInTx(!tx.getFromAddress().equalsIgnoreCase(ownAddres));
            transaction.setRawValue(new BigDecimal(tx.getValue()));
            transaction.setDescription(" ");
            transaction.setFriendlyValue(getFriendlyValueInkForHistory(tx.getValue()));
            transactions.add(transaction);
        }
        return transactions;
    }

    private static String getFriendlyValueQtum(BigDecimal value) {
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setDecimalSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat(BALANCE_SHOW_PATTERN, symbols);
        return decimalFormat.format(value);
    }

    public static String getFriendlyValueInk(String inkBalanceStr) {
        Long balanceInkLong = Long.parseLong(inkBalanceStr);
        BigInteger value = new BigInteger(String.valueOf(balanceInkLong), 10);
        BigDecimal decimal = new BigDecimal(value);
        BigDecimal formatted = decimal.divide(BigDecimal.TEN.pow(9));
        return formatted.toString();
    }

    private static String getFriendlyValueInkForHistory(String inkBalanceStr) {
        Coin value = Coin.valueOf(TransactionHistoryConverter.inkBalanceToSatoshiLong(inkBalanceStr));
        return value.toPlainString();
    }

    public static long inkBalanceToSatoshiLong(String inkBalanceStr) {
        return Long.parseLong(inkBalanceStr) / 10;
    }


    private static BigDecimal getInTxValue(Tx tx, String ownAddress) {
        for (Vout vout : tx.getVout()) {
            if (vout.getAddress().equals(ownAddress)) {
                return vout.getValue();
            }
        }
        return null;
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
