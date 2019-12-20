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

package org.fdesigner.ide.undo;

import java.net.URI;

import org.fdesigner.filesystem.URIUtil;
import org.fdesigner.ide.internal.ide.undo.UndoMessages;
import org.fdesigner.resources.IProject;
import org.fdesigner.resources.IProjectDescription;
import org.fdesigner.resources.IResource;
import org.fdesigner.resources.mapping.IResourceChangeDescriptionFactory;
import org.fdesigner.runtime.common.runtime.Assert;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IAdaptable;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.runtime.core.Platform;

/**
 * A MoveProjectOperation represents an undoable operation for moving a
 * project's content to a different location. Clients may call the public API
 * from a background thread.
 * <p>
 * This class is intended to be instantiated and used by clients. It is not
 * intended to be subclassed by clients.
 * </p>
 *
 * @noextend This class is not intended to be subclassed by clients.
 * @since 3.3
 *
 */
public class MoveProjectOperation extends AbstractCopyOrMoveResourcesOperation {

	private URI projectLocation;

	/**
	 * Create a MoveProjectOperation that moves the specified project contents
	 * to a new location.
	 *
	 * @param project
	 *            the project to be moved
	 * @param location
	 *            the location for the project
	 * @param label
	 *            the label of the operation
	 */
	public MoveProjectOperation(IProject project, URI location, String label) {
		super(new IResource[] { project }, label);
		Assert.isLegal(project != null);
		if (URIUtil.toPath(location).equals(Platform.getLocation())) {
			projectLocation = null;
		} else {
			projectLocation = location;
		}
	}

	@Override
	protected boolean updateResourceChangeDescriptionFactory(
			IResourceChangeDescriptionFactory factory, int operation) {
		// A change of project location only is not of interest to
		// model providers, so treat it as if nothing is happening.
		return false;
	}

	/*
	 * Get the project that this operation is moving.
	 */
	private IProject getProject() {
		return (IProject) resources[0];
	}

	@Override
	protected boolean isDestinationPathValid(IResource resource, int index) {
		// path has already been validated in #computeMoveOrCopyStatus()
		return true;
	}

	@Override
	protected String getProposedName(IResource resource, int index) {
		return getProject().getName();
	}

	/*
	 * Checks that the specified project location is valid in addition to
	 * superclass checks.
	 */
	@Override
	protected IStatus computeMoveOrCopyStatus() {
		IStatus status = Status.OK_STATUS;
		if (projectLocation != null) {
			status = getWorkspace().validateProjectLocationURI(getProject(),
					projectLocation);
		}
		if (status.isOK()) {
			return super.computeMoveOrCopyStatus();
		}
		return status;
	}

	/*
	 * Map execute to moving the project
	 */
	@Override
	protected void doExecute(IProgressMonitor monitor, IAdaptable uiInfo)
			throws CoreException {
		projectLocation = moveProject(getProject(), projectLocation, monitor);
		// nothing was overwritten
		setResourceDescriptions(new ResourceDescription[0]);
	}

	@Override
	protected void doUndo(IProgressMonitor monitor, IAdaptable uiInfo)
			throws CoreException {
		doExecute(monitor, uiInfo);
	}

	/*
	 * Move the project to its new location, returning its previous location.
	 */
	URI moveProject(IProject project, URI locationURI, IProgressMonitor monitor)
			throws CoreException {
		monitor
				.setTaskName(UndoMessages.AbstractCopyOrMoveResourcesOperation_moveProjectProgress);

		IProjectDescription description = project.getDescription();
		// Record the original path so this can be undone
		URI newDestinationURI = description.getLocationURI();
		// Set the new location into the project's description
		description.setLocationURI(locationURI);

		project.move(description, IResource.FORCE | IResource.SHALLOW, monitor);

		// Now adjust the projectLocation so this can be undone/redone.
		return newDestinationURI;
	}

	/*
	 * Map undo to move status.
	 */
	@Override
	public IStatus computeUndoableStatus(IProgressMonitor monitor) {
		IStatus status = super.computeUndoableStatus(monitor);
		if (status.isOK()) {
			status = computeMoveOrCopyStatus();
		}
		return status;
	}
}
