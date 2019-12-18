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

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.ui.jface.dialogs.MessageDialog;
import org.fdesigner.ui.jface.window.Window;
import org.fdesigner.ui.jface.wizard.WizardDialog;
import org.fdesigner.workbench.IWorkingSet;
import org.fdesigner.workbench.IWorkingSetManager;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.actions.WorkingSetFilterActionGroup;
import org.fdesigner.workbench.dialogs.IWorkingSetEditWizard;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.WorkbenchMessages;

/**
 * Displays an IWorkingSetEditWizard for editing a working set.
 *
 * @since 2.1
 */
public class EditWorkingSetAction extends Action {
	private Shell shell;

	private WorkingSetFilterActionGroup actionGroup;

	/**
	 * Creates a new instance of the receiver.
	 *
	 * @param actionGroup the action group this action is created in
	 * @param shell       the parent shell
	 */
	public EditWorkingSetAction(WorkingSetFilterActionGroup actionGroup, Shell shell) {
		super(WorkbenchMessages.EditWorkingSetAction_text);
		Assert.isNotNull(actionGroup);
		setToolTipText(WorkbenchMessages.EditWorkingSetAction_toolTip);

		this.shell = shell;
		this.actionGroup = actionGroup;
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.EDIT_WORKING_SET_ACTION);
	}

	/**
	 * Overrides method from Action
	 *
	 * @see Action#run
	 */
	@Override
	public void run() {
		IWorkingSetManager manager = PlatformUI.getWorkbench().getWorkingSetManager();
		IWorkingSet workingSet = actionGroup.getWorkingSet();

		if (workingSet == null) {
			setEnabled(false);
			return;
		}
		IWorkingSetEditWizard wizard = manager.createWorkingSetEditWizard(workingSet);
		if (wizard == null) {
			String title = WorkbenchMessages.EditWorkingSetAction_error_nowizard_title;
			String message = WorkbenchMessages.EditWorkingSetAction_error_nowizard_message;
			MessageDialog.openError(shell, title, message);
			return;
		}
		WizardDialog dialog = new WizardDialog(shell, wizard);
		dialog.create();
		if (dialog.open() == Window.OK) {
			actionGroup.setWorkingSet(wizard.getSelection());
		}
	}
}
