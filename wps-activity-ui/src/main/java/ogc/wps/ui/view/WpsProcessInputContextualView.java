package ogc.wps.ui.view;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;

import net.opengis.wps.x100.InputDescriptionType;
import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;

public class WpsProcessInputContextualView extends ContextualView {
	//private JTabbedPane tabDes = new JTabbedPane();
	String portName;
	List<InputDescriptionType> inputs;
	private JTextPane desPane = new JTextPane();
	private JLabel label = new JLabel();
	
	public WpsProcessInputContextualView(ActivityInputPort activityport, WpsProcessActivity activity) {
		this.inputs = activity.getConfiguration().getInputList();
		portName = activityport.getName();

		initView();
		
		refreshView();
	}

	@Override
	public JComponent getMainFrame() {
		JPanel jPanel = new JPanel( new GridLayout(1, 1));
		desPane.setBackground(new Color(150, 150, 150));
		desPane.setEditable(false);
		jPanel.add(desPane);
		return jPanel;
	}

	@Override
	public String getViewTitle() {
		return portName;
	}

	@Override
	public void refreshView() {
		desPane.setContentType("text/html");
		
		desPane.setText(getInputPortDesc(portName));
	}

	@Override
	public int getPreferredPosition() {
		// TODO Auto-generated method stub
		return 100;
	}
	
	private String getInputPortDesc(String port)
	{
		String desc = new String();
		for (InputDescriptionType input: inputs)
		{
			if (input.getIdentifier().getStringValue().equals(port))
			{
				desc = String.format("<html><body><p>Title:%1$2s</p>", input.getTitle().getStringValue());
				desc += String.format("<p>minOccurs = %1$2s<br/>maxOccurs = %2$2s</p>", 
						input.getMinOccurs().toString(), input.getMaxOccurs().toString());
				
				// TODO: 
				if (input.isSetLiteralData())
				{
				}
				else if (input.isSetComplexData())
				{
					
				}
					
				desc = desc + "</body></html>";
				break;
			}
		}
		return desc;
	}
	
//	private String getOutputPortDesc(String port)
//	{
//		String desc = new String();
//		for (OutputDescriptionType output: activityConf.getOutputlist())
//		{
//			if (output.getIdentifier().getStringValue().equals(port))
//			{
//				// TODO:
//				break;
//			}
//		}
//		return desc;
//	}

}
