/*******************************************************************************
 * Copyright (c) 2009, 2015 Matthew Hall and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 280157)
 ******************************************************************************/

package org.fdesigner.ui.jface.databinding.internal.databinding.swt;

import org.eclipse.swt.widgets.ScrollBar;

/**
 *
 */
public class ScrollBarEnabledProperty extends WidgetBooleanValueProperty<ScrollBar> {
	@Override
	public boolean doGetBooleanValue(ScrollBar source) {
		return source.getEnabled();
	}

	@Override
	void doSetBooleanValue(ScrollBar source, boolean value) {
		source.setEnabled(value);
	}

	@Override
	public String toString() {
		return "ScrollBar.enabled <boolean>"; //$NON-NLS-1$
	}
}
