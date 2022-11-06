package integration.com.dmdev;

import com.dmdev.annotation.IT;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@IT
@TestPropertySource(locations = "classpath:/application-test.yaml")
@Sql(scripts = "classpath:/insert_data.sql")
public abstract class IntegrationBaseTest {

}