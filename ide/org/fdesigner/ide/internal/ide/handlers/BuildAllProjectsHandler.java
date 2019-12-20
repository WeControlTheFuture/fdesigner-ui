/*******************************************************************************
 * Copyright (c) 2010, 2015 Wind River Systems, Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Wind River Systems - initial API and implementation
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.handlers;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.ide.extensions.actions.GlobalBuildAction;
import org.fdesigner.ide.internal.ide.actions.BuildUtilities;
import org.fdesigner.resources.IProject;
import org.fdesigner.resources.IWorkspace;
import org.fdesigner.resources.IncrementalProjectBuilder;
import org.fdesigner.resources.ResourcesPlugin;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.handlers.HandlerUtil;

/**
 * Default handler for 'Build All' command.
 *
 * @since 3.6
 */
public class BuildAllProjectsHandler extends AbstractHandler {

	/**
	 * @throws ExecutionException
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (isEnabled()) {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			if (window != null) {
				GlobalBuildAction globalBuildAction = new GlobalBuildAction(window, IncrementalProjectBuilder.INCREMENTAL_BUILD);
				try {
					globalBuildAction.run();
				} finally {
					globalBuildAction.dispose();
				}
			}
		}
		return null;
	}

	/*
	 * @see org.eclipse.core.commands.AbstractHandler#setEnabled(java.lang.Object)
	 */
	@Override
	public void setEnabled(Object evaluationContext) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IProject[] projects = workspace.getRoot().getProjects();
		boolean enabled = BuildUtilities.isEnabled(projects, IncrementalProjectBuilder.INCREMENTAL_BUILD);
		setBaseEnabled(enabled);
	}
}
