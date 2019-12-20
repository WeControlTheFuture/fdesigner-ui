/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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

package org.fdesigner.ide.internal.ide.handlers;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.resources.IResource;
import org.fdesigner.ui.jface.viewers.ISelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.workbench.IPageLayout;
import org.fdesigner.workbench.IViewPart;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PartInitException;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.part.ISetSelectionTarget;

/**
 * A command handler to show a resource in the Navigator view given the resource
 * path.
 *
 * @since 3.2
 */
public class ShowResourceByPathHandler extends AbstractHandler {

	private static final String PARAM_ID_RESOURCE_PATH = "resourcePath"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IResource resource = (IResource) event
				.getObjectParameterForExecution(PARAM_ID_RESOURCE_PATH);

		IWorkbenchWindow activeWindow = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		IWorkbenchPage activePage = activeWindow.getActivePage();
		if (activePage == null) {
			throw new ExecutionException("no active workbench page"); //$NON-NLS-1$
		}

		try {
			IViewPart view = activePage.showView(IPageLayout.ID_RES_NAV);
			if (view instanceof ISetSelectionTarget) {
				ISelection selection = new StructuredSelection(resource);
				((ISetSelectionTarget) view).selectReveal(selection);
			}
		} catch (PartInitException e) {
			throw new ExecutionException("error showing resource in navigator"); //$NON-NLS-1$
		}

		return null;
	}

}
