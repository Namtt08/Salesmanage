package org.project.manage.entities;

import org.hibernate.annotations.NaturalId;
import org.project.manage.security.ERole;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Setter
@Getter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId
    @Column(length = 60)
    private ERole name;
    
    public Role() {
       
    }
}
