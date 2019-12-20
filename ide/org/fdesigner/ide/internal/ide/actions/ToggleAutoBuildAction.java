/*******************************************************************************
 * Copyright (c) 2004, 2014 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM - Initial API and implementation
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.actions;

import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.resources.IWorkspace;
import org.fdesigner.resources.IWorkspaceDescription;
import org.fdesigner.resources.ResourcesPlugin;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.ui.jface.action.Action;
import org.fdesigner.ui.jface.dialogs.ErrorDialog;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.actions.ActionFactory;

/**
 * Action for toggling autobuild on or off.
 */
public class ToggleAutoBuildAction extends Action implements
		ActionFactory.IWorkbenchAction {
	private IWorkbenchWindow window;

	/**
	 * Creates a new ToggleAutoBuildAction
	 * @param window The window for parenting dialogs associated with this action
	 */
	public ToggleAutoBuildAction(IWorkbenchWindow window) {
		super(IDEWorkbenchMessages.Workbench_buildAutomatically);
		this.window = window;
		setChecked(ResourcesPlugin.getWorkspace().isAutoBuilding());
	}

	@Override
	public void dispose() {
		window = null;
	}

	@Override
	public void run() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceDescription description = workspace.getDescription();
		description.setAutoBuilding(!description.isAutoBuilding());
		try {
			workspace.setDescription(description);
		} catch (CoreException e) {
			ErrorDialog.openError(window.getShell(), null, null, e.getStatus());
		}
	}
}
