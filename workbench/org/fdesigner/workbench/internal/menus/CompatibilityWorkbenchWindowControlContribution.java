/*******************************************************************************
 * Copyright (c) 2010, 2015 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.menus;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.fdesigner.e4.ui.model.application.ui.MUIElement;
import org.fdesigner.e4.ui.model.application.ui.basic.MTrimBar;
import org.fdesigner.e4.ui.model.application.ui.basic.MWindow;
import org.fdesigner.e4.ui.model.application.ui.menu.MToolControl;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.workbench.IWorkbenchWindow;
import org.fdesigner.workbench.internal.registry.IWorkbenchRegistryConstants;
import org.fdesigner.workbench.internal.util.Util;
import org.fdesigner.workbench.menus.IWorkbenchContribution;
import org.fdesigner.workbench.menus.WorkbenchWindowControlContribution;

/**
 * This is a proxy object for instantiating the real as provided by a plug-in
 * WorkbenchWindowControlContribution subclass.
 */
public class CompatibilityWorkbenchWindowControlContribution {

	public static final String CONTROL_CONTRIBUTION_URI = "bundleclass://org.eclipse.ui.workbench/org.eclipse.ui.internal.menus.CompatibilityWorkbenchWindowControlContribution"; //$NON-NLS-1$

	private WorkbenchWindowControlContribution contribution;

	/**
	 * Constructs the control contribution that this proxy represents.
	 *
	 * @param window      the window that this control contribution is under
	 * @param toolControl the tool control representing this contribution
	 * @param composite   the composite to create or parent the control under
	 */
	@PostConstruct
	void construct(MWindow window, MToolControl toolControl, Composite composite) {
		IConfigurationElement configurationElement = ControlContributionRegistry.get(toolControl.getElementId());
		if (configurationElement != null) {
			contribution = (WorkbenchWindowControlContribution) Util.safeLoadExecutableExtension(configurationElement,
					IWorkbenchRegistryConstants.ATT_CLASS, WorkbenchWindowControlContribution.class);
			if (contribution != null) {
				IWorkbenchWindow workbenchWindow = window.getContext().get(IWorkbenchWindow.class);
				contribution.setWorkbenchWindow(workbenchWindow);

				if (contribution instanceof IWorkbenchContribution) {
					((IWorkbenchContribution) contribution).initialize(workbenchWindow);
				}

				MUIElement parent = toolControl.getParent();
				while (!(parent instanceof MTrimBar) && parent != null) {
					parent = parent.getParent();
				}

				if (parent != null) {
					switch (((MTrimBar) parent).getSide()) {
					case BOTTOM:
						contribution.setCurSide(SWT.BOTTOM);
						break;
					case LEFT:
						contribution.setCurSide(SWT.LEFT);
						break;
					case RIGHT:
						contribution.setCurSide(SWT.RIGHT);
						break;
					case TOP:
						contribution.setCurSide(SWT.TOP);
						break;
					}
				} else {
					// default position
					contribution.setCurSide(SWT.TOP);
				}

				contribution.delegateCreateControl(composite);
			}
		}
	}

	@PreDestroy
	void dispose() {
		if (contribution != null) {
			contribution.dispose();
			contribution = null;
		}
	}

}
