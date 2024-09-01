package ra.project_module04.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder

public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_address", nullable = false, length = 255)
    private String fullAddress;

    @Column(name = "phone", nullable = false, length = 15)
    private String phone;

    @Column(name = "receive_name", nullable = false, length = 50)
    private String receiveName;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;
}
