package ms.aurora.loader.web;

import ms.aurora.browser.Browser;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.exception.ParsingException;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;
import ms.aurora.transform.ClientDefinition;
import org.apache.log4j.Logger;

import java.io.InputStream;

/**
 * @author rvbiljouw
 */
public final class ClientDefinitionJSON implements ResponseHandler {
    private static Logger logger = Logger.getLogger(ClientDefinitionJSON.class);
    private final Browser browser;
    private ClientDefinition definition;

    public ClientDefinitionJSON(Browser browser) {
        this.browser = browser;
    }

    @Override
    public void handleResponse(InputStream inputStream) {
        try {
            Plaintext plaintext = Plaintext.fromStream(inputStream);
            definition = new ClientDefinition(plaintext);
            definition.load();
        } catch (ParsingException e) {
            logger.error("ClientDefinition couldn't be parsed.", e);
        }
    }

    public void visit() {
        GetRequest request = new GetRequest();
        request.setPage("/updater/output.json");
        browser.doRequest(request, this);
    }

    public ClientDefinition getDefinition() {
        return definition;
    }
}
