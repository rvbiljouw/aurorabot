package ms.aurora.loader.web;

import ms.aurora.browser.Browser;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;
import ms.aurora.transform.ClientDefinition;

import java.io.InputStream;

/**
 * @author rvbiljouw
 */
public class ClientDefinitionJSON implements ResponseHandler {
    private final Browser browser;
    private ClientDefinition definition;

    public ClientDefinitionJSON(Browser browser) {
        this.browser = browser;
    }

    @Override
    public void handleResponse(InputStream inputStream) {
        Plaintext plaintext = Plaintext.fromStream(inputStream);
        definition = new ClientDefinition(plaintext);
        definition.load();
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
