package ra.project_module04.model.entity;

import jakarta.persistence.*;
import lombok.*;
import ra.project_module04.constants.RoleName;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING) //Chuyển đổi về chuỗi
    private RoleName roleName;
}
