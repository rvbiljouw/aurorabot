package ms.aurora.browser.wrapper;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import javax.xml.xpath.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class HTML {
    private static Logger logger = Logger.getLogger(HTML.class);
    private Document dom;

    public HTML(Document dom) {
        this.dom = dom;
    }

    public Document getDOM() {
        return dom;
    }

    public List<Node> searchXPath(String expression) {
        List<Node> matchingElements = new ArrayList<Node>();
        try {
            XPathExpression expressionObj = getExpression(expression);
            NodeList resultingNodeList = (NodeList) expressionObj.evaluate(dom,
                    XPathConstants.NODESET);
            for (int index = 0; index < resultingNodeList.getLength(); index++) {
                matchingElements.add(resultingNodeList.item(index));
            }
        } catch (XPathExpressionException e) {
            logger.error("Incorrect XPath expression", e);
        }
        return matchingElements;
    }

    private XPathExpression getExpression(String expression) throws XPathExpressionException {
        XPath xpath = XPathFactory.newInstance().newXPath();
        return xpath.compile(expression);
    }

    public static HTML fromStream(InputStream stream) {
        try {
            /*
             * UGLY ASS W3C API IS UGLY
			 */
            Tidy tidy = new Tidy();
            tidy.setXHTML(true);
            Document dom = tidy.parseDOM(stream, System.out);
            dom.getDocumentElement().normalize();
            return new HTML(dom);
        } catch (Exception e) {
            logger.error("Failed to parse HTML properly", e);
        }
        return null;
    }

}
