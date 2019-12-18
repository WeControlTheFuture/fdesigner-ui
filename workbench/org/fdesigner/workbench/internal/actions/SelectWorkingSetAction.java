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
import org.fdesigner.ui.jface.window.Window;
import org.fdesigner.workbench.IWorkingSet;
import org.fdesigner.workbench.IWorkingSetManager;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.actions.WorkingSetFilterActionGroup;
import org.fdesigner.workbench.dialogs.IWorkingSetSelectionDialog;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.WorkbenchMessages;

/**
 * Displays an IWorkingSetSelectionDialog and sets the selected working set in
 * the action group.
 *
 * @since 2.1
 */
public class SelectWorkingSetAction extends Action {
	private Shell shell;

	private WorkingSetFilterActionGroup actionGroup;

	/**
	 * Creates a new instance of the receiver.
	 *
	 * @param actionGroup the action group this action is created in
	 * @param shell       shell to use for opening working set selection dialog.
	 */
	public SelectWorkingSetAction(WorkingSetFilterActionGroup actionGroup, Shell shell) {
		super(WorkbenchMessages.SelectWorkingSetAction_text);
		Assert.isNotNull(actionGroup);
		setToolTipText(WorkbenchMessages.SelectWorkingSetAction_toolTip);

		this.shell = shell;
		this.actionGroup = actionGroup;
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.SELECT_WORKING_SET_ACTION);
	}

	/**
	 * Overrides method from Action
	 *
	 * @see Action#run()
	 */
	@Override
	public void run() {
		IWorkingSetManager manager = PlatformUI.getWorkbench().getWorkingSetManager();
		IWorkingSetSelectionDialog dialog = manager.createWorkingSetSelectionDialog(shell, false);
		IWorkingSet workingSet = actionGroup.getWorkingSet();

		if (workingSet != null) {
			dialog.setSelection(new IWorkingSet[] { workingSet });
		}

		if (dialog.open() == Window.OK) {
			IWorkingSet[] result = dialog.getSelection();
			if (result != null && result.length > 0) {
				actionGroup.setWorkingSet(result[0]);
				manager.addRecentWorkingSet(result[0]);
			} else {
				actionGroup.setWorkingSet(null);
			}
		} else {
			actionGroup.setWorkingSet(workingSet);
		}
	}
}
