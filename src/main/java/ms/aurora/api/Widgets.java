package ms.aurora.api;

import com.google.common.base.Function;
import ms.aurora.api.rt3.Widget;
import ms.aurora.api.wrappers.RSWidget;

import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;
import static ms.aurora.api.ClientContext.context;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 13/03/2013
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public class Widgets {

    public static RSWidget getWidget(int parent, int child) {
        Widget[][] cache = context.get().getClient().getWidgetCache();
        if (cache[parent][child] != null)
            return new RSWidget(context.get(), cache[parent][child]);
        return null;
    }

    public static RSWidget[] getWidgets(int parent) {
        Widget[][] cache = context.get().getClient().getWidgetCache();
        return transform(newArrayList(cache[parent]), transform).toArray(new RSWidget[0]);
    }

    private static final Function<Widget, RSWidget> transform = new Function<Widget, RSWidget>() {
        @Override
        public RSWidget apply(Widget widget) {
            return new RSWidget(context.get(), widget);
        }
    };

}
