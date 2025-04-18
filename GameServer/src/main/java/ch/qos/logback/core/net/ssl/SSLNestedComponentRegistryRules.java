package ch.qos.logback.core.net.ssl;

import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;

public class SSLNestedComponentRegistryRules
{
public static void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {
registry.add(SSLComponent.class, "ssl", SSLConfiguration.class);
registry.add(SSLConfiguration.class, "parameters", SSLParametersConfiguration.class);

registry.add(SSLConfiguration.class, "keyStore", KeyStoreFactoryBean.class);

registry.add(SSLConfiguration.class, "trustStore", KeyStoreFactoryBean.class);

registry.add(SSLConfiguration.class, "keyManagerFactory", KeyManagerFactoryFactoryBean.class);

registry.add(SSLConfiguration.class, "trustManagerFactory", TrustManagerFactoryFactoryBean.class);

registry.add(SSLConfiguration.class, "secureRandom", SecureRandomFactoryBean.class);
}
}

