/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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

import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.workbench.IWorkbenchPreferencePage;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.WorkbenchMessages;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.misc.StatusUtil;
import org.fdesigner.workbench.internal.preferences.WorkbenchPreferenceExtensionNode;
import org.fdesigner.workbench.internal.registry.CategorizedPageRegistryReader;
import org.fdesigner.workbench.internal.registry.IWorkbenchRegistryConstants;
import org.fdesigner.workbench.statushandlers.StatusManager;

/**
 * A proxy for a preference page to avoid creation of preference page just to
 * show a node in the preference dialog tree.
 */
public class WorkbenchPreferenceNode extends WorkbenchPreferenceExtensionNode {

	/**
	 * Create a new instance of the receiver.
	 *
	 * @param nodeId
	 * @param element
	 */
	public WorkbenchPreferenceNode(String nodeId, IConfigurationElement element) {
		super(nodeId, element);
	}

	/**
	 * Creates the preference page this node stands for.
	 */
	@Override
	public void createPage() {
		IWorkbenchPreferencePage page;
		try {
			page = (IWorkbenchPreferencePage) WorkbenchPlugin.createExtension(getConfigurationElement(),
					IWorkbenchRegistryConstants.ATT_CLASS);
		} catch (CoreException e) {
			// Just inform the user about the error. The details are
			// written to the log by now.
			IStatus errStatus = StatusUtil.newStatus(e.getStatus(), WorkbenchMessages.PreferenceNode_errorMessage);
			StatusManager.getManager().handle(errStatus, StatusManager.SHOW | StatusManager.LOG);
			page = new ErrorPreferencePage();
		}

		page.init(PlatformUI.getWorkbench());
		if (getLabelImage() != null) {
			page.setImageDescriptor(getImageDescriptor());
		}
		page.setTitle(getLabelText());
		setPage(page);
	}

	/**
	 * Return the category name for the node.
	 *
	 * @return java.lang.String
	 */
	public String getCategory() {
		return getConfigurationElement().getAttribute(CategorizedPageRegistryReader.ATT_CATEGORY);
	}
}
