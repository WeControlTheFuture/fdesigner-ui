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

package org.fdesigner.navigator.internal.navigator.filters;

import org.fdesigner.ui.jface.viewers.Viewer;
import org.fdesigner.ui.jface.viewers.ViewerFilter;

/**
 *
 * A no-op viewer filter used to prevent null return values from
 * {@link CommonFilterDescriptor#createFilter()}.
 *
 * @since 3.2
 *
 */
public class SkeletonViewerFilter extends ViewerFilter {

	/**
	 * The singleton instance.
	 */
	public static final SkeletonViewerFilter INSTANCE = new SkeletonViewerFilter();

	private SkeletonViewerFilter() {
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {

		return true;
	}

}
