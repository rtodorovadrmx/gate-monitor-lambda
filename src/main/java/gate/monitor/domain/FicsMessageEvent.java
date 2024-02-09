package gate.monitor.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class FicsMessageEvent {

    @NotBlank
    private String messageId;
    @NotBlank
    private String flight;
    @NotBlank
    @Size(max = 3)
    private String gate;
}
