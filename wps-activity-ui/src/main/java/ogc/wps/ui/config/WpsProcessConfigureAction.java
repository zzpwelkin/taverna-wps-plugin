package ogc.wps.ui.config;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;

import net.sf.taverna.t2.workbench.ui.actions.activity.ActivityConfigurationAction;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationDialog;


@SuppressWarnings("serial")
public class WpsProcessConfigureAction
		extends
		ActivityConfigurationAction<WpsProcessActivity,
        WpsProcessActivityConfigurationBean> {

	public WpsProcessConfigureAction(WpsProcessActivity activity, Frame owner) {
		super(activity);
	}

	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) {
		ActivityConfigurationDialog<WpsProcessActivity, WpsProcessActivityConfigurationBean> currentDialog = ActivityConfigurationAction
				.getDialog(getActivity());
		if (currentDialog != null) {
			currentDialog.toFront();
			return;
		}
		WpsProcessConfigurationPanel panel = new WpsProcessConfigurationPanel(
				getActivity());
		ActivityConfigurationDialog<WpsProcessActivity,
        WpsProcessActivityConfigurationBean> dialog = new ActivityConfigurationDialog<WpsProcessActivity, WpsProcessActivityConfigurationBean>(
				getActivity(), panel);

		ActivityConfigurationAction.setDialog(getActivity(), dialog);

	}

}
