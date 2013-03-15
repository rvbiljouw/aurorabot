package ms.aurora.transform;

import flexjson.JSONDeserializer;
import ms.aurora.browser.wrapper.Plaintext;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

/**
 * @author rvbiljouw
 */
public class ClientDefinition {
    private final Logger logger = Logger.getLogger(ClientDefinition.class);
    private final List<ClassDefinition> classes = newArrayList();
    private final Plaintext source;

    public ClientDefinition(Plaintext source) {
        this.source = source;
    }

    public void load() {
        JSONDeserializer<List<ClassDefinition>> classDeserializer =
                new JSONDeserializer<List<ClassDefinition>>();
        classes.addAll(classDeserializer.deserialize(source.getText()));
        repairInterfaceNames();
        repairAccessorReturns();

        logger.debug("Loaded " + classes.size() + " definitions.");
        for (ClassDefinition definition : classes) {
            logger.debug(definition.getName() + " -> " + definition.getIface());
            for (AccessorDefinition acc : definition.getAccessors()) {
                logger.debug("\t" + acc.getName() + " returns " + acc.getOwner() + "." + acc.getField());
                logger.debug("\t\tStatic? " + acc.isStatik());
                logger.debug("\t\tMultiplier " + acc.getMultiplier());
            }
        }
    }

    private void repairInterfaceNames() {
        // Obtain all the names first
        for (ClassDefinition clazz : classes) {
            clazz.setIface(INTERFACE_PREFIX + clazz.getIface());
        }
    }

    private void repairAccessorReturns() {
        Map<String, String> interfaceClassMap = newHashMap();
        // Obtain all the names first
        for (ClassDefinition clazz : classes) {
            interfaceClassMap.put(clazz.getName(), clazz.getIface());
        }

        // Change all the signatures into the right ones.
        for (ClassDefinition clazz : classes) {
            for (AccessorDefinition accessor : clazz.getAccessors()) {
                String sig = accessor.getSignature().replaceAll("\\[", "")
                        .replaceAll("L", "").replaceAll(";", "");
                if (interfaceClassMap.containsKey(sig)) {
                    String substitution = interfaceClassMap.get(sig);
                    accessor.setReturnSignature(accessor.getSignature().replace(sig, substitution));
                    logger.debug("Substituted " + sig + " with " + substitution);
                } else {
                    accessor.setReturnSignature(accessor.getSignature());
                }
            }
        }
    }

    public ClassDefinition lookup(String name) {
        for (ClassDefinition clazz : classes) {
            if (clazz.getName().equals(name)) {
                return clazz;
            }
        }
        return null;
    }

    private static final String INTERFACE_PREFIX = "ms/aurora/api/rt3/";
}
