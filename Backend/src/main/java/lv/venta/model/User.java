package lv.venta.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Table(name = "User")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {

	@Id
	@Column(name = "UserId")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Setter(value = AccessLevel.NONE)
	private int userId;

	private String fullName;

	private String email;

	private Role role;

	@JsonProperty("dtype")
	public String getDtype() {
		return this.getClass().getSimpleName();
	}

	public User(String fullName, String email, Role role) {
		setFullName(fullName);
		setEmail(email);
		setRole(role);
	}

}
