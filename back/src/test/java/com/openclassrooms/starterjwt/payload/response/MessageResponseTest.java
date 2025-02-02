package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class MessageResponseTest {

    @Test
    void shouldSetMessageCorrectly() {
        // Arrange
        MessageResponse messageResponse = new MessageResponse("Initial message");

        // Act
        messageResponse.setMessage("Updated message");

        // Assert
        assertThat(messageResponse.getMessage()).isEqualTo("Updated message");
    }
}
