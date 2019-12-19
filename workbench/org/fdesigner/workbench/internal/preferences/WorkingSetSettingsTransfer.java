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
 ******************************************************************************/

package org.fdesigner.workbench.internal.preferences;

import java.io.File;
import java.io.IOException;

import org.fdesigner.runtime.common.runtime.IPath;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.workbench.IWorkingSetManager;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.AbstractWorkingSetManager;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.WorkingSetManager;

/**
 * The WorkingSetSettingsTransfer is the settings transfer for the workbench
 * working sets.
 *
 * @since 3.3
 *
 */
public class WorkingSetSettingsTransfer extends WorkbenchSettingsTransfer {

	@Override
	public String getName() {
		return WorkbenchMessages.WorkingSets_Name;
	}

	@Override
	public IStatus transferSettings(IPath newWorkspaceRoot) {
		IPath dataLocation = getNewWorkbenchStateLocation(newWorkspaceRoot);

		if (dataLocation == null)
			return noWorkingSettingsStatus();

		dataLocation = dataLocation.append(WorkingSetManager.WORKING_SET_STATE_FILENAME);

		File stateFile = new File(dataLocation.toOSString());

		try {
			IWorkingSetManager manager = PlatformUI.getWorkbench().getWorkingSetManager();
			if (manager instanceof AbstractWorkingSetManager)
				((AbstractWorkingSetManager) manager).saveState(stateFile);
			else
				return new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH,
						WorkbenchMessages.WorkingSets_CannotSave);
		} catch (IOException e) {
			new Status(IStatus.ERROR, WorkbenchPlugin.PI_WORKBENCH,
					WorkbenchMessages.ProblemSavingWorkingSetState_message, e);
		}
		return Status.OK_STATUS;

	}
}
