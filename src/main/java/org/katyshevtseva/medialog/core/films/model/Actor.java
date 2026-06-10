package org.katyshevtseva.medialog.core.films.model;

import com.katyshevtseva.hibernate.HasId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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

    @OneToMany(mappedBy = "actor", fetch = FetchType.EAGER)
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
}
