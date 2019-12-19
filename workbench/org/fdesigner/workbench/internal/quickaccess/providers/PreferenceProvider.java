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
 *     Lars Vogel <Lars.Vogel@vogella.com> - Bug 472654
 *******************************************************************************/

package org.fdesigner.workbench.internal.quickaccess.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fdesigner.ui.jface.preference.IPreferenceNode;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.activities.WorkbenchActivityHelper;
import org.fdesigner.workbench.internal.IWorkbenchGraphicConstants;
import org.fdesigner.workbench.internal.WorkbenchImages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessMessages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessProvider;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;

/**
 * @since 3.3
 *
 */
public class PreferenceProvider extends QuickAccessProvider {

	private QuickAccessElement[] cachedElements;
	private Map<String, PreferenceElement> idToElement = new HashMap<>();

	@Override
	public String getId() {
		return "org.eclipse.ui.preferences"; //$NON-NLS-1$
	}

	@Override
	public QuickAccessElement findElement(String id, String filterText) {
		getElements();
		return idToElement.get(id);
	}

	@Override
	public QuickAccessElement[] getElements() {
		if (cachedElements == null) {
			List<PreferenceElement> list = new ArrayList<>();
			collectElements("", PlatformUI.getWorkbench().getPreferenceManager().getRootSubNodes(), list); //$NON-NLS-1$
			cachedElements = new PreferenceElement[list.size()];
			for (int i = 0; i < list.size(); i++) {
				PreferenceElement preferenceElement = list.get(i);
				cachedElements[i] = preferenceElement;
				idToElement.put(preferenceElement.getId(), preferenceElement);
			}
		}
		return cachedElements;
	}

	private void collectElements(String prefix, IPreferenceNode[] subNodes, List<PreferenceElement> result) {
		for (IPreferenceNode subNode : subNodes) {
			if (!WorkbenchActivityHelper.filterItem(subNode)) {
				PreferenceElement preferenceElement = new PreferenceElement(subNode, prefix);
				result.add(preferenceElement);
				String nestedPrefix = prefix.length() == 0 ? subNode.getLabelText() : (prefix + "/" + subNode.getLabelText()); //$NON-NLS-1$
				collectElements(nestedPrefix, subNode.getSubNodes(), result);
			}
		}
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_OBJ_NODE);
	}

	@Override
	public String getName() {
		return QuickAccessMessages.QuickAccess_Preferences;
	}

	@Override
	protected void doReset() {
		cachedElements = null;
		idToElement.clear();
	}

	@Override
	public boolean requiresUiAccess() {
		return true; // workbenchActivitySupport.getActivityManager() seems to require UI Thread
	}
}
