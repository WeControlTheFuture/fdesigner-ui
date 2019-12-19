/*******************************************************************************
 * Copyright (c) 2011, 2015 IBM Corporation and others.
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
 *     Lars Vogel <Lars.Vogel@gmail.com> - Bug 440810
 ******************************************************************************/

package org.fdesigner.workbench.internal.handlers;

import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.commands.ExecutionException;
import org.fdesigner.e4.ui.services.EContextService;
import org.fdesigner.workbench.IWorkbenchPart;
import org.fdesigner.workbench.handlers.HandlerUtil;

public class ActiveContextInfoHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final IWorkbenchPart part = HandlerUtil.getActivePartChecked(event);
		EContextService service = part.getSite().getService(EContextService.class);
		for (String id : service.getActiveContextIds()) {
			System.out.println("activeContext: " + id); //$NON-NLS-1$
		}
		return null;
	}

}
