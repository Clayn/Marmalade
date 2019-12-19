package net.bplaced.clayn.marmalade.conf;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.bplaced.clayn.marmalade.io.StaticPaths;
import org.apache.commons.configuration2.JSONConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;

/**
 *
 * @author Clayn <clayn_osmato@gmx.de>
 * @since 0.1
 */
public class Configurator
{

    private static final JSONConfiguration getDefaultConfiguration()
    {
        JSONConfiguration config = new JSONConfiguration();
        return config;
    }

    public static void saveConfiguration(JSONConfiguration conf)
    {
        if (conf == null)
        {
            return;
        }
        try (Writer writer = Files.newBufferedWriter(
                StaticPaths.getConfigurationFile(), StandardOpenOption.WRITE,
                StandardOpenOption.TRUNCATE_EXISTING))
        {
            conf.write(writer);
        } catch (IOException | ConfigurationException ex)
        {
            ex.printStackTrace();
        }
    }

    public static JSONConfiguration getConfiguration()
    {
        Path confFile = StaticPaths.getConfigurationFile();
        JSONConfiguration config = null;
        if (!Files.exists(confFile))
        {
            config = getDefaultConfiguration();
            saveConfiguration(config);
            return config;
        }
        JSONConfiguration jConf = new JSONConfiguration();
        try (InputStream in = Files.newInputStream(confFile))
        {
            jConf.read(in);
            config = jConf;
        } catch (IOException | ConfigurationException ex)
        {
            throw new RuntimeException(ex);
        }
        return config;
    }
}
