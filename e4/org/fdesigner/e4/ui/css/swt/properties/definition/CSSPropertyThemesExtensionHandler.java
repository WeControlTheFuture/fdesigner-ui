/*******************************************************************************
 * Copyright (c) 2013, 2014 IBM Corporation and others.
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
package org.fdesigner.e4.ui.css.swt.properties.definition;

import static org.fdesigner.e4.ui.css.swt.helpers.ThemeElementDefinitionHelper.normalizeId;

import org.fdesigner.e4.ui.css.core.dom.properties.ICSSPropertyHandler;
import org.fdesigner.e4.ui.css.core.engine.CSSEngine;
import org.fdesigner.e4.ui.css.swt.dom.definition.ThemesExtensionElement;
import org.fdesigner.e4.ui.css.swt.internal.css.swt.definition.IThemesExtension;
import org.w3c.dom.css.CSSValue;

public class CSSPropertyThemesExtensionHandler implements ICSSPropertyHandler {
	private static final String DEFINITION_LIST_SEPARATOR = ",";

	private static final String FONT_DEFINITION_PROP = "font-definition";

	private static final String COLOR_DEFINITION_PROP = "color-definition";

	@Override
	public boolean applyCSSProperty(Object element, String property,
			CSSValue value, String pseudo, CSSEngine engine) throws Exception {
		if (!(element instanceof ThemesExtensionElement) || property == null) {
			return false;
		}

		IThemesExtension themeExtension = (IThemesExtension) ((ThemesExtensionElement) element).getNativeWidget();
		switch (property) {
		case FONT_DEFINITION_PROP:
			addDefinitions(themeExtension, true, parseSymbolicNames(value.getCssText()));
			break;
		case COLOR_DEFINITION_PROP:
			addDefinitions(themeExtension, false, parseSymbolicNames(value.getCssText()));
			break;
		default:
			return false;
		}
		return true;
	}

	@Override
	public String retrieveCSSProperty(Object element, String property,
			String pseudo, CSSEngine engine) throws Exception {
		return null;
	}

	private String[] parseSymbolicNames(String symbolicNames) {
		return symbolicNames.split(DEFINITION_LIST_SEPARATOR);
	}

	private void addDefinitions(IThemesExtension themeExtension, boolean fontDefinitions, String... symbolicNames) {
		for (String symbolicName: symbolicNames) {
			String normalizedSymbolicName = normalizeId(symbolicName.trim().substring(1));
			if (fontDefinitions) {
				themeExtension.addFontDefinition(normalizedSymbolicName);
			} else {
				themeExtension.addColorDefinition(normalizedSymbolicName);
			}
		}
	}
}
