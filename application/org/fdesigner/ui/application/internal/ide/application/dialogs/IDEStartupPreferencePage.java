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
 *     Eric Rizzo - removed "prompt for workspace on startup" checkbox
 *******************************************************************************/
package org.fdesigner.ui.application.internal.ide.application.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.fdesigner.ide.internal.ide.IDEInternalPreferences;
import org.fdesigner.ide.internal.ide.IDEWorkbenchMessages;
import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.ui.jface.preference.IPreferenceStore;
import org.fdesigner.workbench.IWorkbenchPreferencePage;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.IWorkbenchHelpContextIds;
import org.fdesigner.workbench.internal.dialogs.StartupPreferencePage;

/**
 * Extends the Startup and Shutdown preference page with IDE-specific settings.
 *
 * Note: want IDE settings to appear in main Workbench preference page (via subclassing),
 *   however the superclass, StartupPreferencePage, is internal
 * @since 3.0
 */
public class IDEStartupPreferencePage extends StartupPreferencePage implements
		IWorkbenchPreferencePage {

	private Button refreshButton;

	private Button showProblemsButton;

	private Button exitPromptButton;

	@Override
	protected Control createContents(Composite parent) {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,
				IWorkbenchHelpContextIds.STARTUP_PREFERENCE_PAGE);

		Composite composite = createComposite(parent);

		createRefreshWorkspaceOnStartupPref(composite);
		createProblemsViewOnStartupPref(composite);
		createExitPromptPref(composite);

		Label space = new Label(composite,SWT.NONE);
		space.setLayoutData(new GridData());

		createEarlyStartupSelection(composite);

		return composite;
	}

	/**
	 * The default button has been pressed.
	 */
	@Override
	protected void performDefaults() {
		IPreferenceStore store = getIDEPreferenceStore();

		refreshButton
				.setSelection(store
						.getDefaultBoolean(IDEInternalPreferences.REFRESH_WORKSPACE_ON_STARTUP));

		showProblemsButton.setSelection(
				store.getDefaultBoolean(IDEInternalPreferences.SHOW_PROBLEMS_VIEW_DECORATIONS_ON_STARTUP));

		exitPromptButton
				.setSelection(store
						.getDefaultBoolean(IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW));

		super.performDefaults();
	}

	/**
	 * The user has pressed Ok. Store/apply this page's values appropriately.
	 */
	@Override
	public boolean performOk() {
		IPreferenceStore store = getIDEPreferenceStore();

		// store the refresh workspace on startup setting
		store.setValue(IDEInternalPreferences.REFRESH_WORKSPACE_ON_STARTUP,
				refreshButton.getSelection());

		store.setValue(IDEInternalPreferences.SHOW_PROBLEMS_VIEW_DECORATIONS_ON_STARTUP,
				showProblemsButton.getSelection());

		// store the exit prompt on last window close setting
		store.setValue(IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW,
				exitPromptButton.getSelection());

		IDEWorkbenchPlugin.getDefault().savePluginPreferences();

		return super.performOk();
	}

	protected void createRefreshWorkspaceOnStartupPref(Composite composite) {
		refreshButton = new Button(composite, SWT.CHECK);
		refreshButton.setText(IDEWorkbenchMessages.StartupPreferencePage_refreshButton);
		refreshButton.setFont(composite.getFont());
		refreshButton.setSelection(getIDEPreferenceStore().getBoolean(
				IDEInternalPreferences.REFRESH_WORKSPACE_ON_STARTUP));
	}

	protected void createProblemsViewOnStartupPref(Composite composite) {
		showProblemsButton = new Button(composite, SWT.CHECK);
		showProblemsButton.setText(IDEWorkbenchMessages.StartupPreferencePage_showProblemsButton);
		showProblemsButton.setFont(composite.getFont());
		showProblemsButton
				.setSelection(getIDEPreferenceStore()
						.getBoolean(IDEInternalPreferences.SHOW_PROBLEMS_VIEW_DECORATIONS_ON_STARTUP));
	}

	protected void createExitPromptPref(Composite composite) {
		exitPromptButton = new Button(composite, SWT.CHECK);
		exitPromptButton.setText(IDEWorkbenchMessages.StartupPreferencePage_exitPromptButton);
		exitPromptButton.setFont(composite.getFont());
		exitPromptButton.setSelection(getIDEPreferenceStore().getBoolean(
				IDEInternalPreferences.EXIT_PROMPT_ON_CLOSE_LAST_WINDOW));
	}

	/**
	 * Returns the IDE preference store.
	 */
	protected IPreferenceStore getIDEPreferenceStore() {
		return IDEWorkbenchPlugin.getDefault().getPreferenceStore();
	}
}
