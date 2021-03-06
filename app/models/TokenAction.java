package models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import play.data.format.Formats;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.QueryIterator;
import com.avaje.ebean.annotation.EnumValue;

@Entity
public class TokenAction extends Model {

	public enum Type {
		@EnumValue("EV")
		EMAIL_VERIFICATION,

		@EnumValue("PR")
		PASSWORD_RESET
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Verification time frame (until the user clicks on the link in the email)
	 * in seconds
	 * Defaults to one week
	 */
	private final static long VERIFICATION_TIME = 7 * 24 * 3600;

	@Id
	private Long id;

	@Column(unique = true)
	private String token;

	@ManyToOne
	private User targetUser;

	private Type type;

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date created;

	@Formats.DateTime(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date expires;

	public static final Finder<Long, TokenAction> find = new Finder<>(TokenAction.class);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getTargetUser() {
		return targetUser;
	}

	public void setTargetUser(User targetUser) {
		this.targetUser = targetUser;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public static TokenAction findByToken(final String token, final Type type) {
		return find.where().eq("token", token).eq("type", type).findUnique();
	}

	public static void deleteByUser(final User u, final Type type) {
		QueryIterator<TokenAction> iterator = find.where()
				.eq("targetUser.id", u.getId()).eq("type", type).findIterate();
		Ebean.delete(iterator);
		iterator.close();
	}

	public boolean isValid() {
		return this.getExpires().after(new Date());
	}

	public static TokenAction create(final Type type, final String token,
			final User targetUser) {
		final TokenAction ua = new TokenAction();
		ua.setTargetUser(targetUser);
		ua.setToken(token);
		ua.setType(type);
		final Date created = new Date();
		ua.setCreated(created);
		ua.setExpires(new Date(created.getTime() + VERIFICATION_TIME * 1000));
		ua.save();
		return ua;
	}
}
