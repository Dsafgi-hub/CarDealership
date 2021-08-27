package ru.bachinin.cardealership.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_role")
public class UserRole implements Serializable {
        @Id
        @SequenceGenerator(name = "userRoleSequence", sequenceName = "USER_ROLE_SEQUENCE", allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userRoleSequence")
        @Column(name = "id", nullable = false)
        private Integer id;

        @Column(name = "role", nullable = false)
        private String role;

        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public String getRole() {
                return role;
        }

        public void setRole(String role) {
                this.role = role;
        }
}
