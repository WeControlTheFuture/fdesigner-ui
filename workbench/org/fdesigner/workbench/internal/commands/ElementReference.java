/*******************************************************************************
 * Copyright (c) 2007, 2015 IBM Corporation and others.
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

package org.fdesigner.workbench.internal.commands;

import java.util.HashMap;
import java.util.Map;

import org.fdesigner.workbench.commands.IElementReference;
import org.fdesigner.workbench.menus.UIElement;

/**
 * Our element reference that is used during element
 * registration/unregistration.
 *
 * @since 3.3
 */
public class ElementReference implements IElementReference {

	private String commandId;
	private UIElement element;
	private HashMap parameters;

	/**
	 * Construct the reference.
	 *
	 * @param id    command id. Must not be <code>null</code>.
	 * @param adapt the element. Must not be <code>null</code>.
	 * @param parms parameters used for filtering. Must not be <code>null</code>.
	 */
	public ElementReference(String id, UIElement adapt, Map parms) {
		commandId = id;
		element = adapt;
		if (parms == null) {
			parameters = new HashMap();
		} else {
			parameters = new HashMap(parms);
		}
	}

	@Override
	public UIElement getElement() {
		return element;
	}

	@Override
	public String getCommandId() {
		return commandId;
	}

	@Override
	public Map getParameters() {
		return parameters;
	}
}
