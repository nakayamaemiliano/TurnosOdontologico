package com.emiliano.turnosOdontologico.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "odontologos",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "matricula")
        }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Odontologo {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false,length = 30)
        private String nombre;

        @Column(nullable = false,length = 30)
        private String apellido;

        @Column(nullable = false, length = 30)
        private String matricula;

        @Column(nullable = false,length = 40)
        private String especialidad;


}
