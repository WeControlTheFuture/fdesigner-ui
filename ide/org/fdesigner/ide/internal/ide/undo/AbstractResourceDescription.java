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

package org.fdesigner.ide.internal.ide.undo;

import org.fdesigner.ide.undo.ResourceDescription;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IMarker;
import org.fdesigner.resources.IResource;
import org.fdesigner.resources.IWorkspace;
import org.fdesigner.resources.ResourceAttributes;
import org.fdesigner.resources.ResourcesPlugin;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;

/**
 * Base implementation of ResourceDescription that describes the common
 * attributes of a resource to be created.
 *
 * This class is not intended to be instantiated or used by clients.
 *
 * @since 3.3
 *
 */
abstract class AbstractResourceDescription extends ResourceDescription {
	IContainer parent;

	long modificationStamp = IResource.NULL_STAMP;

	long localTimeStamp = IResource.NULL_STAMP;

	ResourceAttributes resourceAttributes;

	MarkerDescription[] markerDescriptions;

	/**
	 * Create a resource description with no initial attributes
	 */
	protected AbstractResourceDescription() {
		super();
	}

	/**
	 * Create a resource description from the specified resource.
	 *
	 * @param resource
	 *            the resource to be described
	 */
	protected AbstractResourceDescription(IResource resource) {
		super();
		parent = resource.getParent();
		if (resource.isAccessible()) {
			modificationStamp = resource.getModificationStamp();
			localTimeStamp = resource.getLocalTimeStamp();
			resourceAttributes = resource.getResourceAttributes();
			try {
				IMarker[] markers = resource.findMarkers(null, true,
						IResource.DEPTH_INFINITE);
				markerDescriptions = new MarkerDescription[markers.length];
				for (int i = 0; i < markers.length; i++) {
					markerDescriptions[i] = new MarkerDescription(markers[i]);
				}
			} catch (CoreException e) {
				// Eat this exception because it only occurs when the resource
				// does not exist and we have already checked this.
				// We do not want to throw exceptions on the simple constructor,
				// as no one has actually tried to do anything yet.
			}
		}
	}

	@Override
	public IResource createResource(IProgressMonitor monitor)
			throws CoreException {
		IResource resource = createResourceHandle();
		createExistentResourceFromHandle(resource, monitor);
		restoreResourceAttributes(resource);
		return resource;
	}

	@Override
	public boolean isValid() {
		return parent == null || parent.exists();
	}

	/**
	 * Restore any saved attributed of the specified resource. This method is
	 * called after the existent resource represented by the receiver has been
	 * created.
	 *
	 * @param resource
	 *            the newly created resource
	 * @throws CoreException
	 */
	protected void restoreResourceAttributes(IResource resource)
			throws CoreException {
		if (modificationStamp != IResource.NULL_STAMP) {
			resource.revertModificationStamp(modificationStamp);
		}
		if (localTimeStamp != IResource.NULL_STAMP) {
			resource.setLocalTimeStamp(localTimeStamp);
		}
		if (resourceAttributes != null) {
			resource.setResourceAttributes(resourceAttributes);
		}
		if (markerDescriptions != null) {
			for (MarkerDescription markerDescription : markerDescriptions) {
				if (markerDescription.resource.exists())
					markerDescription.createMarker();
			}
		}
	}

	/*
	 * Return the workspace.
	 */
	IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

	@Override
	public boolean verifyExistence(boolean checkMembers) {
		IContainer p = parent;
		if (p == null) {
			p = getWorkspace().getRoot();
		}
		IResource handle = p.findMember(getName());
		return handle != null;
	}
}
