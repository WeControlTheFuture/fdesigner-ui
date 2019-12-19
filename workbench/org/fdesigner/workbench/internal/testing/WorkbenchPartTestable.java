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
 *******************************************************************************/
package org.fdesigner.workbench.internal.testing;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.fdesigner.ui.testing.IWorkbenchPartTestable;
import org.fdesigner.workbench.internal.PartSite;

/**
 * Implementation of {@link IWorkbenchPartTestable}.
 *
 * @since 3.3
 */
public class WorkbenchPartTestable implements IWorkbenchPartTestable {

	private Composite composite;

	/**
	 * Create a new instance of this class based on the provided part.
	 *
	 * @param partSite the part to test
	 */
	public WorkbenchPartTestable(PartSite partSite) {
		Composite paneComposite = (Composite) partSite.getModel().getWidget();
		Control[] paneChildren = paneComposite.getChildren();
		this.composite = ((Composite) paneChildren[0]);
	}

	@Override
	public Composite getControl() {
		return composite;
	}
}
