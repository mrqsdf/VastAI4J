package fr.mrqsdf.vastai4j.model;

import com.google.gson.annotations.SerializedName;
import fr.mrqsdf.vastai4j.auth.Right;

/**
 * Represents the current user information from Vast.ai.
 * This record contains various fields related to the user's account and settings.
 *
 * @param canPay                  indicates if the user can make payments
 * @param id                      the unique identifier of the user
 * @param createdAt               the account creation timestamp in epoch seconds
 * @param apiKey                  the API key associated with the user
 * @param keyId                   the ID of the API key
 * @param username                the username of the user
 * @param sshKey                  the SSH key associated with the user
 * @param phoneNumber             the user's phone number
 * @param paypalEmail             the user's PayPal email
 * @param wiseEmail               the user's Wise email
 * @param fullname                the full name of the user
 * @param balanceThreshold        the balance threshold for notifications
 * @param balanceThresholdEnabled indicates if balance threshold notifications are enabled
 * @param autobillThreshold       the threshold for automatic billing
 * @param totalSpend              the total amount spent by the user
 * @param autobillAmount          the amount for automatic billing
 * @param billaddressLine1        the first line of the billing address
 * @param billaddressLine2        the second line of the billing address
 * @param billaddressCity         the city of the billing address
 * @param billaddressZip          the ZIP code of the billing address
 * @param billaddressCountry      the country of the billing address
 * @param billingCreditonly       indicates if billing is credit-only
 * @param billaddressTaxinfo      the tax information for the billing address
 * @param passwordResettable      indicates if the user can reset their password
 * @param email                   the user's email address
 * @param hasBilling              indicates if the user has billing information set up
 * @param hasPayout               indicates if the user has payout information set up
 * @param payoutEnabled           indicates if payouts are enabled for the user
 * @param hostOnly                indicates if the user is a host-only account
 * @param hostAgreementAccepted   indicates if the user has accepted the host agreement
 * @param emailVerified           indicates if the user's email is verified
 * @param last4                   the last four digits of the user's payment method
 * @param balance                 the current account balance
 * @param credit                  the available credit
 * @param gotSignupCredit         indicates if the user received signup credit
 * @param user                    the username (redundant with username field)
 * @param paidVerified            the amount of verified payments
 * @param paidExpected            the amount of expected payments
 * @param billedVerified          the amount of verified billings
 * @param billedExpected          the amount of expected billings
 * @param hasRented               indicates if the user has rented instances
 * @param balanceReferrals        the balance from referrals
 * @param discordId               the user's Discord ID
 * @param oauthProvider           the OAuth provider used for authentication
 * @param rights                  the rights and permissions of the user
 */
public record CurrentUser(
        @SerializedName("can_pay") Boolean canPay,
        @SerializedName("id") Long id,
        @SerializedName("created_at") Long createdAt,
        @SerializedName("api_key") String apiKey,
        @SerializedName("key_id") Integer keyId,
        @SerializedName("username") String username,
        @SerializedName("ssh_key") String sshKey,
        @SerializedName("phone_number") String phoneNumber,
        @SerializedName("paypal_email") String paypalEmail,
        @SerializedName("wise_email") String wiseEmail,
        @SerializedName("fullname") String fullname,
        @SerializedName("balance_threshold") Double balanceThreshold,
        @SerializedName("balance_threshold_enabled") Boolean balanceThresholdEnabled,
        @SerializedName("autobill_threshold") Double autobillThreshold,
        @SerializedName("total_spend") Double totalSpend,
        @SerializedName("autobill_amount") Double autobillAmount,
        @SerializedName("billaddress_line1") String billaddressLine1,
        @SerializedName("billaddress_line2") String billaddressLine2,
        @SerializedName("billaddress_city") String billaddressCity,
        @SerializedName("billaddress_zip") String billaddressZip,
        @SerializedName("billaddress_country") String billaddressCountry,
        @SerializedName("billing_creditonly") Integer billingCreditonly,
        @SerializedName("billaddress_taxinfo") String billaddressTaxinfo,
        @SerializedName("password_resettable") Boolean passwordResettable,
        @SerializedName("email") String email,
        @SerializedName("has_billing") Boolean hasBilling,
        @SerializedName("has_payout") Boolean hasPayout,
        @SerializedName("payout_enabled") Boolean payoutEnabled,
        @SerializedName("host_only") Boolean hostOnly,
        @SerializedName("host_agreement_accepted") Boolean hostAgreementAccepted,
        @SerializedName("email_verified") Boolean emailVerified,
        @SerializedName("last4") String last4,
        @SerializedName("balance") Double balance,
        @SerializedName("credit") Double credit,
        @SerializedName("got_signup_credit") Integer gotSignupCredit,
        @SerializedName("user") String user,
        @SerializedName("paid_verified") Double paidVerified,
        @SerializedName("paid_expected") Double paidExpected,
        @SerializedName("billed_verified") Double billedVerified,
        @SerializedName("billed_expected") Double billedExpected,
        @SerializedName("has_rented") Boolean hasRented,
        @SerializedName("balance_referrals") Double balanceReferrals,
        @SerializedName("discord_id") String discordId,
        @SerializedName("oauth_provider") String oauthProvider,
        @SerializedName("rights") Right rights
) {

}
