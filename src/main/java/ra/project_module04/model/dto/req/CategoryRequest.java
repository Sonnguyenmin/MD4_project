package ra.project_module04.model.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class CategoryRequest {
    @NotBlank(message = "Tên danh mục không được để trống")
    private String categoryName;

    private String description;
    private Boolean status;
}
