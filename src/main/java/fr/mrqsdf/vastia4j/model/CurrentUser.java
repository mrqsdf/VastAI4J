package fr.mrqsdf.vastia4j.model;

import com.google.gson.annotations.SerializedName;
import fr.mrqsdf.vastia4j.auth.Right;

public final class CurrentUser {

    @SerializedName("can_pay") private Boolean canPay;
    @SerializedName("id") private Long id;
    @SerializedName("created_at") private Long createdAt;
    @SerializedName("api_key") private String apiKey;
    @SerializedName("key_id") private Integer keyId;
    @SerializedName("username") private String username;
    @SerializedName("ssh_key") private String sshKey;
    @SerializedName("phone_number") private String phoneNumber;
    @SerializedName("paypal_email") private String paypalEmail;
    @SerializedName("wise_email") private String wiseEmail;
    @SerializedName("fullname") private String fullname;
    @SerializedName("balance_threshold") private Double balanceThreshold;
    @SerializedName("balance_threshold_enabled") private Boolean balanceThresholdEnabled;
    @SerializedName("autobill_threshold") private Double autobillThreshold;
    @SerializedName("total_spend" ) private Double totalSpend;
    @SerializedName("autobill_amount") private Double autobillAmount;
    @SerializedName("billaddress_line1") private String billaddressLine1;
    @SerializedName("billaddress_line2") private String billaddressLine2;
    @SerializedName("billaddress_city") private String billaddressCity;
    @SerializedName("billaddress_zip") private String billaddressZip;
    @SerializedName("billaddress_country") private String billaddressCountry;
    @SerializedName("billing_creditonly") private Integer billingCreditonly;
    @SerializedName("billaddress_taxinfo") private String billaddressTaxinfo;
    @SerializedName("password_resettable") private Boolean passwordResettable;
    @SerializedName("email") private String email;
    @SerializedName("has_billing") private Boolean hasBilling;
    @SerializedName("has_payout") private Boolean hasPayout;
    @SerializedName("payout_enabled") private Boolean payoutEnabled;
    @SerializedName("host_only") private Boolean hostOnly;
    @SerializedName("host_agreement_accepted") private Boolean hostAgreementAccepted;
    @SerializedName("email_verified") private Boolean emailVerified;
    @SerializedName("last4") private String last4;
    @SerializedName("balance") private Double balance;
    @SerializedName("credit") private Double credit;
    @SerializedName("got_signup_credit") private Integer gotSignupCredit;
    @SerializedName("user") private String user;
    @SerializedName("paid_verified") private Double paidVerified;
    @SerializedName("paid_expected") private Double paidExpected;
    @SerializedName("billed_verified") private Double billedVerified;
    @SerializedName("billed_expected") private Double billedExpected;
    @SerializedName("has_rented") private Boolean hasRented;
    @SerializedName("balance_referrals") private Double balanceReferrals;
    @SerializedName("discord_id") private String discordId;
    @SerializedName("oauth_provider") private String oauthProvider;
    @SerializedName("rights") private Right rights;

    public Boolean getCanPay() {
        return canPay;
    }

    public Long getId() {
        return id;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public String getApiKey() {
        return apiKey;
    }

    public Integer getKeyId() {
        return keyId;
    }

    public String getUsername() {
        return username;
    }

    public String getSshKey() {
        return sshKey;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public String getWiseEmail() {
        return wiseEmail;
    }

    public String getFullname() {
        return fullname;
    }

    public Double getBalanceThreshold() {
        return balanceThreshold;
    }

    public Boolean getBalanceThresholdEnabled() {
        return balanceThresholdEnabled;
    }

    public Double getAutobillThreshold() {
        return autobillThreshold;
    }

    public Double getTotalSpend() {
        return totalSpend;
    }

    public Double getAutobillAmount() {
        return autobillAmount;
    }

    public String getBilladdressLine1() {
        return billaddressLine1;
    }

    public String getBilladdressLine2() {
        return billaddressLine2;
    }

    public String getBilladdressCity() {
        return billaddressCity;
    }

    public String getBilladdressZip() {
        return billaddressZip;
    }

    public String getBilladdressCountry() {
        return billaddressCountry;
    }

    public Integer getBillingCreditonly() {
        return billingCreditonly;
    }

    public String getBilladdressTaxinfo() {
        return billaddressTaxinfo;
    }

    public Boolean getPasswordResettable() {
        return passwordResettable;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getHasBilling() {
        return hasBilling;
    }

    public Boolean getHasPayout() {
        return hasPayout;
    }

    public Boolean getPayoutEnabled() {
        return payoutEnabled;
    }

    public Boolean getHostOnly() {
        return hostOnly;
    }

    public Boolean getHostAgreementAccepted() {
        return hostAgreementAccepted;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public String getLast4() {
        return last4;
    }

    public Double getBalance() {
        return balance;
    }

    public Double getCredit() {
        return credit;
    }

    public Integer getGotSignupCredit() {
        return gotSignupCredit;
    }

    public String getUser() {
        return user;
    }

    public Double getPaidVerified() {
        return paidVerified;
    }

    public Double getPaidExpected() {
        return paidExpected;
    }

    public Double getBilledVerified() {
        return billedVerified;
    }

    public Double getBilledExpected() {
        return billedExpected;
    }

    public Boolean getHasRented() {
        return hasRented;
    }

    public Double getBalanceReferrals() {
        return balanceReferrals;
    }

    public String getDiscordId() {
        return discordId;
    }

    public String getOauthProvider() {
        return oauthProvider;
    }

    public Right getRights() {
        return rights;
    }
}
