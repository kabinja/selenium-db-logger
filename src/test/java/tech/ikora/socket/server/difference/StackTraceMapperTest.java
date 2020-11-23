package tech.ikora.socket.server.difference;

import org.junit.jupiter.api.Test;
import tech.ikora.socket.server.Helpers;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class StackTraceMapperTest {
    @Test
    void testMapStackTraceWithDiff() throws IOException, URISyntaxException {
        final String newStackTrace = Helpers.readStringFromResourcesFile("stacktrace-1.txt");
        final String diff = Helpers.readStringFromResourcesFile("diff-1.txt");

        final String oldStackTrace = StackTraceMapper.map(newStackTrace, diff);

        assertTrue(oldStackTrace.contains("com.example.demo.integration.StudentStepsDefinitions:verify_redurect_to_student__listpage:49"));
    }

    @Test
    void testMapStackTraceWithNoDiff() throws IOException, URISyntaxException {
        final String newStackTrace = Helpers.readStringFromResourcesFile("stacktrace-1.txt");
        final String diff = "";

        final String oldStackTrace = StackTraceMapper.map(newStackTrace, diff);

        assertEquals(newStackTrace, oldStackTrace);
    }
}