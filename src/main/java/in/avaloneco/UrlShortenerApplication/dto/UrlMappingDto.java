package in.avaloneco.UrlShortenerApplication.dto;
import lombok.*;
import java.time.Instant;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UrlMappingDto {

    private String shortCode;

    private String originalUrl;

    private Instant createdAt;
}