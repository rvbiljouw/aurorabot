package ms.aurora.transform;

import java.util.List;

/**
 * @author Rick
 */
public final class ClassDefinition {
    private List<AccessorDefinition> accessors;
    private String name;
    private String iface;

    public List<AccessorDefinition> getAccessors() {
        return accessors;
    }

    public void setAccessors(List<AccessorDefinition> accessors) {
        this.accessors = accessors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIface() {
        return iface;
    }

    public void setIface(String iface) {
        this.iface = iface;
    }
}
