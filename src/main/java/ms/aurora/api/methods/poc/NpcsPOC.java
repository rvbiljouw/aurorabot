package ms.aurora.api.methods.poc;

import ms.aurora.api.methods.poc.query.impl.NpcQuery;

/**
 * Date: 23/05/13
 * Time: 10:39
 *
 * @author A_C/Cov
 */
public class NpcsPOC {

    public static NpcQuery get() {
        return new NpcQuery();
    }

}
