package ogc.wps.ui.menu;

import javax.swing.Action;

import ogc.wps.WpsProcessActivity;
import ogc.wps.ui.config.WpsProcessConfigureAction;

import net.sf.taverna.t2.workbench.activitytools.AbstractConfigureActivityMenuAction;

public class WpsProcessConfigureMenuAction extends
		AbstractConfigureActivityMenuAction<WpsProcessActivity> {

	public WpsProcessConfigureMenuAction() {
		super(WpsProcessActivity.class);
	}

	@Override
	protected Action createAction() {
		Action result = null;
		result = new WpsProcessConfigureAction(findActivity(),
				getParentFrame());
		result.putValue(Action.NAME, "Configure OGC-WPS service");
		addMenuDots(result);
		return result;
	}

}
