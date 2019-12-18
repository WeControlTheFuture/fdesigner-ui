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
package org.fdesigner.workbench.internal;

import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.dialogs.IDialogConstants;
import org.fdesigner.ui.jface.dialogs.MessageDialog;
import org.fdesigner.workbench.IPerspectiveDescriptor;
import org.fdesigner.workbench.IWorkbenchCommandConstants;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PlatformUI;

/**
 * Reset the layout within the active perspective.
 */
public class ResetPerspectiveAction extends PerspectiveAction {

	/**
	 * This default constructor allows the the action to be called from the welcome
	 * page.
	 */
	public ResetPerspectiveAction() {
		this(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
	}

	/**
	 * Create an instance of this class
	 *
	 * @param window the window
	 */
	public ResetPerspectiveAction(IWorkbenchWindow window) {
		super(window);
		setText(WorkbenchMessages.ResetPerspective_text);
		setActionDefinitionId(IWorkbenchCommandConstants.WINDOW_RESET_PERSPECTIVE);
		// @issue missing action id
		setToolTipText(WorkbenchMessages.ResetPerspective_toolTip);
		window.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.RESET_PERSPECTIVE_ACTION);
	}

	@Override
	protected void run(IWorkbenchPage page, IPerspectiveDescriptor persp) {
		String message = NLS.bind(WorkbenchMessages.ResetPerspective_message, persp.getLabel());
		MessageDialog d = new MessageDialog(getWindow().getShell(), WorkbenchMessages.ResetPerspective_title, null,
				message, MessageDialog.QUESTION, 0, IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL);
		if (d.open() == 0) {
			page.resetPerspective();
		}
	}

}
