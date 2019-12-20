/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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
package org.fdesigner.ide.extensions.actions;

import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.ide.internal.ide.IIDEHelpContextIds;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.ui.jface.dialogs.IDialogSettings;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.ui.jface.wizard.WizardDialog;
import org.fdesigner.workbench.ISharedImages;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.dialogs.NewWizard;

/**
 * Standard action for launching the create project selection
 * wizard.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class NewProjectAction extends Action {

	/**
	 * The wizard dialog width
	 */
	private static final int SIZING_WIZARD_WIDTH = 500;

	/**
	 * The wizard dialog height
	 */
	private static final int SIZING_WIZARD_HEIGHT = 500;

	/**
	 * The workbench window this action will run in
	 */
	private IWorkbenchWindow window;

	/**
	 * This default constructor allows the the action to be called from the welcome page.
	 */
	public NewProjectAction() {
		this(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	}

	/**
	 * Creates a new action for launching the new project
	 * selection wizard.
	 *
	 * @param window the workbench window to query the current
	 * 		selection and shell for opening the wizard.
	 */
	public NewProjectAction(IWorkbenchWindow window) {
		super(IDEWorkbenchMessages.NewProjectAction_text);
		if (window == null) {
			throw new IllegalArgumentException();
		}
		this.window = window;
		ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(images
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD));
		setDisabledImageDescriptor(images
				.getImageDescriptor(ISharedImages.IMG_TOOL_NEW_WIZARD_DISABLED));
		setToolTipText(IDEWorkbenchMessages.NewProjectAction_toolTip);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				org.fdesigner.workbench.internal.IWorkbenchHelpContextIds.NEW_ACTION);
	}

	@Override
	public void run() {
		// Create wizard selection wizard.
		IWorkbench workbench = PlatformUI.getWorkbench();
		NewWizard wizard = new NewWizard();
		wizard.setProjectsOnly(true);
		ISelection selection = window.getSelectionService().getSelection();
		IStructuredSelection selectionToPass = StructuredSelection.EMPTY;
		if (selection instanceof IStructuredSelection) {
			selectionToPass = (IStructuredSelection) selection;
		}
		wizard.init(workbench, selectionToPass);
		IDialogSettings workbenchSettings = IDEWorkbenchPlugin.getDefault()
				.getDialogSettings();
		IDialogSettings wizardSettings = workbenchSettings
				.getSection("NewWizardAction");//$NON-NLS-1$
		if (wizardSettings == null) {
			wizardSettings = workbenchSettings.addNewSection("NewWizardAction");//$NON-NLS-1$
		}
		wizard.setDialogSettings(wizardSettings);
		wizard.setForcePreviousAndNextButtons(true);

		// Create wizard dialog.
		WizardDialog dialog = new WizardDialog(null, wizard);
		dialog.create();
		dialog.getShell().setSize(
				Math.max(SIZING_WIZARD_WIDTH, dialog.getShell().getSize().x),
				SIZING_WIZARD_HEIGHT);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(dialog.getShell(),
				IIDEHelpContextIds.NEW_PROJECT_WIZARD);

		// Open wizard.
		dialog.open();
	}
}
