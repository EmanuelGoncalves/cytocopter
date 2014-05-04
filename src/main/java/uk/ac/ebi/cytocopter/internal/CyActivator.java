package uk.ac.ebi.cytocopter.internal;

import java.io.InputStream;
import java.util.Properties;

import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.service.util.CyServiceRegistrar;
import org.cytoscape.task.read.LoadVizmapFileTaskFactory;
import org.cytoscape.work.ServiceProperties;
import org.cytoscape.work.TaskFactory;
import org.osgi.framework.BundleContext;

import uk.ac.ebi.cytocopter.internal.cellnoptr.enums.CytocopterCommandsEnum;
import uk.ac.ebi.cytocopter.internal.cellnoptr.tasks.ConfigureCellnoptrTaskFactory;
import uk.ac.ebi.cytocopter.internal.cellnoptr.tasks.OptimiseTaskFactory;
import uk.ac.ebi.cytocopter.internal.cellnoptr.tasks.PreprocessTaskFactory;
import uk.ac.ebi.cytocopter.internal.cellnoptr.tasks.SetNodeTypeTaskFactory;
import uk.ac.ebi.cytocopter.internal.ui.CytocopterControlPanel;
import uk.ac.ebi.cytocopter.internal.ui.CytocopterResultsPanel;

public class CyActivator extends AbstractCyActivator {

	public BundleContext bundleContext;
	public CyServiceRegistrar cyServiceRegistrar;
	
	public static final String visualStyleFile = "/CytocopterVisualStyle.xml";
	public static final String visualStyleName = "Cytocopter";
	
	
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		this.bundleContext = bundleContext;
		cyServiceRegistrar = getService(bundleContext, CyServiceRegistrar.class);
		
		CytocopterMenuAction action = new CytocopterMenuAction(cyServiceRegistrar, "Cytocopter");
		registerAllServices(bundleContext, action, new Properties());

		registerPanels();
		registerCytocopterCommands();
		loadVisualStyle();
	}
		
	private void registerPanels () {
		registerService(bundleContext, new CytocopterControlPanel(cyServiceRegistrar), CytoPanelComponent.class, new Properties());
		registerService(bundleContext, new CytocopterResultsPanel(cyServiceRegistrar), CytoPanelComponent.class, new Properties());
	}
	
	private void registerCytocopterCommands () {
		Properties props = new Properties();
		props.setProperty(ServiceProperties.COMMAND_NAMESPACE, CytocopterCommandsEnum.CYTOCOPTER_NAME_SPACE);
		
		props.setProperty(ServiceProperties.COMMAND, CytocopterCommandsEnum.CONFIGURE.getName());
		registerService(bundleContext, new ConfigureCellnoptrTaskFactory(cyServiceRegistrar, false), TaskFactory.class, props);
		
		props.setProperty(ServiceProperties.COMMAND, CytocopterCommandsEnum.PREPROCESS.getName());
		registerService(bundleContext, new PreprocessTaskFactory(cyServiceRegistrar, false, false), TaskFactory.class, props);
		
		props.setProperty(ServiceProperties.COMMAND, CytocopterCommandsEnum.OPTIMISE.getName());
		registerService(bundleContext, new OptimiseTaskFactory(cyServiceRegistrar, false), TaskFactory.class, props);
		
		props.setProperty(ServiceProperties.COMMAND, CytocopterCommandsEnum.NODETYPE.getName());
		registerService(bundleContext, new SetNodeTypeTaskFactory(cyServiceRegistrar), TaskFactory.class, props);
	}

	private void loadVisualStyle () {
		InputStream in = getClass().getResourceAsStream(visualStyleFile);
		LoadVizmapFileTaskFactory loadVizmapFileTaskFactory =  cyServiceRegistrar.getService(LoadVizmapFileTaskFactory.class);
		loadVizmapFileTaskFactory.loadStyles(in);
	}
}
