import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = LmsApplicationTests.TestConfig.class)   
class LmsApplicationTests {

    @Configuration
    static class TestConfig {
        // Define any configuration beans or settings needed for testing
    }
    @Test
    void contextLoads() {
        // Your test logic here
    }
}
