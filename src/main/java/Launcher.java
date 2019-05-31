import game.Game;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.nio.file.Paths;

public class Launcher {
    public static void main(String[] args) {
        Game.init(getArguments(args));
        Game.startProxy();
    }

    /**
     * Parse commandline arguments.
     */
    private static Namespace getArguments(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("teleport-exploit.jar").build()
            .defaultHelp(true)
            .description("Log teleporting players to the console.");
        parser.addArgument("-s", "--server")
            .required(true)
            .help("The address of the remote server to connect to. Hostname or IP address (without port).");
        parser.addArgument("-p", "--port").type(Integer.class)
            .setDefault(25565)
            .help("The port of the remote server.")
            .textualName();
        parser.addArgument("-l", "--local-port").type(Integer.class)
            .setDefault(25565)
            .help("The local port which the client has to connect to.")
            .dest("local-port");
        parser.addArgument("-m", "--minecraft")
            .setDefault(Paths.get("%appdata%", ".minecraft").toString())
            .help("Path to your Minecraft installation, used to authenticate with Mojang servers.");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException ex) {
            parser.handleError(ex);
            System.exit(1);
        }
        return ns;
    }
}
