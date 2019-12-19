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

import org.fdesigner.supplement.util.NLS;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.PlatformUI;
import org.fdesigner.workbench.internal.IWorkbenchGraphicConstants;
import org.fdesigner.workbench.internal.WorkbenchImages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessMessages;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;

/**
 * "Search X in help" element shown at the end of the list.
 */
public class HelpSearchElement extends QuickAccessElement {

	/** identifier */
	private static final String SEARCH_IN_HELP_ID = "search.in.help"; //$NON-NLS-1$

	private String searchText;

	public HelpSearchElement(String filter) {
		this.searchText = filter;
	}

	@Override
	public String getLabel() {
		return NLS.bind(QuickAccessMessages.QuickAccessContents_SearchInHelpLabel, searchText);
	}

	@Override
	public String getId() {
		return SEARCH_IN_HELP_ID;
	}

	@Override
	public void execute() {
		PlatformUI.getWorkbench().getHelpSystem().search(searchText);
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_HELP_SEARCH);
	}

}