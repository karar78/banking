package com.fractal.banking.model;

import java.util.Objects;

public class AuthorizationToken {
	private String access_token;
	private String partner_name;
	private String partner_id;
	private String expires;
	private String token_type;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getPartner_name() {
		return partner_name;
	}

	public void setPartner_name(String partner_name) {
		this.partner_name = partner_name;
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getExpires() {
		return expires;
	}

	public void setExpires(String expires) {
		this.expires = expires;
	}

	public String getToken_type() {
		return token_type;
	}

	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(access_token, expires, partner_id, partner_name, token_type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorizationToken other = (AuthorizationToken) obj;
		return Objects.equals(access_token, other.access_token) && Objects.equals(expires, other.expires)
				&& Objects.equals(partner_id, other.partner_id) && Objects.equals(partner_name, other.partner_name)
				&& Objects.equals(token_type, other.token_type);
	}

	@Override
	public String toString() {
		return "AuthorizationToken [access_token=" + access_token + ", partner_name=" + partner_name + ", partner_id="
				+ partner_id + ", expires=" + expires + ", token_type=" + token_type + "]";
	}

}
