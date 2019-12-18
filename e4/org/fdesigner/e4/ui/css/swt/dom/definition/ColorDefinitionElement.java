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
 *******************************************************************************/
package org.fdesigner.e4.ui.css.swt.dom.definition;

import org.fdesigner.e4.ui.css.core.engine.CSSEngine;
import org.fdesigner.e4.ui.css.swt.internal.css.swt.definition.IColorDefinitionOverridable;

public class ColorDefinitionElement extends ThemeDefinitionElement<IColorDefinitionOverridable> {
	public ColorDefinitionElement(IColorDefinitionOverridable definition,
			CSSEngine engine) {
		super(definition, engine);
	}
}