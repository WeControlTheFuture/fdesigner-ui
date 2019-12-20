/*******************************************************************************
 * Copyright (c) 2012, 2015 IBM Corporation and others.
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
 *     Freescale - Bug 411287 - Quick Access > Build Project is offered even if no valid selection exists
 ******************************************************************************/

package org.fdesigner.ide.internal.ide.handlers;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.ide.extensions.actions.BuildAction;
import org.fdesigner.ide.extensions.part.FileEditorInput;
import org.fdesigner.resources.IProject;
import org.fdesigner.resources.IncrementalProjectBuilder;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.workbench.IEditorInput;
import org.fdesigner.workbench.ISources;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.handlers.HandlerUtil;

/**
 * Default Handler for 'Build Project' command
 *
 * @since 4.3
 *
 */
public class BuildProjectHandler extends AbstractHandler {

	/**
	 * @throws ExecutionException
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if (window != null) {

			ISelection currentSelection = HandlerUtil
					.getCurrentSelection(event);

			if (currentSelection instanceof IStructuredSelection) {
				runBuildAction(window, currentSelection);
			} else {
				currentSelection = extractSelectionFromEditorInput(HandlerUtil
						.getActiveEditorInput(event));
				runBuildAction(window, currentSelection);
			}
		}
		return null;
	}

	private ISelection extractSelectionFromEditorInput(
			IEditorInput activeEditorInput) {
		if (activeEditorInput instanceof FileEditorInput) {
			IProject project = ((FileEditorInput) activeEditorInput).getFile()
					.getProject();
			return new StructuredSelection(project);
		}

		return null;
	}

	private void runBuildAction(IWorkbenchWindow window,
			ISelection currentSelection) {
		BuildAction buildAction = newBuildAction(window);
		buildAction.selectionChanged((IStructuredSelection) currentSelection);
		buildAction.run();
	}

	private BuildAction newBuildAction(IWorkbenchWindow window) {
		return new BuildAction(window,
				IncrementalProjectBuilder.INCREMENTAL_BUILD);
	}

	@Override
	public void setEnabled(Object evaluationContext) {
		boolean enabled = false;
		if ((evaluationContext instanceof IEvaluationContext)) {
			IEvaluationContext context = (IEvaluationContext) evaluationContext;
			Object object = context.getVariable(ISources.ACTIVE_WORKBENCH_WINDOW_NAME);
			if (object instanceof IWorkbenchWindow) {
				BuildAction buildAction = newBuildAction((IWorkbenchWindow) object);
				enabled = buildAction.isEnabled();
			}
		}
		setBaseEnabled(enabled);
	}

}
