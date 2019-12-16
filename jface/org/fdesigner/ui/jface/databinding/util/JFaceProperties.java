/*******************************************************************************
 * Copyright (c) 2008, 2015 IBM Corporation and others.
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
 *     Jeanderson Candido <http://jeandersonbc.github.io> - Bug 413611
 *******************************************************************************/
package org.fdesigner.ui.jface.databinding.util;

import org.fdesigner.databinding.property.value.IValueProperty;
import org.fdesigner.ui.jface.databinding.internal.databinding.util.JFaceProperty;

/**
 * Helper class for providing {@link IObservableValue} instances for properties
 * of an object that fires property changes events to an
 * {@link IPropertyChangeListener}.
 *
 * @since 1.3
 */
public class JFaceProperties {

	/**
	 * Returns a property for observing the property of the given model object
	 * whose getter and setter use the suffix fieldName in the same manner as a
	 * Java bean and which fires events to an {@link IPropertyChangeListener}
	 * for the given propertyName when the value of the field changes.
	 *
	 * @param clazz
	 *            the class defining the getter and setter
	 * @param fieldName
	 *            the field name
	 * @param propertyName
	 *            the property name
	 *
	 * @return an observable value
	 */
	public static <S, T> IValueProperty<S, T> value(Class<S> clazz, String fieldName, String propertyName) {
		return new JFaceProperty<>(fieldName, propertyName, clazz);
	}

}
