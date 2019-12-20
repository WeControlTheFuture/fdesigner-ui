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
 ******************************************************************************/

package org.fdesigner.navigator.internal.navigator.extensions;

import org.fdesigner.navigator.ILinkHelper;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;
import org.fdesigner.workbench.IEditorInput;
import org.fdesigner.workbench.IWorkbenchPage;

/**
 * @since 3.2
 *
 */
public class SkeletonLinkHelper implements ILinkHelper {

	/**
	 * The singleton instance.
	 */
	public static final ILinkHelper INSTANCE = new SkeletonLinkHelper();

	private SkeletonLinkHelper() {

	}

	@Override
	public IStructuredSelection findSelection(IEditorInput anInput) {
		return StructuredSelection.EMPTY;
	}

	@Override
	public void activateEditor(IWorkbenchPage aPage, IStructuredSelection aSelection) {
		// no-op

	}

}
