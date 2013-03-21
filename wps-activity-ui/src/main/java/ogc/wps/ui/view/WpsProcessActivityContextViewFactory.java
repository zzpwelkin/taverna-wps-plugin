package ogc.wps.ui.view;

import java.util.Arrays;
import java.util.List;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;

import ogc.wps.WpsProcessActivity;

public class WpsProcessActivityContextViewFactory implements
		ContextualViewFactory<WpsProcessActivity> {

	public boolean canHandle(Object selection) {
		return selection instanceof WpsProcessActivity;
	}

	public List<ContextualView> getViews(WpsProcessActivity selection) {
		return Arrays.<ContextualView>asList(new WpsProcessContextualView(selection));
	}
}
