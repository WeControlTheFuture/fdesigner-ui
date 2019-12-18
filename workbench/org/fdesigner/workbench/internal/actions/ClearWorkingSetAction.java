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

import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.actions.WorkingSetFilterActionGroup;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.WorkbenchMessages;

/**
 * Clears the selected working set in the working set action group.
 *
 * @since 2.1
 */
public class ClearWorkingSetAction extends Action {
	private WorkingSetFilterActionGroup actionGroup;

	/**
	 * Creates a new instance of the receiver.
	 *
	 * @param actionGroup the action group this action is created in
	 */
	public ClearWorkingSetAction(WorkingSetFilterActionGroup actionGroup) {
		super(WorkbenchMessages.ClearWorkingSetAction_text);
		Assert.isNotNull(actionGroup);
		setToolTipText(WorkbenchMessages.ClearWorkingSetAction_toolTip);
		setEnabled(actionGroup.getWorkingSet() != null);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.CLEAR_WORKING_SET_ACTION);
		this.actionGroup = actionGroup;
	}

	/**
	 * Overrides method from Action
	 *
	 * @see Action#run
	 */
	@Override
	public void run() {
		actionGroup.setWorkingSet(null);
	}
}
