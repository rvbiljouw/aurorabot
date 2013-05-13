package ms.aurora.loader.web;

import ms.aurora.Messages;
import ms.aurora.browser.Browser;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.exception.ParsingException;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ResponseHandler for the client configuration page.
 * Our loader utilizes the same page as Jagex's RuneScape loader does.
 * @author Rick
 */
public final class ClientConfig implements ResponseHandler {
    private static Logger logger = Logger.getLogger(ClientConfig.class);
    private final Browser browser;
    private String documentBase;
    private String archiveName;
    private String mainClass;

    private Map<String, String> parameters = new HashMap<String, String>();

    public ClientConfig(Browser browser) {
        this.browser = browser;
    }

    @Override
    public void handleResponse(InputStream inputStream) {
        try {
            Plaintext plaintext = Plaintext.fromStream(inputStream);
            plaintext.setText(plaintext.getText().replaceAll("param=", "").replaceAll("msg=", ""));
            String[] lines = plaintext.getText().split("\n");
            for (String line : lines) {
                String key = line.substring(0, line.indexOf('='));
                String value = line.substring(line.indexOf('=') + 1, line.length());
                parameters.put(key, value);
            }

            documentBase = parameters.get("codebase");
            archiveName = parameters.get("initial_jar");
            mainClass = parameters.get("initial_class").replace(".class", "");
        } catch (ParsingException e) {
            logger.error(Messages.getString("cconfig.parsingFailed"), e);
        }
    }

    public void visit() {
        GetRequest request = new GetRequest();
        request.setPage(URL);
        browser.doRequest(request, this);
    }

    public String getDocumentBase() {
        return documentBase;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public String getMainClass() {
        return mainClass;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    private static final String URL = "/jav_config.ws";
}
