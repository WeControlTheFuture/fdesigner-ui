/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.fdesigner.workbench.internal.actions;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.ui.jface.dialogs.ErrorDialog;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.ui.jface.wizard.WizardDialog;
import org.fdesigner.workbench.IEditorInput;
import org.fdesigner.workbench.IEditorPart;
import org.fdesigner.workbench.INewWizard;
import org.fdesigner.workbench.IPluginContribution;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.actions.ActionFactory;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.LegacyResourceSupport;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.wizards.IWizardDescriptor;

/**
 * Opens a specific new wizard.
 */
public class NewWizardShortcutAction extends Action implements IPluginContribution {
	private IWizardDescriptor wizardElement;

	/**
	 * The wizard dialog width
	 */
	private static final int SIZING_WIZARD_WIDTH = 500;

	/**
	 * The wizard dialog height
	 */
	private static final int SIZING_WIZARD_HEIGHT = 500;

	private IWorkbenchWindow window;

	/**
	 * Create an instance of this class.
	 *
	 * @param window     the workbench window in which this action will appear
	 * @param wizardDesc a wizard element
	 */
	public NewWizardShortcutAction(IWorkbenchWindow window, IWizardDescriptor wizardDesc) {
		super(wizardDesc.getLabel());
		setToolTipText(wizardDesc.getDescription());
		setImageDescriptor(wizardDesc.getImageDescriptor());
		setId(ActionFactory.NEW.getId());
		wizardElement = wizardDesc;
		this.window = window;
	}

	/**
	 * Get the wizard descriptor for this action.
	 *
	 * @return the wizard descriptor
	 */
	public IWizardDescriptor getWizardDescriptor() {
		return wizardElement;
	}

	@Override
	public void run() {
		// create instance of target wizard

		INewWizard wizard;
		try {
			wizard = (INewWizard) wizardElement.createWizard();
		} catch (CoreException e) {
			ErrorDialog.openError(window.getShell(), WorkbenchMessages.NewWizardShortcutAction_errorTitle,
					WorkbenchMessages.NewWizardShortcutAction_errorMessage, e.getStatus());
			return;
		}

		ISelection selection = window.getSelectionService().getSelection();
		IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
		if (selection instanceof IStructuredSelection) {
			selectionToPass = wizardElement.adaptedSelection((IStructuredSelection) selection);
		} else {
			// Build the selection from the IFile of the editor
			IWorkbenchPart part = window.getPartService().getActivePart();
			if (part instanceof IEditorPart) {
				IEditorInput input = ((IEditorPart) part).getEditorInput();
				Class<?> fileClass = LegacyResourceSupport.getFileClass();
				if (input != null && fileClass != null) {
					Object file = Adapters.adapt(input, fileClass);
					if (file != null) {
						selectionToPass = new StructuredSelection(file);
					}
				}
			}
		}

		// even tho we MAY finish early without showing a dialog, prep the
		// wizard with a dialog and such in case it's logic requires it
		// - yes, it wastes a dialog but they are plentiful...
		wizard.init(window.getWorkbench(), selectionToPass);

		Shell parent = window.getShell();
		WizardDialog dialog = new WizardDialog(parent, wizard);
		dialog.create();
		Point defaultSize = dialog.getShell().getSize();
		dialog.getShell().setSize(Math.max(SIZING_WIZARD_WIDTH, defaultSize.x),
				Math.max(SIZING_WIZARD_HEIGHT, defaultSize.y));
		window.getWorkbench().getHelpSystem().setHelp(dialog.getShell(), IWorkbenchHelpContextIds.NEW_WIZARD_SHORTCUT);

		// if the wizard can finish early and doesn't have any pages, just finish it.
		if (wizardElement.canFinishEarly() && !wizardElement.hasPages()) {
			wizard.performFinish();
			dialog.close();
		} else {
			dialog.open();
		}
	}

	@Override
	public String getLocalId() {
		IPluginContribution contribution = getPluginContribution();
		if (contribution != null) {
			return contribution.getLocalId();
		}
		return wizardElement.getId();
	}

	@Override
	public String getPluginId() {
		IPluginContribution contribution = getPluginContribution();
		if (contribution != null) {
			return contribution.getPluginId();
		}
		return null;
	}

	/**
	 * Return the plugin contribution associated with the wizard.
	 *
	 * @return the contribution or <code>null</code>
	 * @since 3.1
	 */
	private IPluginContribution getPluginContribution() {
		return Adapters.adapt(wizardElement, IPluginContribution.class);
	}
}
