package model;

import com.google.gson.annotations.SerializedName;

public enum TransactionType {
    @SerializedName("deposit")
    DEPOSIT,
    @SerializedName("withdraw")
    WITHDRAW,
    @SerializedName("transfer_in")
    TRANSFER_IN,
    @SerializedName("transfer_out")
    TRANSFER_OUT
}