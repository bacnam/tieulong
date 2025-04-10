

package org.apache.thrift.transport;

import java.util.Map;

import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import javax.security.sasl.SaslClient;
import javax.security.sasl.SaslException;

import org.apache.thrift.EncodingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TSaslClientTransport extends TSaslTransport {

  private static final Logger LOGGER = LoggerFactory.getLogger(TSaslClientTransport.class);

  private final String mechanism;

  public TSaslClientTransport(SaslClient saslClient, TTransport transport) {
    super(saslClient, transport);
    mechanism = saslClient.getMechanismName();
  }

  public TSaslClientTransport(String mechanism, String authorizationId, String protocol,
      String serverName, Map<String, String> props, CallbackHandler cbh, TTransport transport)
      throws SaslException {
    super(Sasl.createSaslClient(new String[] { mechanism }, authorizationId, protocol, serverName,
        props, cbh), transport);
    this.mechanism = mechanism;
  }

  @Override
  protected SaslRole getRole() {
    return SaslRole.CLIENT;
  }

  @Override
  protected void handleSaslStartMessage() throws TTransportException, SaslException {
    SaslClient saslClient = getSaslClient();

    byte[] initialResponse = new byte[0];
    if (saslClient.hasInitialResponse())
      initialResponse = saslClient.evaluateChallenge(initialResponse);

    LOGGER.debug("Sending mechanism name {} and initial response of length {}", mechanism,
        initialResponse.length);

    byte[] mechanismBytes = mechanism.getBytes();
    sendSaslMessage(NegotiationStatus.START,
                    mechanismBytes);

    sendSaslMessage(saslClient.isComplete() ? NegotiationStatus.COMPLETE : NegotiationStatus.OK,
                    initialResponse);
    underlyingTransport.flush();
  }
}
