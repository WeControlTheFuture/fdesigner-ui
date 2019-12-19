/*******************************************************************************
 * Copyright (c) 2010, 2016 IBM Corporation and others.
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
package org.fdesigner.e4.ui.workbench.swt.internal.workbench.swt;

import org.fdesigner.e4.core.contexts.ContextFunction;
import org.fdesigner.e4.core.contexts.ContextInjectionFactory;
import org.fdesigner.e4.core.contexts.IContextFunction;
import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.services.component.annotations.Component;

/**
 *
 */
@Component(service = IContextFunction.class, property = "service.context.key=org.eclipse.e4.core.services.statusreporter.StatusReporter")
public class StatusReporterCreationFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		return ContextInjectionFactory.make(WorkbenchStatusReporter.class,
				context);
	}

}
