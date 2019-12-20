/*******************************************************************************
 * Copyright (c) 2008, 2015 Freescale Semiconductor and others.
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
package org.fdesigner.ide.internal.ide.dialogs;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.fdesigner.ide.internal.ide.IIDEHelpContextIds;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.dialogs.PropertyPage;

/**
 * The ResourceInfoPage is the page that shows the basic info about the
 * resource.
 */
public class ResourceFilterPage extends PropertyPage {

	ResourceFilterGroup groupWidget;

	/**
	 *
	 */
	public ResourceFilterPage() {
		groupWidget = new ResourceFilterGroup();
	}

	@Override
	protected Control createContents(Composite parent) {

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(),
				IIDEHelpContextIds.RESOURCE_FILTER_PROPERTY_PAGE);

		IResource resource = Adapters.adapt(getElement(), IResource.class);
		IContainer container = resource instanceof IContainer ? (IContainer) resource
				: null;
		groupWidget.setContainer(container);

		return groupWidget.createContents(parent);
	}

	@Override
	protected void performDefaults() {
		groupWidget.performDefaults();
	}

	@Override
	public void dispose() {
		groupWidget.dispose();
		super.dispose();
	}

	/**
	 * Apply the read only state and the encoding to the resource.
	 */
	@Override
	public boolean performOk() {
		return groupWidget.performOk();
	}
}
