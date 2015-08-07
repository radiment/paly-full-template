package models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.avaje.ebean.Model;
import com.feth.play.module.pa.user.AuthUser;

@Entity
public class LinkedAccount extends Model {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @ManyToOne
    private User user;

    private String providerUserId;
    private String providerKey;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

    public String getProviderKey() {
        return providerKey;
    }

    public void setProviderKey(String providerKey) {
        this.providerKey = providerKey;
    }

    public static final Finder<Long, LinkedAccount> find = new Finder<>(LinkedAccount.class);

    public static LinkedAccount findByProviderKey(final User user, String key) {
        return find.where().eq("user", user).eq("providerKey", key)
                .findUnique();
    }

    public static LinkedAccount create(final AuthUser authUser) {
        final LinkedAccount ret = new LinkedAccount();
        ret.update(authUser);
        return ret;
    }

    public void update(final AuthUser authUser) {
        this.setProviderKey(authUser.getProvider());
        this.setProviderUserId(authUser.getId());
    }

    public static LinkedAccount create(final LinkedAccount acc) {
        final LinkedAccount ret = new LinkedAccount();
        ret.setProviderKey(acc.getProviderKey());
        ret.setProviderUserId(acc.getProviderUserId());

        return ret;
    }
}