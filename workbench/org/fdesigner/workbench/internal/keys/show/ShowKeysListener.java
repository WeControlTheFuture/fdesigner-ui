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
import org.eclipse.swt.widgets.Event;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.commands.IExecutionListener;
import org.fdesigner.commands.NotHandledException;
import org.fdesigner.ui.jface.preference.IPreferenceStore;
import org.fdesigner.ui.jface.util.IPropertyChangeListener;
import org.fdesigner.ui.jface.util.PropertyChangeEvent;
import org.fdesigner.workbench.commands.ICommandService;
import org.fdesigner.workbench.internal.IPreferenceConstants;
import org.fdesigner.workbench.services.IDisposable;
import org.fdesigner.workbench.services.IServiceLocator;

/**
 * Listens to command executions and shows the keys UI if enabled
 */
public class ShowKeysListener implements IExecutionListener, IPropertyChangeListener, IDisposable {

	private IPreferenceStore preferenceStore;
	private IServiceLocator serviceLocator;
	private ShowKeysUI showKeysUI;

	public ShowKeysListener(IServiceLocator serviceLocator, IPreferenceStore preferenceStore) {
		this.serviceLocator = serviceLocator;
		this.preferenceStore = preferenceStore;
		this.showKeysUI = new ShowKeysUI(serviceLocator, preferenceStore);

		if (isEnabled()) {
			ICommandService cmdService = this.serviceLocator.getService(ICommandService.class);
			cmdService.addExecutionListener(this);
		}
		this.preferenceStore.addPropertyChangeListener(this);
	}

	@Override
	public void dispose() {
		this.preferenceStore.removePropertyChangeListener(this);

		ICommandService cmdService = this.serviceLocator.getService(ICommandService.class);
		if (cmdService != null) {
			cmdService.removeExecutionListener(this);
		}

		showKeysUI.dispose();
	}

	@Override
	public void preExecute(String commandId, ExecutionEvent event) {
		if (!isEnabled() || ShowKeysToggleHandler.COMMAND_ID.equals(commandId)) {
			return;
		}

		// We never know from which thread we are called, so schedule the UI opening in
		// the event loop. Also, this allows having the popup on top of whatever UI is
		// opened right now. E.g. we can now draw on top of the Quick Access UI rather
		// than being hidden underneath it.
		Display.getDefault().asyncExec(() -> showKeysUI.open(commandId, (Event) event.getTrigger()));
	}

	private boolean isEnabled() {
		return this.preferenceStore.getBoolean(IPreferenceConstants.SHOW_KEYS_ENABLED);
	}

	@Override
	public void postExecuteSuccess(String commandId, Object returnValue) {
	}

	@Override
	public void notHandled(String commandId, NotHandledException exception) {
	}

	@Override
	public void postExecuteFailure(String commandId, ExecutionException exception) {
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		String property = event.getProperty();
		if (IPreferenceConstants.SHOW_KEYS_ENABLED.equals(property)) {
			ICommandService cmdService = this.serviceLocator.getService(ICommandService.class);
			if (isEnabled()) {
				cmdService.addExecutionListener(this);
			} else {
				cmdService.removeExecutionListener(this);
			}
		}
	}

}
