package ms.aurora.clientsim;

import ms.aurora.api.Context;
import ms.aurora.api.event.MessageEvent;

/**
 * @author rvbiljouw
 */
public class as {


    static final void dk(int var0, String var1, String var2, String var3, byte var4) {
        Context.getEventBus().submit(new MessageEvent(var1 + " " + var2 + " " + var3));
        for(int var5 = 99; var5 > 0; --var5) {
            client.lu[var5] = client.lu[var5 - 1];
            client.lx[var5] = client.lx[var5 - 1];
            client.ly[var5] = client.ly[var5 - 1];
            client.lo[var5] = client.lo[var5 - 1];
        }

        client.lu[0] = var0;
        client.lx[0] = var1;
        client.ly[0] = var2;
        client.lo[0] = var3;
        client.lr += 2094119005;
        client.ky = client.kj;
    }

}
