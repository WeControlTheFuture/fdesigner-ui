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
 *     Matthew Hall - bug 281723
 ******************************************************************************/

package org.fdesigner.ui.jface.databinding.internal.databinding.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;
import org.fdesigner.databinding.observable.value.DecoratingObservableValue;
import org.fdesigner.databinding.observable.value.IObservableValue;
import org.fdesigner.ui.jface.databinding.swt.ISWTObservableValue;

/**
 * @param <T>
 *            the type of value being observed
 *
 * @since 3.3
 */
public class SWTObservableValueDecorator<T> extends DecoratingObservableValue<T>
		implements ISWTObservableValue<T>, Listener {
	private Widget widget;

	/**
	 * @param decorated
	 * @param widget
	 */
	public SWTObservableValueDecorator(IObservableValue<T> decorated, Widget widget) {
		super(decorated, true);
		this.widget = widget;
		WidgetListenerUtil.asyncAddListener(widget, SWT.Dispose, this);
	}

	@Override
	public void handleEvent(Event event) {
		if (event.type == SWT.Dispose)
			dispose();
	}

	@Override
	public Widget getWidget() {
		return widget;
	}

	@Override
	public synchronized void dispose() {
		if (widget != null) {
			WidgetListenerUtil.asyncRemoveListener(widget, SWT.Dispose, this);
			widget = null;
		}
		super.dispose();
	}
}
