package it.elis.sicilianaturalmente.exception;

import lombok.*;
import lombok.experimental.Accessors;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class ErrorResponse {
    private String timestamp;
    private Integer status;
    private String error;
    private String path;
    private String message;
}
