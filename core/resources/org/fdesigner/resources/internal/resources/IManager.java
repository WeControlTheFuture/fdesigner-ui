/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
package org.fdesigner.resources.internal.resources;

import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;

public interface IManager {
	void shutdown(IProgressMonitor monitor) throws CoreException;

	void startup(IProgressMonitor monitor) throws CoreException;
}
