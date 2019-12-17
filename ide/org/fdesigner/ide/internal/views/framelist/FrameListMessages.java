/**********************************************************************
 * Copyright (c) 2005, 2015 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 * IBM - Initial API and implementation
 **********************************************************************/
package org.eclipse.ui.internal.views.framelist;

import org.eclipse.osgi.util.NLS;

public class FrameListMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ui.internal.views.framelist.messages";//$NON-NLS-1$

	// ==============================================================================
	// FrameList
	// ==============================================================================
	public static String Back_text;
	public static String Back_toolTip;
	public static String Back_toolTipOneArg;

	public static String Forward_text;
	public static String Forward_toolTip;
	public static String Forward_toolTipOneArg;

	public static String GoInto_text;
	public static String GoInto_toolTip;

	public static String Up_text;
	public static String Up_toolTip;
	public static String Up_toolTipOneArg;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, FrameListMessages.class);
	}
}