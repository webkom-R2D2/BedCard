package no.abakus.naut.ws.bedcard;

/**
 * @author Erik Drolshammer
 *
 */
public class UserDto {
	private Long userBeanId;
    private String username; 
    private String cn;
    private String sn;

	private Integer cardNumber;
    private Long rfidCardNumber;

    private boolean hasAccess;

    public Long getUserBeanId() {
        return userBeanId;
    }

    public void setUserBeanId(Long userBeanId) {
        this.userBeanId = userBeanId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
 
    public Integer getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Integer cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Long getRfidCardNumber() {
        return rfidCardNumber;
    }

    public void setRfidCardNumber(Long rfidCardNumber) {
        this.rfidCardNumber = rfidCardNumber;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }
}
