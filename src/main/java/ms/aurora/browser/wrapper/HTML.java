package ms.aurora.browser.wrapper;

import com.google.common.collect.Lists;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import javax.xml.xpath.*;
import java.io.InputStream;
import java.util.List;

public class HTML {
    private Document dom;

    public HTML(Document dom) {
        this.dom = dom;
    }

    public Document getDOM() {
        return dom;
    }

    public List<Node> searchXPath(String expression) {
        List<Node> matchingElements = Lists.newArrayList();
        XPathExpression expressionObj = getExpression(expression);
        try {
            NodeList resultingNodeList = (NodeList) expressionObj.evaluate(dom,
                    XPathConstants.NODESET);
            for (int index = 0; index < resultingNodeList.getLength(); index++) {
                matchingElements.add(resultingNodeList.item(index));
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Failed to evaluate expression "
                    + expression, e);
        }
        return matchingElements;
    }

    private XPathExpression getExpression(String expression) {
        XPath xpath = XPathFactory.newInstance().newXPath();
        try {
            XPathExpression expr = xpath.compile(expression);
            return expr;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(
                    "Problem with XPath query " + expression, e);
        }
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
            throw new RuntimeException(
                    "Failed to parse Html content from stream.", e);
        }
    }

}
