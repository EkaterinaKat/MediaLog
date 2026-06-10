package org.katyshevtseva.medialog.core.films.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    private String description;

    public boolean descriptionIsEmpty(){
        return description==null;
    }

    public boolean actorDoesntHavePhoto(){
        return !actor.getHasLoadedPhoto();
    }

    public String getNameAndDescNonNull() {
        String name = actor.getNameNonNull();
        if (description != null) {
            name += ("\n" + description);
        }
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        return id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
