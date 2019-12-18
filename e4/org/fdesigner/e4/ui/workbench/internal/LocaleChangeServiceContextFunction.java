/*******************************************************************************
 * Copyright (c) 2013, 2016 Dirk Fauth and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Dirk Fauth <dirk.fauth@googlemail.com> - initial API and implementation
 *******************************************************************************/
package org.fdesigner.e4.ui.workbench.internal;

import org.fdesigner.e4.core.contexts.ContextFunction;
import org.fdesigner.e4.core.contexts.ContextInjectionFactory;
import org.fdesigner.e4.core.contexts.IContextFunction;
import org.fdesigner.e4.core.contexts.IEclipseContext;
import org.fdesigner.e4.core.services.nls.ILocaleChangeService;
import org.fdesigner.e4.ui.model.application.MApplication;
import org.fdesigner.services.component.annotations.Component;

/**
 * Context function to provide the LocaleChangeServiceImpl to the application context.
 */
@Component(service = IContextFunction.class, property = "service.context.key=org.eclipse.e4.core.services.nls.ILocaleChangeService")
public class LocaleChangeServiceContextFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {
		ILocaleChangeService lcService = ContextInjectionFactory.make(
				LocaleChangeServiceImpl.class, context);

		// add the new object to the application context
		MApplication application = context.get(MApplication.class);
		IEclipseContext ctx = application.getContext();
		ctx.set(ILocaleChangeService.class, lcService);
		return lcService;
	}
}
