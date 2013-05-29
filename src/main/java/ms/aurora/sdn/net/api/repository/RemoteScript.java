package ms.aurora.sdn.net.api.repository;

import ms.aurora.api.script.ScriptManifest;

import java.lang.annotation.Annotation;

/**
 * @author tobiewarburton
 */
public class RemoteScript extends Requestable implements ScriptManifest {
    private String name;
    private String author;

    public RemoteScript(long id, String name, String author) {
        super((byte) 1, id);
        this.name = name;
        this.author = author;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public String author() {
        return author;
    }

    @Override
    public String shortDescription() {
        return "Remote";
    }

    @Override
    public String longDescription() {
        return null;
    }

    @Override
    public double version() {
        return 0;
    }

    @Override
    public String category() {
        return null;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
