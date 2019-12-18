/*******************************************************************************
 * Copyright (c) 2009, 2015 Angelo Zerr and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Angelo Zerr <angelo.zerr@gmail.com> - initial API and implementation
 *******************************************************************************/
package org.fdesigner.e4.ui.css.swt.dom;

import org.eclipse.swt.widgets.Item;
import org.fdesigner.e4.ui.css.core.dom.CSSStylableElement;
import org.fdesigner.e4.ui.css.core.engine.CSSEngine;

/**
 * {@link CSSStylableElement} implementation which wrap SWT {@link Item}.
 *
 */
public class ItemElement extends WidgetElement {

	public ItemElement(Item item, CSSEngine engine) {
		super(item, engine);
	}

}
