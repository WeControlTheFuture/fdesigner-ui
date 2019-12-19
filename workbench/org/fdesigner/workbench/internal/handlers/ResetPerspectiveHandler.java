/*******************************************************************************
 * Copyright (c) 2011, 2014 IBM Corporation and others.
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
 ******************************************************************************/

package org.fdesigner.workbench.internal.handlers;

import org.eclipse.swt.SWT;
import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.dialogs.IDialogConstants;
import org.fdesigner.ui.jface.dialogs.MessageDialog;
import org.fdesigner.ui.jface.dialogs.MessageDialogWithToggle;
import org.fdesigner.ui.jface.window.Window;
import org.fdesigner.workbench.IPerspectiveDescriptor;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPage;
import org.fdesigner.workbench.internal.registry.PerspectiveDescriptor;
import org.fdesigner.workbench.internal.registry.PerspectiveRegistry;

/**
 * @since 3.5
 *
 */
public class ResetPerspectiveHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {

		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow != null) {
			WorkbenchPage page = (WorkbenchPage) activeWorkbenchWindow.getActivePage();
			if (page != null) {
				IPerspectiveDescriptor descriptor = page.getPerspective();
				if (descriptor != null) {
					boolean offerRevertToBase = false;
					if (descriptor instanceof PerspectiveDescriptor) {
						PerspectiveDescriptor desc = (PerspectiveDescriptor) descriptor;
						offerRevertToBase = desc.isPredefined() && desc.hasCustomDefinition();
					}

					if (offerRevertToBase) {
						String message = NLS.bind(WorkbenchMessages.RevertPerspective_message, descriptor.getLabel());
						boolean toggleState = false;
						MessageDialogWithToggle dialog = MessageDialogWithToggle.open(MessageDialog.QUESTION,
								activeWorkbenchWindow.getShell(), WorkbenchMessages.RevertPerspective_title, message,
								WorkbenchMessages.RevertPerspective_option, toggleState, null, null, SWT.SHEET);
						if (dialog.getReturnCode() == IDialogConstants.YES_ID) {
							if (dialog.getToggleState()) {
								PerspectiveRegistry reg = (PerspectiveRegistry) PlatformUI.getWorkbench()
										.getPerspectiveRegistry();
								reg.revertPerspective(descriptor);
							}
							page.resetPerspective();
						}
					} else {
						String message = NLS.bind(WorkbenchMessages.ResetPerspective_message, descriptor.getLabel());

						int result = MessageDialog.open(MessageDialog.CONFIRM, activeWorkbenchWindow.getShell(),
								WorkbenchMessages.ResetPerspective_title, message, SWT.SHEET,
								WorkbenchMessages.ResetPerspective_buttonLabel, IDialogConstants.NO_LABEL);

						if (result == Window.OK) {
							page.resetPerspective();
						}
					}
				}
			}
		}

		return null;
	}
}
