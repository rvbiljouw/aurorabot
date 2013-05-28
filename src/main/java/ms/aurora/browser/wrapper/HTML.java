package ms.aurora.browser.wrapper;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A parser / access layer for HTML pages
 * @author Rick
 */
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

    public List<Node> searchXPath(Node base, String expression) {
        List<Node> matchingElements = new ArrayList<Node>();
        try {
            XPathExpression expressionObj = getExpression(expression);
            NodeList resultingNodeList = (NodeList) expressionObj.evaluate(base,
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
            Document dom = tidy.parseDOM(stream, null);
            dom.getDocumentElement().normalize();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(dom);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());
            return new HTML(db.parse(is));
        } catch (Exception e) {
            logger.error("Failed to parse HTML properly", e);
        }
        return null;
    }

}
