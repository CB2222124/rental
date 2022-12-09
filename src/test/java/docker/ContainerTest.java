package docker;

//import org.apache.commons.lang3.SystemUtils;

//import java.io.IOException;

/**
 * Unused: Experimented trying to spin up the docker container automatically before conducting tests.
 * In future develop this further or consider Testcontainers for java, pgTAP etc.
 *
 * @author Callan
 */
public class ContainerTest {
    /*
    public void containerDown() throws IOException, InterruptedException {
        if (SystemUtils.IS_OS_WINDOWS) {
            new ProcessBuilder().command("cmd.exe", "/c", "docker compose down").start().waitFor();
        } else {
            new ProcessBuilder().command("bash", "-c", "docker compose down").start().waitFor();
        }
    }

    public void containerUp() throws IOException, InterruptedException {
        containerDown();
        if (SystemUtils.IS_OS_WINDOWS) {
            new ProcessBuilder().command("cmd.exe", "/c", "docker compose up -d").start().waitFor();
        } else {
            new ProcessBuilder().command("bash", "-c", "docker compose up -d").start().waitFor();
        }
    }
     */
}
