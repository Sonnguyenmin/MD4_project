package ra.project_module04.model.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ra.project_module04.constants.EHttpStatus;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ResponseWrapper<T> {
    EHttpStatus eHttpStatus;
    int statusCode;
    T data;
}
