package ogc.wps.ui.view;

import java.util.Arrays;
import java.util.List;

import ogc.wps.WpsProcessActivity;
import net.sf.taverna.t2.workbench.file.FileManager;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ContextualViewFactory;
import net.sf.taverna.t2.workflowmodel.Dataflow;
import net.sf.taverna.t2.workflowmodel.Processor;
import net.sf.taverna.t2.workflowmodel.processor.activity.Activity;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.utils.Tools;

public class WpsProcessActivityInputContextViewFactory implements
	ContextualViewFactory<ActivityInputPort> {
	private WpsProcessActivity activity;
	
	public boolean canHandle(Object selection) {
		if(!(selection instanceof ActivityInputPort)){
			return false;
		}
		return (hasContainingWpsProcessActivity(selection));
	}

	public List<ContextualView> getViews(ActivityInputPort selection) {
		return Arrays.<ContextualView>asList(new WpsProcessInputContextualView(selection, activity ));
	}
	
	private boolean hasContainingWpsProcessActivity(Object selection)
	{
		Processor p = null;
		Dataflow d = FileManager.getInstance().getCurrentDataflow();
		if (selection instanceof ActivityInputPort) {
			p = Tools.getFirstProcessorWithActivityInputPort(d, (ActivityInputPort) selection);
		}

		Activity a = p.getActivityList().get(0);
		
		if ( a instanceof WpsProcessActivity)
		{
			activity = (WpsProcessActivity)a;
			return true;
		}
		else{
			return false;
		}
	}
}
