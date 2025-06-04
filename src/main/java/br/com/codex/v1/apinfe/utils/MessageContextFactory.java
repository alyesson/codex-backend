package br.com.codex.v1.apinfe.utils;

import org.apache.axis2.context.MessageContext;
import org.apache.axis2.transport.http.HTTPConstants;

import br.com.codex.v1.apinfe.DFConfig;

public enum MessageContextFactory {
    INSTANCE;

    public MessageContext create(DFConfig config) {
        final MessageContext messageContext = new MessageContext();
        messageContext.setProperty(HTTPConstants.SO_TIMEOUT, config.getTimeoutRequisicaoEmMillis());
        messageContext.setProperty(HTTPConstants.CONNECTION_TIMEOUT, config.getSoTimeoutEmMillis());
        return messageContext;
    }
}
