package tech.ikora.socket.server.difference;

import org.junit.jupiter.api.Test;
import tech.ikora.socket.server.Helpers;

import java.io.IOException;
import java.net.URISyntaxException;

class StackTraceMapperTest {
    @Test
    void testMapStackTrace() throws IOException, URISyntaxException {
        final String newStackTrace = Helpers.readStringFromResourcesFile("stacktrace-1.txt");
        final String diff = Helpers.readStringFromResourcesFile("diff-1.txt");

        final String oldStackTrace = StackTraceMapper.map(newStackTrace, diff);
    }
}