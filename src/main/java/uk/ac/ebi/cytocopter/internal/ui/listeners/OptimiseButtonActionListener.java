package uk.ac.ebi.cytocopter.internal.ui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.cytoscape.work.swing.DialogTaskManager;
import uk.ac.ebi.cytocopter.internal.cellnoptr.tasks.Observer;

import uk.ac.ebi.cytocopter.internal.cellnoptr.tasks.OptimiseTaskFactory;
import uk.ac.ebi.cytocopter.internal.ui.panels.ControlPanel;

public class OptimiseButtonActionListener implements ActionListener
{

	private ControlPanel controlPanel;

	public OptimiseButtonActionListener(ControlPanel controlPanel)
	{
		this.controlPanel = controlPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
                Observer taskObserver = new Observer(); 
		OptimiseTaskFactory optimiseTaskFactory = new OptimiseTaskFactory(controlPanel.cyServiceRegistrar, true);
		controlPanel.cyServiceRegistrar.getService(DialogTaskManager.class)
				.execute(optimiseTaskFactory.createTaskIterator(), taskObserver);
                controlPanel.SBMLExportButton.setEnabled(true);
                
	}
}
