package ogc.wps.ui.config;

import java.awt.GridLayout;
import javax.swing.JCheckBox;

import ogc.wps.WpsProcessActivity;
import ogc.wps.WpsProcessActivityConfigurationBean;

import net.sf.taverna.t2.workbench.ui.views.contextualviews.activity.ActivityConfigurationPanel;



@SuppressWarnings("serial")
public class WpsProcessConfigurationPanel
		extends
		ActivityConfigurationPanel<WpsProcessActivity, 
        WpsProcessActivityConfigurationBean> {

	private WpsProcessActivity activity;
	private WpsProcessActivityConfigurationBean configBean;
	private JCheckBox checkReference;

	public WpsProcessConfigurationPanel(WpsProcessActivity activity) {
		this.activity = activity;
		initGui();
	}

	protected void initGui() {
		removeAll();
		setLayout(new GridLayout(0, 2));

		// FIXME: Create GUI depending on activity configuration bean
		if (this.activity.getConfiguration().isStoreSupported())
		{
			checkReference = new JCheckBox("Reference Respond",true);
			add(checkReference);
		}
		// Populate fields from activity configuration bean
		refreshConfiguration();
	}

	/**
	 * Check that user values in UI are valid
	 */
	@Override
	public boolean checkValues() {
//		try {
//			URI.create(fieldURI.getText());
//		} catch (IllegalArgumentException ex) {
//			JOptionPane.showMessageDialog(this, ex.getCause().getMessage(),
//					"Invalid URI", JOptionPane.ERROR_MESSAGE);
//			// Not valid, return false
//			return false;
//		}
		// All valid, return true
		return true;
	}

	/**
	 * Return configuration bean generated from user interface last time
	 * noteConfiguration() was called.
	 */
	@Override
	public WpsProcessActivityConfigurationBean getConfiguration() {
		// Should already have been made by noteConfiguration()
		return configBean;
	}

	/**
	 * Check if the user has changed the configuration from the original
	 */
	@Override
	public boolean isConfigurationChanged() {
		// FIXME: Init bean fields from your active configureBean
		return ( configBean.isReferenceRespond()!= checkReference.isSelected() )?true:false;
	}

	/**
	 * Prepare a new configuration bean from the UI, to be returned with
	 * getConfiguration()
	 */
	@Override
	public void noteConfiguration() {
		//configBean = new ExampleActivityConfigurationBean();
		
		// FIXME: Update bean fields from your UI elements
		configBean.setReferenceRespond(checkReference.isSelected());
	}

	/**
	 * Update GUI from a changed configuration bean (perhaps by undo/redo).
	 * 
	 */
	@Override
	public void refreshConfiguration() {
		configBean = activity.getConfiguration();
		
		// FIXME: Update UI elements from your bean fields
		checkReference.setSelected(configBean.isReferenceRespond());
	}
}
