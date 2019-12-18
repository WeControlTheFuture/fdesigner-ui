/*******************************************************************************
 * Copyright (c) 2003, 2015 IBM Corporation and others.
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
package org.fdesigner.workbench.internal;

import java.util.HashMap;
import java.util.Map;

import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.ui.jface.window.WindowManager;
import org.fdesigner.workbench.IMemento;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.WorkbenchException;
import org.fdesigner.workbench.application.IWorkbenchConfigurer;
import org.fdesigner.workbench.application.IWorkbenchWindowConfigurer;

/**
 * Internal class providing special access for configuring the workbench.
 * <p>
 * Note that these objects are only available to the main application (the
 * plug-in that creates and owns the workbench).
 * </p>
 * <p>
 * This class is not intended to be instantiated or subclassed by clients.
 * </p>
 *
 * @since 3.0
 */
public final class WorkbenchConfigurer implements IWorkbenchConfigurer {

	/**
	 * Table to hold arbitrary key-data settings (key type: <code>String</code>,
	 * value type: <code>Object</code>).
	 *
	 * @see #setData
	 */
	private Map extraData = new HashMap();

	/**
	 * Indicates whether workbench state should be saved on close and restored on
	 * subsequent open.
	 */
	private boolean saveAndRestore = false;

	/**
	 * Indicates whether the workbench is being force to close. During an emergency
	 * close, no interaction with the user should be done.
	 */
	private boolean isEmergencyClosing = false;

	/**
	 * Indicates the behaviour when the last window is closed. If <code>true</code>,
	 * the workbench will exit (saving the last window's state, if configured to do
	 * so). If <code>false</code> the window will be closed, leaving the workbench
	 * running.
	 *
	 * @since 3.1
	 */
	private boolean exitOnLastWindowClose = true;

	/**
	 * Creates a new workbench configurer.
	 * <p>
	 * This method is declared package-private. Clients are passed an instance only
	 * via {@link WorkbenchAdvisor#initialize WorkbenchAdvisor.initialize}
	 * </p>
	 */
	/* package */ WorkbenchConfigurer() {
		super();
	}

	@Override
	public IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	@Override
	public WindowManager getWorkbenchWindowManager() {
		// return the global workbench window manager
		return null;
	}

	@Override
	public void declareImage(String symbolicName, ImageDescriptor descriptor, boolean shared) {
		if (symbolicName == null || descriptor == null) {
			throw new IllegalArgumentException();
		}
		WorkbenchImages.declareImage(symbolicName, descriptor, shared);
	}

	@Override
	public IWorkbenchWindowConfigurer getWindowConfigurer(IWorkbenchWindow window) {
		if (window == null) {
			throw new IllegalArgumentException();
		}
		return ((WorkbenchWindow) window).getWindowConfigurer();
	}

	@Override
	public boolean getSaveAndRestore() {
		return saveAndRestore;
	}

	@Override
	public void setSaveAndRestore(boolean enabled) {
		saveAndRestore = enabled;
	}

	@Override
	public Object getData(String key) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		return extraData.get(key);
	}

	@Override
	public void setData(String key, Object data) {
		if (key == null) {
			throw new IllegalArgumentException();
		}
		if (data != null) {
			extraData.put(key, data);
		} else {
			extraData.remove(key);
		}
	}

	@Override
	public void emergencyClose() {
		if (!isEmergencyClosing) {
			isEmergencyClosing = true;
			if (Workbench.getInstance() != null && !Workbench.getInstance().isClosing()) {
				Workbench.getInstance().close(PlatformUI.RETURN_EMERGENCY_CLOSE, true);
			}
		}

	}

	@Override
	public boolean emergencyClosing() {
		return isEmergencyClosing;
	}

	@Override
	public IStatus restoreState() {
		return Status.OK_STATUS;
	}

	@Override
	public void openFirstTimeWindow() {
		((Workbench) getWorkbench()).openFirstTimeWindow();
	}

	@Override
	public IWorkbenchWindowConfigurer restoreWorkbenchWindow(IMemento memento) throws WorkbenchException {
		return getWindowConfigurer(null);
	}

	@Override
	public boolean getExitOnLastWindowClose() {
		return exitOnLastWindowClose;
	}

	@Override
	public void setExitOnLastWindowClose(boolean enabled) {
		exitOnLastWindowClose = enabled;
	}
}
