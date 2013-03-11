package ms.aurora.loader.web;

import ms.aurora.browser.Browser;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;

import java.io.InputStream;
import java.util.Map;
import java.util.regex.Matcher;

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
        plaintext.setText(plaintext.getText().replaceAll("param=", ""));
        Matcher codeBaseMatcher = plaintext.regex(KV_PAIR_REGEX);
        while (codeBaseMatcher.find()) {
            parameters.put(codeBaseMatcher.group(1),
                    codeBaseMatcher.group(2));
        }

        documentBase = parameters.get("codebase");
        archiveName = parameters.get("initial_jar");
        mainClass = parameters.get("initial_class").replace(".class", "");
        System.out.println("Parameters " + documentBase + " " + archiveName + " " + mainClass);
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

    private static final String KV_PAIR_REGEX = "(.*)=(.*)";
    private static final String URL = "/jav_config.ws";
}
