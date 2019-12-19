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

package org.fdesigner.workbench.internal.handlers;

import org.eclipse.swt.widgets.Shell;
import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.ui.jface.preference.PreferenceDialog;
import org.fdesigner.workbench.IWorkbenchCommandConstants;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.dialogs.PreferencesUtil;
import org.fdesigner.workbench.handlers.HandlerUtil;

/**
 * <p>
 * Shows the given preference page. If no preference page id is specified in the
 * parameters, then this opens the preferences dialog to whatever page was
 * active the last time the dialog was shown.
 * </p>
 * <p>
 * This class is not intended for use outside of the
 * <code>org.eclipse.ui.workbench</code> plug-in.
 * </p>
 *
 * @since 3.2
 */
public final class ShowPreferencePageHandler extends AbstractHandler {

	public ShowPreferencePageHandler() {
		super();
	}

	@Override
	public Object execute(final ExecutionEvent event) {
		final String preferencePageId = event.getParameter(IWorkbenchCommandConstants.WINDOW_PREFERENCES_PARM_PAGEID);
		final IWorkbenchWindow activeWorkbenchWindow = HandlerUtil.getActiveWorkbenchWindow(event);

		final Shell shell;
		if (activeWorkbenchWindow == null) {
			shell = null;
		} else {
			shell = activeWorkbenchWindow.getShell();
		}

		final PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(shell, preferencePageId, null, null);
		dialog.open();

		return null;
	}

}
