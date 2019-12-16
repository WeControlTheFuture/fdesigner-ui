/*******************************************************************************
 * Copyright (c) 2008, 2015 Matthew Hall and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 194734)
 *     Matthew Hall - bug 262946
 ******************************************************************************/

package org.fdesigner.ui.jface.databinding.internal.databinding.swt;

import org.eclipse.swt.widgets.ToolItem;

/**
 * @since 3.3
 *
 */
public class ToolItemTooltipTextProperty extends WidgetStringValueProperty<ToolItem> {
	@Override
	String doGetStringValue(ToolItem source) {
		return source.getToolTipText();
	}

	@Override
	void doSetStringValue(ToolItem source, String value) {
		source.setToolTipText(value);
	}

	@Override
	public String toString() {
		return "ToolItem.toolTipText <String>"; //$NON-NLS-1$
	}
}