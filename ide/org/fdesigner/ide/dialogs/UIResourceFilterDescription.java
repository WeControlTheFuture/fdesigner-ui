/*******************************************************************************
 * Copyright (c) 2009, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Serge Beauchamp (Freescale Semiconductor)- initial API and implementation
 ******************************************************************************/

package org.eclipse.ui.ide.dialogs;

import org.eclipse.core.resources.FileInfoMatcherDescription;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceFilterDescription;
import org.eclipse.core.runtime.IPath;

/**
 * @since 3.6
 */
public abstract class UIResourceFilterDescription {
	/**
	 * @return the filter path
	 */
	abstract public IPath getPath();
	/**
	 * @return the project
	 */
	abstract public IProject getProject();
	/**
	 * @return the filter type
	 */
	abstract public int getType();
	/**
	 * @return the description
	 */
	abstract public FileInfoMatcherDescription getFileInfoMatcherDescription();

	/**
	 * @param iResourceFilterDescription
	 * @return a UIResourceFilterDescription
	 */
	public static UIResourceFilterDescription wrap(
			final IResourceFilterDescription iResourceFilterDescription) {
		return new UIResourceFilterDescription() {
			@Override
			public FileInfoMatcherDescription getFileInfoMatcherDescription() {
				return iResourceFilterDescription.getFileInfoMatcherDescription();
			}
			@Override
			public IPath getPath() {
				return iResourceFilterDescription.getResource().getProjectRelativePath();
			}

			@Override
			public IProject getProject() {
				return iResourceFilterDescription.getResource().getProject();
			}

			@Override
			public int getType() {
				return iResourceFilterDescription.getType();
			}
		};
	}
}