package hh.hw.javadb.employers;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "employers")
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "employer_id")
    private Integer id;

    @Column(name = "title", unique=true)
    private String title;

    @Column(name = "registration_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;
    
    Employer() {
        this.id = null; 
        this.regDate = new Date();
    }

    Employer(Integer id, String title) {
        this.id = id;
        this.title = title;
        this.regDate = new Date();
    }

    public Employer(String title) {
        this(null, title);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("{id='%s', title='%s', regDate='%s'}", id, title, regDate);
    }

    @Override
    public int hashCode() {
        return getId() + (int) getRegDate().getTime() + getTitle().length();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Employer other = (Employer) obj;
        return id.equals(other.getId()) && title.equals(other.getTitle()) && regDate.equals(other.getRegDate());
    }

}
