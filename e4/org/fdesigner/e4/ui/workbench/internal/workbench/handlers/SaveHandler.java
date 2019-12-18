/*******************************************************************************
 * Copyright (c) 2009, 2010 IBM Corporation and others.
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
package org.fdesigner.e4.ui.workbench.internal.workbench.handlers;

import javax.inject.Named;

import org.fdesigner.e4.core.di.annotations.CanExecute;
import org.fdesigner.e4.core.di.annotations.Execute;
import org.fdesigner.e4.ui.model.application.ui.MDirtyable;
import org.fdesigner.e4.ui.model.application.ui.basic.MPart;
import org.fdesigner.e4.ui.services.IServiceConstants;
import org.fdesigner.e4.ui.workbench.modeling.EPartService;

public class SaveHandler {

	@CanExecute
	boolean canExecute(@Named(IServiceConstants.ACTIVE_PART) MDirtyable dirtyable) {
		return dirtyable == null ? false : dirtyable.isDirty();
	}

	@Execute
	void execute(EPartService partService, @Named(IServiceConstants.ACTIVE_PART) MPart part) {
		partService.savePart(part, false);
	}

}
