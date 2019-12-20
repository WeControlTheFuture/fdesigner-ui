/*******************************************************************************
 * Copyright (c) 2008, 2014 Freescale Semiconductor and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Serge Beauchamp (Freescale Semiconductor) - [252996] initial API and implementation
 *     IBM Corporation - ongoing implementation
 *******************************************************************************/
package org.fdesigner.ide.internal.ide.misc;

import org.fdesigner.filesystem.IFileInfo;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.filtermatchers.CompoundFileInfoMatcher;
import org.fdesigner.runtime.common.runtime.CoreException;

/**
 * A Resource Filter Type Factory for supporting the AND logical preposition
 */
public class AndFileInfoMatcher extends CompoundFileInfoMatcher {

	@Override
	public boolean matches(IContainer parent, IFileInfo fileInfo) throws CoreException {
		for (int i = 0; i < matchers.length; i++) {
			if (!matchers[i].matches(parent, fileInfo))
				return false;
		}
		return true;
	}
}
