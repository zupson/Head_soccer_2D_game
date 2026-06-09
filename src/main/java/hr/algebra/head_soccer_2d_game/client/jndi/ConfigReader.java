package hr.algebra.head_soccer_2d_game.client.jndi;

import lombok.experimental.UtilityClass;

import javax.naming.Context;
import javax.naming.NamingException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

@UtilityClass
public class ConfigReader {
    private static final Properties props;

    static {
        props = loadProperties();
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        Hashtable<String, String> config = buildJndiConfig();

        try (InitialDirContextCloseable context = new InitialDirContextCloseable(config)) {
            Object configObject = context.lookup("app.conf");
            properties.load(new FileReader(configObject.toString()));
        } catch (NamingException | IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    private static Hashtable<String, String> buildJndiConfig() {
        Hashtable<String, String> config = new Hashtable<>();
        config.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        config.put(Context.PROVIDER_URL, "file:src/main/resources");
        return config;
    }

    public static String getStringValue(ConfigKey key) {
        return (String) props.get(key.getKey());
    }

    public static Integer getIntegerValueForKey(ConfigKey key) {
        return Integer.valueOf((String) props.get(key.getKey()));
    }
}