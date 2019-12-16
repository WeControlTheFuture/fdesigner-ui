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
 *     Matthew Hall - initial API and implementation (bug 263868)
 *     Stefan Xenos <sxenos@gmail.com> - Bug 335792
 ******************************************************************************/

package org.fdesigner.databinding.property.internal.databinding.property.value;

import org.fdesigner.databinding.observable.value.ValueDiff;
import org.fdesigner.databinding.property.INativePropertyListener;
import org.fdesigner.databinding.property.ISimplePropertyListener;
import org.fdesigner.databinding.property.value.SimpleValueProperty;

/**
 * @param <T>
 *            type of the value of the property
 * @since 3.3
 *
 */
public final class SelfValueProperty<T> extends SimpleValueProperty<T, T> {
	private final Object valueType;

	/**
	 * @param valueType
	 */
	public SelfValueProperty(Object valueType) {
		this.valueType = valueType;
	}

	@Override
	public Object getValueType() {
		return valueType;
	}

	@Override
	protected T doGetValue(T source) {
		return source;
	}

	@Override
	protected void doSetValue(T source, T value) {
	}

	@Override
	public INativePropertyListener<T> adaptListener(ISimplePropertyListener<T, ValueDiff<? extends T>> listener) {
		return null;
	}

	protected void doAddListener(T source, INativePropertyListener<T> listener) {
	}

	protected void doRemoveListener(T source, INativePropertyListener<T> listener) {
	}
}
