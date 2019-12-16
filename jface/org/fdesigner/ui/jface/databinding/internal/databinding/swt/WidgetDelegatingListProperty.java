/*******************************************************************************
 * Copyright (c) 2009, 2014 Matthew Hall and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Matthew Hall - initial API and implementation (bug 264286)
 *******************************************************************************/

package org.fdesigner.ui.jface.databinding.internal.databinding.swt;

import org.eclipse.swt.widgets.Widget;
import org.fdesigner.databinding.property.list.DelegatingListProperty;
import org.fdesigner.ui.jface.databinding.swt.DisplayRealm;
import org.fdesigner.ui.jface.databinding.swt.ISWTObservableList;
import org.fdesigner.ui.jface.databinding.swt.IWidgetListProperty;

abstract class WidgetDelegatingListProperty<S extends Widget, E> extends DelegatingListProperty<S, E>
		implements IWidgetListProperty<S, E> {
	RuntimeException notSupported(Object source) {
		return new IllegalArgumentException(
				"Widget [" + source.getClass().getName() + "] is not supported."); //$NON-NLS-1$//$NON-NLS-2$
	}

	public WidgetDelegatingListProperty(Object elementType) {
		super(elementType);
	}

	@Override
	public ISWTObservableList<E> observe(S widget) {
		return (ISWTObservableList<E>) observe(DisplayRealm.getRealm(widget.getDisplay()), widget);
	}
}