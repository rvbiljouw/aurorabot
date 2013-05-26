package ms.aurora.sdn.net.api.repository;

import ms.aurora.api.plugin.PluginManifest;

import java.lang.annotation.Annotation;

/**
 * @author tobiewarburton
 */
public class RemotePlugin extends Requestable implements PluginManifest {
    private String name;
    private String author;

    public RemotePlugin(long id, String name, String author) {
        super((byte) 0, id);
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
        return null;
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
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
