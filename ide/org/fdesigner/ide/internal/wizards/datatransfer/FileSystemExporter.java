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
 *     Taimoor Mirza <taimoor.mrza@gmail.com> - Bug 549486
 *******************************************************************************/
package org.fdesigner.ide.internal.wizards.datatransfer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.StandardCopyOption;

import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IFile;
import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IPath;

/**
 * Helper class for exporting resources to the file system.
 */
public class FileSystemExporter {
	/**
	 *  Creates the specified file system directory at <code>destinationPath</code>.
	 *  This creates a new file system directory.
	 *
	 *  @param destinationPath location to which files will be written
	 */
	public void createFolder(IPath destinationPath) {
		new File(destinationPath.toOSString()).mkdir();
	}

	/**
	 *  Writes the passed resource to the specified location recursively.
	 *
	 *  @param resource the resource to write out to the file system
	 *  @param destinationPath location where the resource will be written
	 *  @exception CoreException if the operation fails
	 *  @exception IOException if an I/O error occurs when writing files
	 */
	public void write(IResource resource, IPath destinationPath)
			throws CoreException, IOException {
		if (resource.getType() == IResource.FILE) {
			writeFile((IFile) resource, destinationPath);
		} else {
			writeChildren((IContainer) resource, destinationPath);
		}
	}

	/**
	 *  Exports the passed container's children
	 */
	protected void writeChildren(IContainer folder, IPath destinationPath)
			throws CoreException, IOException {
		if (folder.isAccessible()) {
			for (IResource child : folder.members()) {
				writeResource(child, destinationPath.append(child.getName()));
			}
		}
	}

	/**
	 *  Writes the passed file resource to the specified destination on the local
	 *  file system
	 */
	protected void writeFile(IFile file, IPath destinationPath)
			throws IOException {
		Files.copy(file.getLocation().toFile().toPath(), destinationPath.toFile().toPath(),
				StandardCopyOption.COPY_ATTRIBUTES, LinkOption.NOFOLLOW_LINKS);
	}

	/**
	 *  Writes the passed resource to the specified location recursively
	 */
	protected void writeResource(IResource resource, IPath destinationPath)
			throws CoreException, IOException {
		if (resource.getType() == IResource.FILE) {
			writeFile((IFile) resource, destinationPath);
		} else {
			createFolder(destinationPath);
			writeChildren((IContainer) resource, destinationPath);
		}
	}
}
