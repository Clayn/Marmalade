package net.bplaced.clayn.marmalade.io;

import java.nio.file.Path;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public final class StaticPaths
{

    public static String CONFIG_DIR = "config";

    private static boolean isWindows()
    {
        return System.getProperty("os.name", "").toLowerCase()
                .contains("windows");
    }

    public static final Path getConfigurationFile()
    {
        String path = System.getenv().getOrDefault("APPDATA",
                System.getProperty("user.home"));
        return Path.of(path, "Marmalade", "marmalade.properties");
    }
}
