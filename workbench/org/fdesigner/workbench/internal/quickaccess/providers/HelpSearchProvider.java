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

import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessMessages;
import org.fdesigner.workbench.internal.quickaccess.QuickAccessProvider;
import org.fdesigner.workbench.quickaccess.QuickAccessElement;

/**
 * Provider for the "Search X in help" element. Only used to have a category
 * "Help".
 */
public class HelpSearchProvider extends QuickAccessProvider {
	/** Minumum length to suggest the user to search typed text in the Help */
	public static final int MIN_SEARCH_LENGTH = 3;

	public HelpSearchProvider() {
		super();
	}

	@Override
	public String getName() {
		return QuickAccessMessages.QuickAccessContents_HelpCategory;
	}

	@Override
	public String getId() {
		return "search.help"; //$NON-NLS-1$
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public QuickAccessElement[] getElements() {
		return new QuickAccessElement[0];
	}

	@Override
	public QuickAccessElement[] getElements(String filter, IProgressMonitor monitor) {
		return new QuickAccessElement[] { new HelpSearchElement(filter) };
	}

	@Override
	public QuickAccessElement findElement(String id, String filterText) {
		return null;
	}

	@Override
	protected void doReset() {
	}


}