/*******************************************************************************
 * Copyright (c) 2016 Red Hat Inc., and others
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mickael Istria (Red Hat Inc.) - initial API and implementation
 ******************************************************************************/
package org.fdesigner.ide.internal.wizards.datatransfer;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.fdesigner.ide.wizards.datatransfer.ProjectConfigurator;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IFolder;
import org.fdesigner.resources.IProject;
import org.fdesigner.runtime.common.runtime.IPath;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.Path;

/**
 * Detects Eclipse workspace (folder with .metadata)
 */
public class EclipseWorkspaceConfigurator implements ProjectConfigurator {

	@Override
	public Set<File> findConfigurableLocations(File root, IProgressMonitor monitor) {
		return Collections.emptySet();
	}

	@Override
	public boolean shouldBeAnEclipseProject(IContainer container, IProgressMonitor monitor) {
		return container.getFolder(new Path(".metadata")).exists(); //$NON-NLS-1$
	}

	@Override
	public Set<IFolder> getFoldersToIgnore(IProject project, IProgressMonitor monitor) {
		Set<IFolder> res = new HashSet<>();
		res.add(project.getFolder(new Path(".metadata"))); //$NON-NLS-1$
		return res;
	}

	@Override
	public boolean canConfigure(IProject project, Set<IPath> ignoredPaths, IProgressMonitor monitor) {
		return false;
	}

	@Override
	public void configure(IProject project, Set<IPath> excludedDirectories, IProgressMonitor monitor) {
		// Nothing to do
	}

}
