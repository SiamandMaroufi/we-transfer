package we.transfer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ApplicationTest {

    @Test
    void healthTest() {
        assertDoesNotThrow(() -> Application.main(new String[]{}));
    }
}