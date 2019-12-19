/*******************************************************************************
 * Copyright (c) 2019 SAP SE and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Christian Georgi (SAP SE) - Bug 540440
 *******************************************************************************/
package org.fdesigner.workbench.internal.keys.show;

import org.eclipse.swt.widgets.Display;
import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.ui.jface.preference.IPreferenceStore;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.IPreferenceConstants;
import org.fdesigner.workbench.internal.WorkbenchPlugin;

/**
 * Toggles whether to show keyboard shortcuts
 */
public class ShowKeysToggleHandler extends AbstractHandler {

	public static final String COMMAND_ID = "org.eclipse.ui.toggleShowKeys"; //$NON-NLS-1$
	private static ShowKeysUI showKeysUI;

	@Override
	public Object execute(ExecutionEvent event) {
		IPreferenceStore prefStore = WorkbenchPlugin.getDefault().getPreferenceStore();
		boolean newValue = toggleValue(IPreferenceConstants.SHOW_KEYS_ENABLED, prefStore);
		if (newValue) {
			showPreview(prefStore);
		}
		return newValue;
	}

	private boolean toggleValue(String key, IPreferenceStore prefStore) {
		boolean newValue = !prefStore.getBoolean(key);
		prefStore.setValue(key, newValue);
		return newValue;
	}

	private void showPreview(IPreferenceStore prefStore) {
		if (showKeysUI == null) {
			// keep a singleton so that multiple quick invocations of this command
			// do not end up in multiple popups
			showKeysUI = new ShowKeysUI(PlatformUI.getWorkbench(), prefStore);
		}
		Display.getDefault().asyncExec(() -> showKeysUI.openForPreview(ShowKeysToggleHandler.COMMAND_ID, null));
	}

}
