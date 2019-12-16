/*
 * Copyright (C) 2005, 2014 db4objects Inc. (http://www.db4o.com) and others.
 *
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     db4objects - Initial API and implementation
 *     Boris Bokowski (IBM Corporation) - bug 118429
 *     Tom Schindl<tom.schindl@bestsolution.at> - bugfix for 217940
 */
package org.fdesigner.databinding.internal.databinding.validation;

import org.fdesigner.databinding.internal.databinding.BindingMessages;
import org.fdesigner.databinding.validation.IValidator;
import org.fdesigner.databinding.validation.ValidationStatus;
import org.fdesigner.runtime.common.runtime.IStatus;

/**
 * ReadOnlyValidator. A validator that can be used as a partial validator for read-only fields.
 */
public class ReadOnlyValidator implements IValidator<Object> {

	private static ReadOnlyValidator singleton = null;

	/**
	 * Returns the ReadOnlyValidator
	 *
	 * @return the ReadOnlyValidator
	 */
	public static ReadOnlyValidator getDefault() {
		if (singleton == null) {
			singleton = new ReadOnlyValidator();
		}
		return singleton;
	}

	@Override
	public IStatus validate(Object value) {
		// No changes are allowed
		return ValidationStatus.error(BindingMessages
				.getString(BindingMessages.VALIDATE_NO_CHANGE_ALLOWED_HELP));
	}

}
