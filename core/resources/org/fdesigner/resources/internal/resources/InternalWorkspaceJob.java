/*******************************************************************************
 * Copyright (c) 2003, 2014 IBM Corporation and others.
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
package org.fdesigner.resources.internal.resources;

import org.fdesigner.resources.ResourcesPlugin;
import org.fdesigner.resources.internal.utils.Policy;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.OperationCanceledException;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.runtime.jobs.runtime.jobs.Job;

/**
 * Batches the activity of a job as a single operation, without obtaining the workspace
 * lock.
 */
public abstract class InternalWorkspaceJob extends Job {
	private Workspace workspace;

	public InternalWorkspaceJob(String name) {
		super(name);
		this.workspace = (Workspace) ResourcesPlugin.getWorkspace();
	}

	@Override
	public final IStatus run(IProgressMonitor monitor) {
		monitor = Policy.monitorFor(monitor);
		try {
			int depth = -1;
			try {
				workspace.prepareOperation(null, monitor);
				workspace.beginOperation(true);
				depth = workspace.getWorkManager().beginUnprotected();
				return runInWorkspace(monitor);
			} catch (OperationCanceledException e) {
				workspace.getWorkManager().operationCanceled();
				return Status.CANCEL_STATUS;
			} finally {
				if (depth >= 0)
					workspace.getWorkManager().endUnprotected(depth);
				workspace.endOperation(null, false);
			}
		} catch (CoreException e) {
			return e.getStatus();
		}
	}

	protected abstract IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException;
}
