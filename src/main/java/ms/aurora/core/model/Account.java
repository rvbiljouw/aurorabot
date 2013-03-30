package ms.aurora.core.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name = "account.getAll", query = "select a from Account a")
})
public class Account extends AbstractModel {
	@Id
	@GeneratedValue
	private Long id;
	private String username = "";
	private String password = "";
	private String bankPin = "0000";

	public Account(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public Account() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getBankPin() {
		return bankPin;
	}


	public void setBankPin(String bankPin) {
		this.bankPin = bankPin;
	}
	
	public static List<Account> getAll() {
		return getEm().createNamedQuery("account.getAll", Account.class).getResultList();
	}
	
}
