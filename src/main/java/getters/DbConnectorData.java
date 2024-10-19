package getters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DbConnectorData {
    private String url;
    private String user;
    private String pass;

}
