package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints.Required;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class CMSPage extends Model {

    @Required
    @Id
    private String name;
    @Required
    private String title;
    @Required
    @Lob
    @Column
    private String body;

    public static final Finder<String, CMSPage> find = new Finder<>(CMSPage.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
