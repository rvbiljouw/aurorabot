package ms.aurora.loader;

import ms.aurora.browser.Browser;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.exception.ParsingException;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Client configuration file
 *
 * @author rvbiljouw
 */
public class ClientConfig implements ResponseHandler {
    private final Map<String, String> params = new HashMap<String, String>();
    private final Browser browser;
    private String documentBase;
    private String archiveName;
    private String mainClass;

    public ClientConfig(Browser browser) {
        this.browser = browser;
    }

    public String getParam(String param) {
        return params.get(param);
    }

    public String getDocumentBase() {
        return documentBase;
    }

    public void setDocumentBase(String documentBase) {
        this.documentBase = documentBase;
    }

    public String getArchiveName() {
        return archiveName;
    }

    public void setArchiveName(String archiveName) {
        this.archiveName = archiveName;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    @Override
    public void handleResponse(InputStream inputStream) {
        try {
            Plaintext plaintext = Plaintext.fromStream(inputStream);
            plaintext.setText(normalize(plaintext.getText()));
            String[] lines = plaintext.getText().split("\n");
            for (String line : lines) {
                extractKVPairInto(params, line);
            }

            setMainClass(params.get("initial_class").replace(".class", ""));
            setDocumentBase(params.get("codebase"));
            setArchiveName(params.get("initial_jar"));
        } catch (ParsingException e) {
            e.printStackTrace();
        }
    }

    public void visit() {
        GetRequest request = new GetRequest();
        request.setPage(ClientConfig.CONFIG_URL);
        browser.doRequest(request, this);
    }

    private static String normalize(String string) {
        return string.replaceAll("param=", "")
                .replaceAll("msg=", "");
    }

    private static void extractKVPairInto(Map<String, String> into, String input) {
        int len = input.length();
        int idx = input.indexOf('=');
        into.put(input.substring(0, idx),
                input.substring(idx + 1, len));
    }

    private static final String CONFIG_URL = "/jav_config.ws";
}
