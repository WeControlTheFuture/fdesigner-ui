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
 *     Matthew Hall - initial API and implementation (bug 263709)
 *     Matthew Hall - bug 264954
 *     Ovidio Mallo - bug 306611
 ******************************************************************************/

package org.fdesigner.databinding;

import org.fdesigner.databinding.conversion.IConverter;
import org.fdesigner.databinding.internal.databinding.BindingModelProperty;
import org.fdesigner.databinding.internal.databinding.BindingTargetProperty;
import org.fdesigner.databinding.internal.databinding.ConverterValueProperty;
import org.fdesigner.databinding.internal.databinding.DataBindingContextBindingsProperty;
import org.fdesigner.databinding.internal.databinding.DataBindingContextValidationStatusProvidersProperty;
import org.fdesigner.databinding.internal.databinding.ValidationStatusProviderModelsProperty;
import org.fdesigner.databinding.internal.databinding.ValidationStatusProviderTargetsProperty;
import org.fdesigner.databinding.internal.databinding.ValidationStatusProviderValidationStatusProperty;
import org.fdesigner.databinding.observable.IObservable;
import org.fdesigner.databinding.property.Properties;
import org.fdesigner.databinding.property.list.IListProperty;
import org.fdesigner.databinding.property.value.IValueProperty;
import org.fdesigner.runtime.common.runtime.IStatus;

/**
 * A factory for creating properties for core types in the DataBinding framework
 * e.g. {@link DataBindingContext} or ValidationStatusProvider.
 *
 * @since 1.2
 */
public class BindingProperties {
	/**
	 * Returns an {@link IListProperty} for observing the bindings of a
	 * {@link DataBindingContext}.
	 *
	 * @return an {@link IListProperty} for observing the bindings of a
	 *         {@link DataBindingContext}.
	 */
	public static IListProperty<DataBindingContext, Binding> bindings() {
		return new DataBindingContextBindingsProperty();
	}

	/**
	 * Returns an {@link IValueProperty} for observing the model of a
	 * {@link Binding}.
	 *
	 * @return an {@link IValueProperty} for observing the model of a
	 *         {@link Binding}.
	 */
	public static IValueProperty<Binding, IObservable> model() {
		return new BindingModelProperty();
	}

	/**
	 * Returns an {@link IListProperty} for observing the models of a
	 * {@link ValidationStatusProvider}.
	 *
	 * @return an {@link IListProperty} for observing the models of a
	 *         {@link ValidationStatusProvider}.
	 */
	public static IListProperty<ValidationStatusProvider, IObservable> models() {
		return new ValidationStatusProviderModelsProperty();
	}

	/**
	 * Returns an {@link IValueProperty} for observing the target of a
	 * {@link Binding}.
	 *
	 * @return an {@link IValueProperty} for observing the target of a
	 *         {@link Binding}.
	 */
	public static IValueProperty<Binding, IObservable> target() {
		return new BindingTargetProperty();
	}

	/**
	 * Returns an {@link IListProperty} for observing the targets of a
	 * {@link ValidationStatusProvider}.
	 *
	 * @return an {@link IListProperty} for observing the targets of a
	 *         {@link ValidationStatusProvider}.
	 */
	public static IListProperty<ValidationStatusProvider, IObservable> targets() {
		return new ValidationStatusProviderTargetsProperty();
	}

	/**
	 * Returns an {@link IValueProperty} for observing the validation status of
	 * a {@link ValidationStatusProvider}.
	 *
	 * @return an {@link IValueProperty} for observing the validation status of
	 *         a {@link ValidationStatusProvider}.
	 */
	public static IValueProperty<ValidationStatusProvider, IStatus> validationStatus() {
		return new ValidationStatusProviderValidationStatusProperty().value(Properties.observableValue(IStatus.class));
	}

	/**
	 * Returns an {@link IListProperty} for observing the validation status
	 * providers of a {@link DataBindingContext}.
	 *
	 * @return an {@link IListProperty} for observing the validation status
	 *         providers of a {@link DataBindingContext}.
	 */
	public static IListProperty<DataBindingContext, ValidationStatusProvider> validationStatusProviders() {
		return new DataBindingContextValidationStatusProvidersProperty();
	}

	/**
	 * Returns an {@link IValueProperty} whose value results from applying the
	 * given {@link IConverter} on the source object of the value property.
	 * Consequently, the {@link IValueProperty#getValueType() value type} of the
	 * returned property is the same as the {@link IConverter#getToType() target
	 * type} of the converter. Setting a value on the property is not supported.
	 *
	 * @param converter
	 *            The converter to apply to the source object of the value
	 *            property.
	 * @return A new instance of a value property whose value is the result of
	 *         applying the given converter to the source object passed to the
	 *         value property.
	 *
	 * @since 1.4
	 */
	public static <S, T> IValueProperty<S, T> convertedValue(IConverter<S, T> converter) {
		return new ConverterValueProperty<>(converter);
	}
}
