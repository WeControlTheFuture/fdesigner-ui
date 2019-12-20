/*******************************************************************************
 * Copyright (c) 2015 Red Hat Inc.
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

import org.fdesigner.ide.wizards.datatransfer.ProjectConfigurator;
import org.fdesigner.resources.IProject;
import org.fdesigner.runtime.common.runtime.IPath;

public interface RecursiveImportListener {

	public void projectCreated(IProject project);

	public void projectConfigured(IProject project, ProjectConfigurator configurator);

	public void errorHappened(IPath location, Exception ex);

}
