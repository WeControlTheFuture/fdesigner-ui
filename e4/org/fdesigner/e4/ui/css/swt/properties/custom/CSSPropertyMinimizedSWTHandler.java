/*******************************************************************************
 * Copyright (c) 2009, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the terms of
 * the Eclipse Public License 2.0 which accompanies this distribution, and is
t https://www.eclipse.org/legal/epl-2.0/
t
t SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.fdesigner.e4.ui.css.swt.properties.custom;

import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.widgets.Control;
import org.fdesigner.e4.ui.css.core.engine.CSSEngine;
import org.fdesigner.e4.ui.css.swt.properties.AbstractCSSPropertySWTHandler;
import org.w3c.dom.css.CSSValue;

public class CSSPropertyMinimizedSWTHandler extends AbstractCSSPropertySWTHandler{

	@Override
	public void applyCSSProperty(Control control, String property,
			CSSValue value, String pseudo, CSSEngine engine) throws Exception {
		boolean isMinimized = (Boolean)engine.convert(value, Boolean.class, null);
		if (control instanceof CTabFolder) {
			CTabFolder folder = (CTabFolder) control;
			folder.setMinimized(isMinimized);
		}
	}

	@Override
	public String retrieveCSSProperty(Control control, String property,
			String pseudo, CSSEngine engine) throws Exception {
		if (control instanceof CTabFolder) {
			CTabFolder folder = (CTabFolder)control;
			return Boolean.toString( folder.getMinimized() );
		}
		return null;
	}


}
