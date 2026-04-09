package hackerton.educationentity.domain.school.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
@Table(name = "school")
public class School {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id", nullable = false)
    private Long id;

    @Column(name = "school_name", nullable = false)
    private String name;

    @Column(name = "school_address", nullable = false)
    private String address;

    @Column(name = "school_phone", nullable = false)
    private String phone;

    @Column(name = "school_type", nullable = false)
    private String type;
}
