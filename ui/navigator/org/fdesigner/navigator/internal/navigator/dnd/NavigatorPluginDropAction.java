/*******************************************************************************
 * Copyright (c) 2003, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.fdesigner.navigator.internal.navigator.dnd;

import org.fdesigner.ide.views.navigator.LocalSelectionTransfer;
import org.fdesigner.navigator.CommonDropAdapterAssistant;
import org.fdesigner.navigator.INavigatorContentService;
import org.fdesigner.navigator.internal.navigator.Policy;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.workbench.part.IDropActionDelegate;
import org.fdesigner.workbench.part.PluginTransferData;

/**
 *
 *
 * @since 3.2
 *
 */
public class NavigatorPluginDropAction implements IDropActionDelegate {

	private static final String CN_PLUGIN_ACTION_ID = "org.eclipse.ui.navigator.PluginDropAction"; //$NON-NLS-1$

	/**
	 * A default no-args constructor is required by the
	 * <b>org.eclipse.ui.dropAdapters</b> extension point
	 */
	public NavigatorPluginDropAction() {
		super();
	}

	@Override
	public boolean run(Object sourceData, Object target) {

		if (Policy.DEBUG_DND) {
			System.out.println("NavigatorPluginDropAction.run (begin)"); //$NON-NLS-1$
		}

		String sourceViewerId = new String((byte[]) sourceData);

		IStructuredSelection selection = (IStructuredSelection) LocalSelectionTransfer
				.getTransfer().getSelection();

		INavigatorContentService contentService = NavigatorContentServiceTransfer
				.getInstance().findService(sourceViewerId);

		if (contentService == null) {
			return false;
		}
		try {
			CommonDropAdapterAssistant[] assistants = contentService
					.getDnDService().findCommonDropAdapterAssistants(target,
							selection);

			IStatus valid = null;
			for (CommonDropAdapterAssistant assistant : assistants) {
				valid = assistant.validatePluginTransferDrop(selection, target);
				if (valid != null && valid.isOK()) {
					valid = assistant.handlePluginTransferDrop(selection, target);
					return valid != null && valid.isOK();
				}
			}
		} finally {
			NavigatorContentServiceTransfer.getInstance()
					.unregisterContentService(contentService);
		}

		if (Policy.DEBUG_DND) {
			System.out.println("NavigatorPluginDropAction.run (exit)"); //$NON-NLS-1$
		}

		return false;
	}

	/**
	 *
	 * @param aContentService
	 *            The associated content service that is the source of the drag
	 * @return A PluginTransferData properly configured to call the Common
	 *         Navigator's PluginDropAction.
	 */
	public static PluginTransferData createTransferData(
			INavigatorContentService aContentService) {
		NavigatorContentServiceTransfer.getInstance().registerContentService(
				aContentService);
		return new PluginTransferData(CN_PLUGIN_ACTION_ID, aContentService
				.getViewerId().getBytes());
	}

}
