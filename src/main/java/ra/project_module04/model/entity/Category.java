package ra.project_module04.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name ="categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String categoryName;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean status;

}
