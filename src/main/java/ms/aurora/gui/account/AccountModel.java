package ms.aurora.gui.account;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import ms.aurora.core.model.Account;

/**
 * @author rvbiljouw
 */
public class AccountModel {
    private final Account account;
    private StringProperty username;
    private StringProperty password;
    private StringProperty bankPin;


    public AccountModel(Account account) {
        this.account = account;
        this.username = new SimpleStringProperty(account.getUsername());
        this.password = new SimpleStringProperty(account.getPassword());
        this.bankPin = new SimpleStringProperty(account.getBankPin());
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        getAccount().setUsername(username);
        this.username.set(username);
    }

    public String getPassword() {
        String maskedPassword = "";
        for(int i = 0; i < password.get().length(); i++) {
            maskedPassword += "*";
        }
        return maskedPassword;
    }

    public String getRealPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        getAccount().setPassword(password);
        this.password.set(password);
    }

    public String getBankPin() {
        return bankPin.get();
    }

    public void setBankPin(String bankPin) {
        getAccount().setBankPin(bankPin);
        this.bankPin.set(bankPin);
    }

    public Account getAccount() {
        return account;
    }
}