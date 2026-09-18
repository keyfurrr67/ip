package peter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for {@link Peter}, focused on the error/non-error distinction its
 * responses carry for the GUI to style differently.
 */
public class PeterTest {
    @TempDir
    private Path tempDir;

    @Test
    public void getResponse_validCommand_returnsNonErrorResponse() {
        Peter peter = new Peter(tempDir.resolve("data.txt").toString());

        Peter.Response response = peter.getResponse("todo read book");

        assertFalse(response.isError());
        assertTrue(response.text().contains("read book"));
    }

    @Test
    public void getResponse_unrecognisedCommand_returnsErrorResponse() {
        Peter peter = new Peter(tempDir.resolve("data.txt").toString());

        Peter.Response response = peter.getResponse("fly to the moon");

        assertTrue(response.isError());
    }

    @Test
    public void getResponse_missingArguments_returnsErrorResponse() {
        Peter peter = new Peter(tempDir.resolve("data.txt").toString());

        Peter.Response response = peter.getResponse("todo");

        assertTrue(response.isError());
    }

    @Test
    public void isLoadingError_freshDataFile_isFalse() {
        Peter peter = new Peter(tempDir.resolve("data.txt").toString());

        assertFalse(peter.isLoadingError());
    }
}
