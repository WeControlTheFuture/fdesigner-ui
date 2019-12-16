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
 *     Matthew Hall - bugs 265561, 278311
 *     Stefan Xenos <sxenos@gmail.com> - Bug 335792
 ******************************************************************************/

package org.fdesigner.databinding.property;

import org.fdesigner.databinding.property.list.SimpleListProperty;
import org.fdesigner.databinding.property.map.SimpleMapProperty;
import org.fdesigner.databinding.property.set.SimpleSetProperty;
import org.fdesigner.databinding.property.value.SimpleValueProperty;

/**
 * A listener capable of adding or removing itself as a listener on a source
 * object using the source's "native" listener API. Events received from the
 * source objects are parlayed to the {@link ISimplePropertyListener} provided
 * to the method that constructed this native listener instance.
 *
 * @param <S>
 *            type of the source object
 * @since 1.2
 * @see NativePropertyListener
 * @see SimpleValueProperty#adaptListener(ISimplePropertyListener)
 * @see SimpleListProperty#adaptListener(ISimplePropertyListener)
 * @see SimpleSetProperty#adaptListener(ISimplePropertyListener)
 * @see SimpleMapProperty#adaptListener(ISimplePropertyListener)
 */
public interface INativePropertyListener<S> {
	/**
	 * Adds the receiver as a listener for property events on the specified
	 * property source.
	 *
	 * @param source
	 *            the property source (may be null)
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void addTo(S source);

	/**
	 * Removes the receiver as a listener for property events on the specified
	 * property source.
	 *
	 * @param source
	 *            the property source (may be null)
	 * @noreference This method is not intended to be referenced by clients.
	 */
	public void removeFrom(S source);
}
