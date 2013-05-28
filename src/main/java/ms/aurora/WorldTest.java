package ms.aurora;

import ms.aurora.browser.Browser;
import ms.aurora.browser.Context;
import ms.aurora.browser.ContextBuilder;
import ms.aurora.browser.ResponseHandler;
import ms.aurora.browser.exception.ParsingException;
import ms.aurora.browser.impl.GetRequest;
import ms.aurora.browser.wrapper.Plaintext;
import ms.aurora.gui.Messages;

import java.io.InputStream;

/**
 * @author rvbiljouw
 */
public class WorldTest {

    public static void main(String[] args) {
        Context ctx = ContextBuilder.get().domain(Messages.getString("runescape.url")).build();
        Browser browser = new Browser(ctx);

        GetRequest req = new GetRequest();
        req.setPage("/slu");
        browser.doRequest(req, new ResponseHandler() {
            @Override
            public void handleResponse(InputStream inputStream) {
                try {
                    Plaintext plaintext = Plaintext.fromStream(inputStream);
                    System.out.println(plaintext.getText());
                } catch (ParsingException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        });
    }

}
