/*******************************************************************************
 * Copyright (c) 2013 IBM Corporation and others.
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
package org.fdesigner.e4.ui.css.core.resources;

import org.w3c.dom.css.CSSValue;

public class ResourceRegistryKeyFactory {
	public Object createKey(CSSValue value) {
		return CSSResourcesHelpers.getCSSValueKey(value);
	}
}
