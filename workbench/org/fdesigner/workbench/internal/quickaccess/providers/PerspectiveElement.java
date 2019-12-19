/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.quickaccess.providers;

import java.util.Objects;

import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.IPerspectiveDescriptor;
import org.fdesigner.workbench.IWorkbench;
import org.fdesigner.workbench.IWorkbenchPage;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.WorkbenchException;
import org.fdesigner.workbench.internal.Workbench;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;
import org.fdesigner.workbench.statushandlers.StatusManager;

/**
 * @since 3.3
 *
 */
public class PerspectiveElement extends QuickAccessElement {

	private final IPerspectiveDescriptor descriptor;

	/* package */ PerspectiveElement(IPerspectiveDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public void execute() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = window.getActivePage();
		if (activePage != null) {
			activePage.setPerspective(descriptor);
		} else {
			try {
				window.openPage(descriptor.getId(), ((Workbench) workbench).getDefaultPageInput());
			} catch (WorkbenchException e) {
				IStatus errorStatus = WorkbenchPlugin
						.newError(NLS.bind(WorkbenchMessages.Workbench_showPerspectiveError, descriptor.getLabel()), e);
				StatusManager.getManager().handle(errorStatus, StatusManager.SHOW);
			}
		}
	}

	@Override
	public String getId() {
		return descriptor.getId();
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return descriptor.getImageDescriptor();
	}

	@Override
	public String getLabel() {
		String label = descriptor.getLabel();
		String description = descriptor.getDescription();
		if (description != null && !description.isEmpty()) {
			return label + separator + description;
		}
		return label;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(descriptor);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final PerspectiveElement other = (PerspectiveElement) obj;
		return Objects.equals(descriptor, other.descriptor);
	}
}
