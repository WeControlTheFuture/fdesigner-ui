/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
package org.fdesigner.ide.internal.ide.model;

import org.fdesigner.ide.IDE;
import org.fdesigner.resources.IFile;
import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.QualifiedName;
import org.fdesigner.runtime.contenttype.runtime.content.IContentType;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.ISharedImages;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.WorkbenchPlugin;

/**
 * An IWorkbenchAdapter that represents IFiles.
 */
public class WorkbenchFile extends WorkbenchResource {

	/**
	 * Constant that is used as the key of a session property on IFile objects
	 * to cache the result of doing a proper content type lookup. This will be
	 * set by the ContentTypeDecorator (if enabled) and used instead of the
	 * "guessed" content type in {@link #getBaseImage(IResource)}.
	 *
	 * @since 3.4
	 */
	public static QualifiedName IMAGE_CACHE_KEY = new QualifiedName(WorkbenchPlugin.PI_WORKBENCH, "WorkbenchFileImage"); //$NON-NLS-1$

	/**
	 *	Answer the appropriate base image to use for the passed resource, optionally
	 *	considering the passed open status as well iff appropriate for the type of
	 *	passed resource
	 */
	@Override
	protected ImageDescriptor getBaseImage(IResource resource) {
		IContentType contentType = null;
		// do we need to worry about checking here?
		if (resource instanceof IFile) {
			IFile file = (IFile)resource;
			// cached images come from ContentTypeDecorator
			ImageDescriptor cached;
			try {
				cached = (ImageDescriptor) file.getSessionProperty(IMAGE_CACHE_KEY);
				if (cached != null) {
					return cached;
				}
			} catch (CoreException e) {
				// ignore - not having a cached image descriptor is not fatal
			}
			contentType = IDE.guessContentType(file);
		}
		// @issue move IDE specific images
		ImageDescriptor image = PlatformUI.getWorkbench().getEditorRegistry()
				.getImageDescriptor(resource.getName(), contentType);
		if (image == null) {
			image = PlatformUI.getWorkbench().getSharedImages()
					.getImageDescriptor(ISharedImages.IMG_OBJ_FILE);
		}
		return image;
	}
}
