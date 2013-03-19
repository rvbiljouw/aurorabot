package ms.aurora.browser.wrapper;

import ms.aurora.browser.exception.ParsingException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Plaintext {
    private String text;

    public Plaintext(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Matcher regex(String expression) {
        Pattern pattern = Pattern.compile(expression);
        return  pattern.matcher(text);
    }

    public static Plaintext fromStream(InputStream stream) throws ParsingException {
        try {
            BufferedReader streamReader = new BufferedReader(
                    new InputStreamReader(stream));
            String text = "";
            String buffer = "";
            while ((buffer = streamReader.readLine()) != null) {
                text += new StringBuilder().append(buffer).append("\n").toString();
            }
            return new Plaintext(text);
        } catch (Exception e) {
            throw new ParsingException("Failed to parse plaintext document.", e);
        }
    }
}
