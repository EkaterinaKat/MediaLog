package org.katyshevtseva.medialog.core.films.model;

import com.katyshevtseva.hibernate.HasId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Actor implements HasId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long kpId;

    private String photoUrl;

    private String name;

    private String enName;

    @Transient
    private Boolean hasLoadedPhoto;

    @OneToMany(mappedBy = "actor", fetch = FetchType.LAZY)
    private Set<Role> roles;

    @Override
    public String toString() {
        return name;
    }

    public String getNameNonNull() {
        return name == null ? enName : name;
    }

    public int getNumOfRoles() {
        return roles.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Actor actor = (Actor) o;
        return id.equals(actor.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
