/*******************************************************************************
 * Copyright (c) 2008, 2015 IBM Corporation and others.
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

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.runtime.common.runtime.IAdaptable;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.WorkbenchException;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.Workbench;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.misc.StatusUtil;
import org.fdesigner.workbench.statushandlers.StatusManager;

/**
 * @since 3.4
 *
 */
public class OpenInNewWindowHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);
		if (activeWorkbenchWindow == null) {
			return null;
		}
		try {
			String perspId = null;

			IWorkbenchPage page = activeWorkbenchWindow.getActivePage();
			IAdaptable pageInput = ((Workbench) activeWorkbenchWindow.getWorkbench()).getDefaultPageInput();
			if (page != null && page.getPerspective() != null) {
				perspId = page.getPerspective().getId();
				pageInput = page.getInput();
			} else {
				perspId = activeWorkbenchWindow.getWorkbench().getPerspectiveRegistry().getDefaultPerspective();
			}

			activeWorkbenchWindow.getWorkbench().openWorkbenchWindow(perspId, pageInput);
		} catch (WorkbenchException e) {
			StatusUtil.handleStatus(e.getStatus(),
					WorkbenchMessages.OpenInNewWindowAction_errorTitle + ": " + e.getMessage(), //$NON-NLS-1$
					StatusManager.SHOW);
		}
		return null;

	}

}
