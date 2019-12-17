/*******************************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation and others.
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
 ******************************************************************************/

package org.fdesigner.workbench.internal.dialogs;

import java.util.Set;

import org.fdesigner.ui.jface.viewers.Viewer;
import org.fdesigner.ui.jface.viewers.ViewerFilter;
import org.fdesigner.workbench.IWorkingSet;

public class WorkingSetFilter extends ViewerFilter {
	Set workingSetIds;

	public WorkingSetFilter(Set workingSetIds) {
		this.workingSetIds = workingSetIds;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (element instanceof IWorkingSet) {
			IWorkingSet workingSet = (IWorkingSet) element;
			String id = workingSet.getId();
			// if (!workingSet.isVisible())
			// return false;
			if (workingSetIds != null && id != null) {
				return workingSetIds.contains(id);
			}
		}
		return true;
	}
}