/*******************************************************************************
 * Copyright (c) 2008, 2014 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Serge Beauchamp (Freescale Semiconductor) - initial API and implementation
 *     IBM Corporation - ongoing development
 *******************************************************************************/
package org.fdesigner.resources.internal.resources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.fdesigner.filesystem.IFileInfo;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IProject;
import org.fdesigner.resources.ResourcesPlugin;
import org.fdesigner.resources.filtermatchers.AbstractFileInfoMatcher;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.runtime.core.Platform;

/**
 * A Filter provider for Java Regular expression supported by
 * java.util.regex.Pattern.
 */
public class RegexFileInfoMatcher extends AbstractFileInfoMatcher {

	Pattern pattern = null;

	public RegexFileInfoMatcher() {
		// nothing to do
	}

	@Override
	public boolean matches(IContainer parent, IFileInfo fileInfo) {
		if (pattern != null) {
			Matcher m = pattern.matcher(fileInfo.getName());
			return m.matches();
		}
		return false;
	}

	@Override
	public void initialize(IProject project, Object arguments) throws CoreException {
		if (arguments != null) {
			try {
				pattern = Pattern.compile((String) arguments);
			} catch (PatternSyntaxException e) {
				throw new CoreException(new Status(IStatus.ERROR, ResourcesPlugin.PI_RESOURCES, Platform.PLUGIN_ERROR, e.getMessage(), e));
			}
		}
	}
}