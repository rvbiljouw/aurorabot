package ms.aurora.core.impl;

import ms.aurora.core.Session;
import ms.aurora.core.SessionVisitor;

/**
 * Responsible for executing plugin state updates.
 * @author rvbiljouw
 */
public class PluginRefreshVisitor implements SessionVisitor {

    @Override
    public void visit(Session session) {
        session.getPluginManager().refresh();
    }

}
