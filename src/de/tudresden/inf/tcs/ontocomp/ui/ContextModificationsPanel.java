package de.tudresden.inf.tcs.ontocomp.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.semanticweb.owl.model.OWLClass;

import org.apache.log4j.Logger;

import de.tudresden.inf.tcs.fcaapi.change.ContextChange;
import de.tudresden.inf.tcs.oclib.change.AbstractContextModification;
import de.tudresden.inf.tcs.ontocomp.ui.OntoComPViewComponent;

public class ContextModificationsPanel extends JPanel implements ItemListener {

	private static final long serialVersionUID = 1L;
	
	private OntoComPViewComponent viewComponent;
	
	// number of checked context modifications for controlling undo buttons
	private int checkedCount;
	
	private HashMap<ContextChange<OWLClass>,JCheckBox> modificationsHash;
	
	/**
	 * The logger.
	 */
	private static final Logger logger = Logger.getLogger(ContextModificationsPanel.class);
	
	public ContextModificationsPanel(OntoComPViewComponent vc) {
		super();
		viewComponent = vc;
		// GridLayout myLayout = new GridLayout(0,2);
		 setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		 setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		 setBackground(Color.WHITE);
		 modificationsHash = new HashMap<ContextChange<OWLClass>,JCheckBox>();
		// setLayout(myLayout);
	}
	
	protected OntoComPViewComponent getViewComponent() {
		return viewComponent;
	}
	
	public void update() {
		removeAll();
		modificationsHash.clear();
		logger.debug("History size: " + getViewComponent().getContext().getHistory().size());
	 	// for (int i = getViewComponent().getContext().getHistory().size() - 1; i >= 0; --i) {
	 	for (Iterator<AbstractContextModification> it = getViewComponent().getContext().getHistory().iterator(); it.hasNext();) {
	 		// add(new ContextModificationRow(this,getViewComponent().getContext().getHistory().get(i)));
	 		AbstractContextModification modification = it.next();
	 		JCheckBox checkBox = new JCheckBox();
	 		checkBox.addItemListener(this);
	 		modificationsHash.put(modification, checkBox);
	 		ContextModificationRow modificationRow = 
	 			new ContextModificationRow(modification,checkBox);
	 		add(modificationRow);
	 		add(Box.createRigidArea(new Dimension(0, 5)));
	 		// add(new ContextModificationRow(modification,checkBox));
	 	}
	 	revalidate();
	 	repaint();
	}
	
	public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
        	++checkedCount;
        	getViewComponent().setUndoSelectedModificationsButton(true);
        } else {
        	--checkedCount;
        	if (checkedCount == 0) {
        		getViewComponent().setUndoSelectedModificationsButton(false);
        	}
        }
	}
	
	public boolean isSelected(ContextChange<OWLClass> m) {
		return modificationsHash.get(m).isSelected();
	}
	
//	public JEditorPane createEditorPane(ContextChange<OWLClass> c) {
//		JEditorPane editorPane = n//ew JEditorPane();
//		editorPane.setEditable(false);
// 		editorPane.setContentType("text/html");
// 		editorPane.setEditorKit(new HTMLEditorKit());
//		try {
// 			switch (c.getType()) {
// 			case ContextChange.NEW_IMPLICATION_MODIFICATION:
// 				System.out.println("new implication");
// 				editorPane.getEditorKit().read(new java.io.StringReader ("<html><body>" +
// 					GUIConstants.NEW_IMPLICATION_MODIFICATION_TEXT_PART1 + 
// 					renderer.render(((NewSubClassAxiomChange) c).getImplication().getPremise()) +
// 					GUIConstants.NEW_IMPLICATION_MODIFICATION_TEXT_PART2 + 
// 					renderer.render(((NewSubClassAxiomChange) c).getImplication().getConclusion()) +
// 					"</body></html>"),editorPane.getDocument(),
// 					editorPane.getDocument().getLength());
// 				break;
// 			case ContextChange.NEW_OBJECT_MODIFICATION:
// 				System.out.println("new object");
// 				editorPane.getEditorKit().read(new java.io.StringReader ("<html><body>" +
// 						GUIConstants.NEW_OBJECT_MODIFICATION_TEXT_PART1 +
// 						renderer.render(((NewIndividualChange) c).getObject()) +
// 						GUIConstants.NEW_OBJECT_MODIFICATION_TEXT_PART2 +
// 						renderer.render(((NewIndividualChange) c).getAttributes()) +
// 					"</body></html>"),editorPane.getDocument(),
// 					editorPane.getDocument().getLength());
// 				break;
// 			case ContextChange.OBJECT_HAS_ATTRIBUTE_MODIFICATION:
// 				System.out.println("object has attribute");
// 				editorPane.getEditorKit().read(new java.io.StringReader ("<html><body>" +
// 						GUIConstants.CLASSASSERTION_MODIFICATION_TEXT_PART1 +
// 						renderer.render(((ClassAssertionChange) c).getObject()) +
// 						GUIConstants.CLASSASSERTION_MODIFICATION_TEXT_PART2 +
// 						renderer.render(((ClassAssertionChange) c).getAttribute(),
// 								((ClassAssertionChange) c).isComplemented()) +
// 					"</body></html>"),editorPane.getDocument(),
// 					editorPane.getDocument().getLength());
// 				break;
// 			}
// 		}
// 		catch (BadLocationException e) {
// 			e.printStackTrace();
// 		}
// 		catch (IOException e) {
// 			e.printStackTrace();
// 		}
// 		
// 		return editorPane;
//	}
	// @Override
	// public void validate() {
	// 	System.out.println("update");
	// 	update();
	// }
	
	// public void validate() {
	// 	for (int i = history.size() - 1; i >= 0; --i) {
	// 		// System.out.println(history.get(i).getClass().getName());
	// 		// System.out.println(history.get(i).getChange().getAxiom());
	// 		// if (history.get(i).getClass().getName() == "ClassAssertionChange") {
	// 		// 	
	// 		// }
	// 		switch (history.get(i).getType()) {
	// 		case ContextChange.NEW_IMPLICATION_MODIFICATION:
	// 			System.out.println(((NewSubClassAxiomChange) history.get(i)).getImplication());
	// 			System.out.println();
	// 			break;
	// 		case ContextChange.NEW_OBJECT_MODIFICATION:
	// 			System.out.println(((NewIndividualChange) history.get(i)).getObject());
	// 			System.out.println(((NewIndividualChange) history.get(i)).getAttributes());
	// 			System.out.println();
	// 			break;
	// 		case ContextChange.OBJECT_HAS_ATTRIBUTE_MODIFICATION:
	// 			System.out.println(((ClassAssertionChange) history.get(i)).getObject());
	// 			System.out.println(((ClassAssertionChange) history.get(i)).getAttribute());
	// 			System.out.println(((ClassAssertionChange) history.get(i)).isComplemented());
	// 			System.out.println();
	// 			break;
	// 		}
	// 	}
	// }
}
