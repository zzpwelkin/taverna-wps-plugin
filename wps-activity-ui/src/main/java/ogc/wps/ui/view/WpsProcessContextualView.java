package ogc.wps.ui.view;

import java.awt.Frame;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;
import ogc.wps.ui.config.WpsProcessConfigureAction;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.ContextualView;


@SuppressWarnings("serial")
public class WpsProcessContextualView extends ContextualView {
	private final WpsProcessActivity activity;
	//private JTabbedPane tabDes = new JTabbedPane();
	private JTextPane titlePane = new JTextPane();
	private JTextPane abstractPane = new JTextPane();

	public WpsProcessContextualView(WpsProcessActivity activity) {
		this.activity = activity;
		initView();
	}

	@Override
	public JComponent getMainFrame() {
		JPanel jPanel = new JPanel(new GridLayout(2,2));
		
		// title label
		JLabel titleLabel= new JLabel("title");
		titleLabel.setBorder(BorderFactory.createEtchedBorder());
		titleLabel.setText("Title:");
		titleLabel.setSize(50, 200);
		
		// abstract label
		JLabel absLabel= new JLabel("abstract");
		absLabel.setBorder(BorderFactory.createEtchedBorder());
		absLabel.setText("Abstract:");
		absLabel.setSize(50, 200);
		
		// title
		titlePane.setBorder(BorderFactory.createRaisedBevelBorder());
		
		// abstract
		abstractPane.addHyperlinkListener(new HyperlinkListener() {
			
			@Override
			public void hyperlinkUpdate(HyperlinkEvent arg0) {
				if (arg0.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					try{
				    String command = "firefox " + arg0.getURL().toString();
				    Runtime.getRuntime().exec(command);
					}
					catch(IOException e){
						// TODO: Add the messagebox
					}
				}
			}
		});
				
		titlePane.setEditable(false);
		abstractPane.setEditable(false);
		jPanel.add(titleLabel);
		jPanel.add(titlePane);
		jPanel.add(absLabel);
		jPanel.add(abstractPane);
		refreshView();
		return jPanel;
	}
	@Override
	public String getViewTitle() {
		WpsProcessActivityConfigurationBean configuration = activity
				.getConfiguration();
		return configuration.getWpsUri().toString() + "-" + 
				configuration.getProcessBrief().getIdentifier().getStringValue();
	}

	/**
	 * Typically called when the activity configuration has changed.
	 */
	@Override
	public void refreshView() {
		WpsProcessActivityConfigurationBean configuration = activity
				.getConfiguration();
		
		titlePane.setText(configuration.getProcessBrief().getTitle().getStringValue());
		
		String abst = configuration.getProcessBrief().getAbstract().getStringValue();
		if (abst.startsWith("http://"))
		{
			abstractPane.setContentType("text/html");
			abstractPane.setText(String.format("<html><body><a href=%1$2s>%1$2s</a></body></html>", abst));
		}
		else
		{
			abstractPane.setText(abst);
		}
	}

	/**
	 * View position hint
	 */
	@Override
	public int getPreferredPosition() {
		// We want to be on top
		return 100;
	}
	
	@Override
	public Action getConfigureAction(final Frame owner) {
		return new WpsProcessConfigureAction(activity, owner);
	}

}


///**
// * create ows brief component
// * @param title The title of identifier
// * @param abst Abstract description
// * @return
// */
//private JPanel GetOWSBrief(String title, String abst)
//{
//	JPanel processBrief = new JPanel();
//	
//	// title
//	JLabel labelTitle = new JLabel("Title:" + title);
//	
//	// abstract
//	JTextPane textAbstr = new JTextPane();
//	textAbstr.setEditable(false);
//	if (abst.startsWith("http://"))
//	{
//		textAbstr.setContentType("text/html");
//		textAbstr.setText(String.format("<html><body><a href=%1$2s>%1$2s</a></body></html>", abst));
//	}
//	else
//	{
//		textAbstr.setText(abst);
//	}
//	
//	processBrief.add(labelTitle);
//	processBrief.add(textAbstr);
//	return processBrief;
//}


//private JComponent getProcessBriefTab() {	
//	return GetOWSBrief(activity.getConfiguration().getProcessBrief().getTitle().getStringValue(),
//			activity.getConfiguration().getProcessBrief().getAbstract().getStringValue());
//}
//
//private JComponent getInputsTab() {
//	JTree inputs = new JTree();
//	
//	for(InputDescriptionType input: activity.getConfiguration().getInputList()){
//		JPanel brief = GetOWSBrief(input.getTitle().getStringValue(), input.getAbstract().getStringValue());
//		
//		if (input.isSetLiteralData())
//		{
//		}
//		else if(input.isSetComplexData())
//		{
//		}
//		inputs.add(input.getIdentifier().getStringValue(), brief);
//	}
//	
//	return inputs;
//}
//
//private JComponent getOutputsTab() {
//	JComponent outputs = new JTree();
//	return outputs;
//}
