/*******************************************************************************
 * Copyright (c) 2014-2016 Red Hat Inc., and others
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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.fdesigner.ide.internal.ide.IDEWorkbenchPlugin;
import org.fdesigner.ide.wizards.datatransfer.ProjectConfigurator;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IFolder;
import org.fdesigner.resources.IProject;
import org.fdesigner.resources.IProjectDescription;
import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IPath;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.Path;

/**
 * A {@link ProjectConfigurator} that detects Eclipse projects (folder with
 * .project)
 *
 * @since 3.12
 *
 */
public class EclipseProjectConfigurator implements ProjectConfigurator {

	@Override
	public Set<File> findConfigurableLocations(File root, IProgressMonitor monitor) {
		Set<File> projectFiles = new LinkedHashSet<>();
		Set<String> visitedDirectories = new HashSet<>();
		WizardProjectsImportPage.collectProjectFilesFromDirectory(projectFiles, root, visitedDirectories, true,
				monitor);
		Set<File> res = new LinkedHashSet<>();
		for (File projectFile : projectFiles) {
			res.add(projectFile.getParentFile());
		}
		return res;
	}

	@Override
	public boolean shouldBeAnEclipseProject(IContainer container, IProgressMonitor monitor) {
		return container.getFile(new Path(IProjectDescription.DESCRIPTION_FILE_NAME)).exists();
	}

	@Override
	public Set<IFolder> getFoldersToIgnore(IProject project, IProgressMonitor monitor) {
		return null;
	}

	@Override
	public boolean canConfigure(IProject project, Set<IPath> ignoredPaths, IProgressMonitor monitor) {
		return true;
	}

	@Override
	public void removeDirtyDirectories(Map<File, List<ProjectConfigurator>> proposals) {
		// nothing to do: we cannot infer that a directory is dirty from
		// .project
	}

	@Override
	public void configure(IProject project, Set<IPath> excludedDirectories, IProgressMonitor monitor) {
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
		} catch (CoreException ex) {
			IDEWorkbenchPlugin.log(ex.getMessage(), ex);
		}
	}

}
