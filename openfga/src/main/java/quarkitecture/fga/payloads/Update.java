package quarkitecture.fga.payloads;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Update {
    String payload;
}
