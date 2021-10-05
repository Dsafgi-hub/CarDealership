package ru.bachinin.cardealership.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users", schema = "public")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(name = "defaultSeq", sequenceName = "USERS_SEQ", allocationSize = 1)
public class User extends UpdatedAndCreatedBaseEntity implements Serializable {
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "second_name")
    private String secondName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "id_user", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "id_role", referencedColumnName = "id")})
    private List<UserRole> roles;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "createdBy")
    private List<Invoice> invoices;

    public User() {
    }

    public User(String login, String password, String surname, String firstName, String secondName) {
        this.login = login;
        this.password = password;
        this.surname = surname;
        this.firstName = firstName;
        this.secondName = secondName;
    }

    public User(String login, String password, String surname, String firstName) {
        this.login = login;
        this.password = password;
        this.surname = surname;
        this.firstName = firstName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }
}
