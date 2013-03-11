package ms.aurora.browser.wrapper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Plaintext {
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
        Matcher matcher = pattern.matcher(text);
        return matcher;
    }

    public static Plaintext fromStream(InputStream stream) {
        try {
            BufferedReader streamReader = new BufferedReader(
                    new InputStreamReader(stream));
            String text = "";
            String buffer = "";
            while ((buffer = streamReader.readLine()) != null) {
                text += buffer + "\n";
            }
            return new Plaintext(text);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse plaintext document.", e);
        }
    }
}
