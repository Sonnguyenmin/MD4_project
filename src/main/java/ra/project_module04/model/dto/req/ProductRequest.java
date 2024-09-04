package ra.project_module04.model.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class ProductRequest {
    @NotBlank(message = "sku không được để trống")
    private String sku= UUID.randomUUID().toString();

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String productName;

    private String description;

    @NotNull(message = "Đơn giá không được để trống")
//    @Column(columnDefinition = "Decimal(10,2)")
    private Double unitPrice;

    @NotNull(message = "Số lượng không được để trống")
    @Min(0)
    private Integer stockQuantity;

    @NotNull(message = "Ảnh không được để trống")
    private MultipartFile image;

    private Long categoryId;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date updatedAt;

    private Boolean status;
}
