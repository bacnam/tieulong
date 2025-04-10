

package org.apache.thrift.transport;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslException;
import javax.security.sasl.SaslServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TSaslServerTransport extends TSaslTransport {

  private static final Logger LOGGER = LoggerFactory.getLogger(TSaslServerTransport.class);

  private Map<String, TSaslServerDefinition> serverDefinitionMap = new HashMap<String, TSaslServerDefinition>();

  private static class TSaslServerDefinition {
    public String mechanism;
    public String protocol;
    public String serverName;
    public Map<String, String> props;
    public CallbackHandler cbh;

    public TSaslServerDefinition(String mechanism, String protocol, String serverName,
        Map<String, String> props, CallbackHandler cbh) {
      this.mechanism = mechanism;
      this.protocol = protocol;
      this.serverName = serverName;
      this.props = props;
      this.cbh = cbh;
    }
  }

  public TSaslServerTransport(TTransport transport) {
    super(transport);
  }

  public TSaslServerTransport(String mechanism, String protocol, String serverName,
      Map<String, String> props, CallbackHandler cbh, TTransport transport) {
    super(transport);
    addServerDefinition(mechanism, protocol, serverName, props, cbh);
  }

  private TSaslServerTransport(Map<String, TSaslServerDefinition> serverDefinitionMap, TTransport transport) {
    super(transport);
    this.serverDefinitionMap.putAll(serverDefinitionMap);
  }

  public void addServerDefinition(String mechanism, String protocol, String serverName,
      Map<String, String> props, CallbackHandler cbh) {
    serverDefinitionMap.put(mechanism, new TSaslServerDefinition(mechanism, protocol, serverName,
        props, cbh));
  }

  @Override
  protected SaslRole getRole() {
    return SaslRole.SERVER;
  }

  @Override
  protected void handleSaslStartMessage() throws TTransportException, SaslException {
    SaslResponse message = receiveSaslMessage();

    LOGGER.debug("Received start message with status {}", message.status);
    if (message.status != NegotiationStatus.START) {
      sendAndThrowMessage(NegotiationStatus.ERROR, "Expecting START status, received " + message.status);
    }

    String mechanismName = new String(message.payload);
    TSaslServerDefinition serverDefinition = serverDefinitionMap.get(mechanismName);
    LOGGER.debug("Received mechanism name '{}'", mechanismName);

    if (serverDefinition == null) {
      sendAndThrowMessage(NegotiationStatus.BAD, "Unsupported mechanism type " + mechanismName);
    }
    SaslServer saslServer = Sasl.createSaslServer(serverDefinition.mechanism,
        serverDefinition.protocol, serverDefinition.serverName, serverDefinition.props,
        serverDefinition.cbh);
    setSaslServer(saslServer);
  }

  public static class Factory extends TTransportFactory {

    private static Map<TTransport, TSaslServerTransport> transportMap =
      Collections.synchronizedMap(new WeakHashMap<TTransport, TSaslServerTransport>());

    private Map<String, TSaslServerDefinition> serverDefinitionMap = new HashMap<String, TSaslServerDefinition>();

    public Factory() {
      super();
    }

    public Factory(String mechanism, String protocol, String serverName,
        Map<String, String> props, CallbackHandler cbh) {
      super();
      addServerDefinition(mechanism, protocol, serverName, props, cbh);
    }

    public void addServerDefinition(String mechanism, String protocol, String serverName,
        Map<String, String> props, CallbackHandler cbh) {
      serverDefinitionMap.put(mechanism, new TSaslServerDefinition(mechanism, protocol, serverName,
          props, cbh));
    }

    @Override
    public TTransport getTransport(TTransport base) {
      TSaslServerTransport ret = transportMap.get(base);
      if (ret == null) {
        LOGGER.debug("transport map does not contain key", base);
        ret = new TSaslServerTransport(serverDefinitionMap, base);
        try {
          ret.open();
        } catch (TTransportException e) {
          LOGGER.debug("failed to open server transport", e);
          throw new RuntimeException(e);
        }
        transportMap.put(base, ret);
      } else {
        LOGGER.debug("transport map does contain key {}", base);
      }
      return ret;
    }
  }
}
