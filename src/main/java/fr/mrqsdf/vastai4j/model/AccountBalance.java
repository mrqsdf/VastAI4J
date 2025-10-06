package fr.mrqsdf.vastai4j.model;

/**
 * Represents account balance information from Vast.ai.
 * @param balance the current account balance
 * @param credit the available credit
 */
public record AccountBalance(double balance, double credit) {

}
