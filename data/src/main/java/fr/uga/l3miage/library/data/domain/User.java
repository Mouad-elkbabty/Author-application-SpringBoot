package fr.uga.l3miage.library.data.domain;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;

// Attention le mot 'user' est reservé
@NamedQuery(name = "findAll", query = "SELECT u FROM User u")
@Entity
@DiscriminatorValue("1")
public class User extends Person {

    private Date registered;

    private float lateRatio;

    @OneToOne
    private Borrow borrow;

    public Borrow getBorrow() {
        return this.borrow;
    }

    public void set(Borrow borrow) {
        this.borrow = borrow;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public float getLateRatio() {
        return lateRatio;
    }

    public void setLateRatio(float lateRatio) {
        this.lateRatio = lateRatio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof User user))
            return false;
        if (!super.equals(o))
            return false;
        return Float.compare(user.lateRatio, lateRatio) == 0 && Objects.equals(registered, user.registered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), registered, lateRatio);
    }
}
