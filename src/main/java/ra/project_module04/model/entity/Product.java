package ra.project_module04.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;


import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, unique = true, length = 100)
    @Builder.Default
    private String sku= UUID.randomUUID().toString();

    @Column( nullable = false, unique = true, length = 100)
    private String productName;

    @Column( columnDefinition = "TEXT")
    private String description;

    @Column( nullable = false, columnDefinition = "Decimal(10,2)")
    private Double unitPrice;

    @Column( nullable = false)
    private Integer stockQuantity;

    @Column( length = 255)
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

//    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date createdAt;

//    @JsonFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date updatedAt;

    private Boolean status;
}
