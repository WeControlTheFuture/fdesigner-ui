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
 *     Jan-Hendrik Diederich, Bredex GmbH - bug 201052
 *******************************************************************************/
package org.fdesigner.workbench.internal.dialogs;

import java.util.Collection;
import java.util.Iterator;

import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.core.Platform;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;
import org.fdesigner.runtime.registry.runtime.IExtension;
import org.fdesigner.runtime.registry.runtime.IExtensionPoint;
import org.fdesigner.runtime.registry.runtime.dynamichelpers.ExtensionTracker;
import org.fdesigner.runtime.registry.runtime.dynamichelpers.IExtensionChangeHandler;
import org.fdesigner.runtime.registry.runtime.dynamichelpers.IExtensionTracker;
import org.fdesigner.ui.jface.preference.IPreferenceNode;
import org.fdesigner.ui.jface.preference.PreferenceManager;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.WorkbenchPlugin;
import org.fdesigner.workbench.internal.misc.StatusUtil;
import org.fdesigner.workbench.internal.preferences.WorkbenchPreferenceExpressionNode;
import org.fdesigner.workbench.internal.registry.IWorkbenchRegistryConstants;
import org.fdesigner.workbench.internal.registry.PreferencePageRegistryReader;

/**
 * The WorkbenchPreferenceManager is the manager that can handle categories and
 * preference nodes.
 */
public class WorkbenchPreferenceManager extends PreferenceManager implements IExtensionChangeHandler {

	/**
	 * Create a new instance of the receiver with the specified seperatorChar
	 *
	 * @param separatorChar
	 */
	public WorkbenchPreferenceManager(char separatorChar) {
		super(separatorChar, new WorkbenchPreferenceExpressionNode("")); //$NON-NLS-1$

		IExtensionTracker tracker = PlatformUI.getWorkbench().getExtensionTracker();
		tracker.registerHandler(this, ExtensionTracker.createExtensionPointFilter(getExtensionPointFilter()));

		// add a listener for keyword deltas. If any occur clear all page caches
		Platform.getExtensionRegistry().addRegistryChangeListener(event -> {
			if (event.getExtensionDeltas(PlatformUI.PLUGIN_ID, IWorkbenchRegistryConstants.PL_KEYWORDS).length > 0) {
				for (Object element : getElements(PreferenceManager.POST_ORDER)) {
					((WorkbenchPreferenceNode) element).clearKeywords();
				}
			}
		});
	}

	/**
	 * Add the pages and the groups to the receiver.
	 *
	 * @param pageContributions
	 */
	public void addPages(Collection pageContributions) {

		// Add the contributions to the manager
		Iterator iterator = pageContributions.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			if (next instanceof WorkbenchPreferenceNode) {
				WorkbenchPreferenceNode wNode = (WorkbenchPreferenceNode) next;
				addToRoot(wNode);
				registerNode(wNode);
			}
		}

	}

	/**
	 * Register a node with the extension tracker.
	 *
	 * @param node register the given node and its subnodes with the extension
	 *             tracker
	 */
	private void registerNode(WorkbenchPreferenceNode node) {
		PlatformUI.getWorkbench().getExtensionTracker().registerObject(
				node.getConfigurationElement().getDeclaringExtension(), node, IExtensionTracker.REF_WEAK);
		for (IPreferenceNode subNode : node.getSubNodes()) {
			registerNode((WorkbenchPreferenceNode) subNode);
		}

	}

	@Override
	public void addExtension(IExtensionTracker tracker, IExtension extension) {
		for (IConfigurationElement configElement : extension.getConfigurationElements()) {
			WorkbenchPreferenceNode node = PreferencePageRegistryReader.createNode(configElement);
			if (node == null) {
				continue;
			}
			registerNode(node);
			String category = node.getCategory();
			if (category == null) {
				addToRoot(node);
			} else {
				IPreferenceNode parent = null;
				for (IPreferenceNode element : getElements(PreferenceManager.POST_ORDER)) {
					if (category.equals(element.getId())) {
						parent = element;
						break;
					}
				}
				if (parent == null) {
					// Could not find the parent - log
					String message = "Invalid preference category path: " + category + " (bundle: " + node.getPluginId() //$NON-NLS-1$ //$NON-NLS-2$
							+ ", page: " + node.getId() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
					WorkbenchPlugin.log(StatusUtil.newStatus(IStatus.WARNING, message, null));
					addToRoot(node);
				} else {
					parent.add(node);
				}
			}
		}
	}

	private IExtensionPoint getExtensionPointFilter() {
		return Platform.getExtensionRegistry().getExtensionPoint(PlatformUI.PLUGIN_ID,
				IWorkbenchRegistryConstants.PL_PREFERENCES);
	}

	@Override
	public void removeExtension(IExtension extension, Object[] objects) {
		for (Object object : objects) {
			if (object instanceof IPreferenceNode) {
				IPreferenceNode wNode = (IPreferenceNode) object;
				wNode.disposeResources();
				deepRemove(getRoot(), wNode);
			}
		}
	}

	/**
	 * Removes the node from the manager, searching through all subnodes.
	 *
	 * @param parent       the node to search
	 * @param nodeToRemove the node to remove
	 * @return whether the node was removed
	 */
	private boolean deepRemove(IPreferenceNode parent, IPreferenceNode nodeToRemove) {
		if (parent == nodeToRemove) {
			if (parent == getRoot()) {
				removeAll(); // we're removing the root
				return true;
			}
		}

		if (parent.remove(nodeToRemove)) {
			return true;
		}

		for (IPreferenceNode subNode : parent.getSubNodes()) {
			if (deepRemove(subNode, nodeToRemove)) {
				return true;
			}
		}
		return false;
	}
}
