/*******************************************************************************
 * Copyright (c) 2010, 2016 Tom Schindl and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Tom Schindl <tom.schindl@bestsolution.at> - initial API and implementation
 *******************************************************************************/
package org.fdesigner.e4.ui.css.swt.theme.internal.theme;

import org.eclipse.swt.widgets.Display;
import org.fdesigner.e4.ui.css.core.engine.CSSEngine;
import org.fdesigner.e4.ui.css.core.engine.CSSErrorHandler;
import org.fdesigner.e4.ui.css.swt.dom.WidgetElement;
import org.fdesigner.e4.ui.css.swt.engine.CSSSWTEngineImpl;
import org.fdesigner.e4.ui.css.swt.theme.IThemeEngine;
import org.fdesigner.e4.ui.css.swt.theme.IThemeManager;
import org.fdesigner.runtime.common.runtime.IStatus;
import org.fdesigner.runtime.common.runtime.Status;
import org.fdesigner.runtime.core.ILog;
import org.fdesigner.runtime.core.Platform;
import org.fdesigner.services.component.annotations.Component;

@Component
public class ThemeEngineManager implements IThemeManager {
	private static final String KEY = "org.eclipse.e4.ui.css.swt.theme";

	private static ILog LOG = Platform.getLog(Platform.getBundle(KEY));

	@Override
	public IThemeEngine getEngineForDisplay(Display display) {
		IThemeEngine engine = (IThemeEngine) display.getData(KEY);

		if( engine == null ) {
			engine = new ThemeEngine(display);
			engine.addCSSEngine(getCSSSWTEngine(display));
			display.setData(KEY, engine);
		}
		return engine;
	}

	private CSSEngine getCSSSWTEngine(Display display) {
		CSSEngine cssEngine = WidgetElement.getEngine(display);
		if (cssEngine != null) {
			return cssEngine;
		}
		cssEngine = new CSSSWTEngineImpl(display, true);
		cssEngine.setErrorHandler(new CSSErrorHandler() {
			@Override
			public void error(Exception e) {
				logError(e.getMessage(), e);
			}
		});
		WidgetElement.setEngine(display, cssEngine);
		return cssEngine;
	}

	static void logError(String message, Throwable e) {
		LOG.log(new Status(IStatus.ERROR, KEY, message, e));
	}
}