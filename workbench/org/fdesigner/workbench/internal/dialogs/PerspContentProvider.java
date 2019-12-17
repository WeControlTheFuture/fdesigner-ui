/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.dialogs;

import org.fdesigner.ui.jface.viewers.IStructuredContentProvider;
import org.fdesigner.workbench.IPerspectiveRegistry;

public class PerspContentProvider implements IStructuredContentProvider {
	@Override
	public Object[] getElements(Object element) {
		if (element instanceof IPerspectiveRegistry) {
			return ((IPerspectiveRegistry) element).getPerspectives();
		}
		return null;
	}
}
