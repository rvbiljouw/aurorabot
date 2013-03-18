package ms.aurora.loader.web;

import ms.aurora.browser.Browser;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;

import java.io.InputStream;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author rvbiljouw
 */
public class ClientConfig implements ResponseHandler {
    private final Browser browser;
    private String documentBase;
    private String archiveName;
    private String mainClass;

    private Map<String, String> parameters = newHashMap();

    public ClientConfig(Browser browser) {
        this.browser = browser;
    }

    @Override
    public void handleResponse(InputStream inputStream) {
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
