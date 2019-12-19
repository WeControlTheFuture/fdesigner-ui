/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM - Initial API and implementation
 *******************************************************************************/
package org.fdesigner.resources.internal.refresh;

import org.fdesigner.resources.IResource;
import org.fdesigner.resources.internal.resources.Workspace;
import org.fdesigner.resources.refresh.IRefreshMonitor;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.SubMonitor;

/**
 * Internal abstract superclass of all refresh providers.  This class must not be
 * subclassed directly by clients.  All refresh providers must subclass the public
 * API class <code>org.eclipse.core.resources.refresh.RefreshProvider</code>.
 *
 * @since 3.0
 */
public class InternalRefreshProvider {
	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.refresh.RefreshProvider#createPollingMonitor(IResource)
	 */
	protected IRefreshMonitor createPollingMonitor(IResource resource) {
		Workspace workspace = (Workspace) resource.getWorkspace();
		RefreshManager refreshManager = workspace.getRefreshManager();
		MonitorManager monitors = refreshManager.monitors;
		PollingMonitor pollingMonitor = monitors.pollMonitor;
		pollingMonitor.monitor(resource);
		return pollingMonitor;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.resources.refresh.RefreshProvider#resetMonitors(IResource)
	 */
	public void resetMonitors(IResource resource, IProgressMonitor progressMonitor) {
		SubMonitor subMonitor = SubMonitor.convert(progressMonitor, 2);
		MonitorManager manager = ((Workspace) resource.getWorkspace()).getRefreshManager().monitors;
		manager.unmonitor(resource, subMonitor.split(1));
		manager.monitor(resource, subMonitor.split(1));
	}
}
