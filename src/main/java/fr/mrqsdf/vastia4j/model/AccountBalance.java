package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;

/**
 * Represents account balance information from Vast.ai.
 */
public class AccountBalance {

    @SerializedName("balance")
    private double balance;

    @SerializedName("credit")
    private double credit;

    public double getBalance() {
        return balance;
    }

    public double getCredit() {
        return credit;
    }
}
