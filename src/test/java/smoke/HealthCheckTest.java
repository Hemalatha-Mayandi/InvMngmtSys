package smoke;

import org.example.domain.Status;
import org.example.utils.HealthCheckUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HealthCheckTest {

    private final HealthCheckUtils healthCheckUtils = new HealthCheckUtils();

    @Test
    public void checkDbStatus(){
        Status dbStatus = healthCheckUtils.getDbStatus();
        assertThat(dbStatus.getDbStatus(), equalTo("Connected"));
        assertThat(dbStatus.getStatus(), equalTo("OK"));
    }
}
