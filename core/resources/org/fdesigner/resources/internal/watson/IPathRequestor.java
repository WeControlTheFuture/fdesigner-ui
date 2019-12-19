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
package org.fdesigner.resources.internal.watson;

import org.fdesigner.runtime.common.runtime.IPath;

/**
 * Callback interface so visitors can request the path of the object they
 * are visiting. This avoids creating paths when they are not needed.
 */
public interface IPathRequestor {
	IPath requestPath();

	String requestName();
}
