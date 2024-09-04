package ra.project_module04.model.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import ra.project_module04.constants.OrderStatus;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column( nullable = false, unique = true, length = 100)
    @Builder.Default
    private String serialNumber = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn( nullable = false)
    private Users users;

    @Column( nullable = false, columnDefinition = "Decimal(10,2)")
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column( length = 100)
    private String receiveName;

    @Column( length = 254)
    private String receiveAddress;

    @Column( length = 15)
    private String receivePhone;

    @Column( length = 100)
    private String note;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(nullable = false)
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column( nullable = false)
    private Date receivedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
     List<OrderDetails> orderDetails;

}
