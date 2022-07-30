package top.imzdx.renxingpush.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TelegramAuthenticationRequest {
    @NotNull
    private Long auth_date;

    private String first_name;

    @NotEmpty
    private Long id;

    private String last_name;

    private String photo_url;

    private String username;

    @NotEmpty
    private String hash;
}
