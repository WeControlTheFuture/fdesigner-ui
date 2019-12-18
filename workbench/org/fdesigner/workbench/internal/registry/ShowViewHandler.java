/*******************************************************************************
 * Copyright (c) 2004, 2015 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.registry;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PartInitException;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.misc.StatusUtil;
import org.fdesigner.workbench.statushandlers.StatusManager;

/**
 * Command handler to show a particular view.
 *
 * @since 3.0
 */
public final class ShowViewHandler extends AbstractHandler {

	/**
	 * The identifier of the view this handler should open. This value should never
	 * be <code>null</code>.
	 */
	private final String viewId;

	/**
	 * Constructs a new instance of <code>ShowViewHandler</code>.
	 *
	 * @param viewId The identifier of the view this handler should open; must not
	 *               be <code>null</code>.
	 */
	public ShowViewHandler(final String viewId) {
		this.viewId = viewId;
	}

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		final IWorkbenchPage activePage = activeWorkbenchWindow.getActivePage();
		if (activePage == null) {
			return null;
		}

		try {
			activePage.showView(viewId);
		} catch (PartInitException e) {
			IStatus status = StatusUtil.newStatus(e.getStatus(), e.getMessage());
			StatusManager.getManager().handle(status, StatusManager.SHOW);
		}

		return null;
	}
}
