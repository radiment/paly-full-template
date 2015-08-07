package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import be.objectify.deadbolt.core.models.Permission;
import com.avaje.ebean.Model;

/**
 * Initial version based on work by Steve Chaloner (steve@objectify.be) for
 * Deadbolt2
 */
@Entity
public class UserPermission extends Model implements Permission {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    public Long id;

    public String value;

    public static final Finder<Long, UserPermission> find = new Finder<>(UserPermission.class);

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static UserPermission findByValue(String value) {
        return find.where().eq("value", value).findUnique();
    }
}
