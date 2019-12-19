/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.ui.jface.preference.PreferenceDialog;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.dialogs.PropertyDialog;

/**
 * @since 3.4
 *
 */
public class PropertyDialogHandler extends AbstractHandler {

	private String initialPageId = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PreferenceDialog dialog;
		Object element = null;
		IStructuredSelection currentSelection = HandlerUtil.getCurrentStructuredSelection(event);
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		Shell shell;

		element = currentSelection.getFirstElement();

		if (activeWorkbenchWindow != null) {
			shell = activeWorkbenchWindow.getShell();
			dialog = PropertyDialog.createDialogOn(shell, initialPageId, element);
			if (dialog != null) {
				dialog.open();
			}
		}
		return null;
	}

}
