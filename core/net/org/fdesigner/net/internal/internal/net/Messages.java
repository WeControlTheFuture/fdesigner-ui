/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
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
package org.fdesigner.net.internal.internal.net;

import org.fdesigner.supplement.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.core.internal.net.messages"; //$NON-NLS-1$

	public static String ProxySelector_0;
	public static String ProxySelector_1;
	public static String ProxySelector_2;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
