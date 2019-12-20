/*******************************************************************************
 * Copyright (c) 2006, 2015 IBM Corporation and others.
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
package org.fdesigner.forms.internal.forms;

import org.fdesigner.supplement.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.ui.internal.forms.Messages"; //$NON-NLS-1$

	private Messages() {
	}

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	public static String FormDialog_defaultTitle;
	public static String FormText_copy;
	/*
	 * Message manager
	 */
	public static String MessageManager_sMessageSummary;
	public static String MessageManager_sWarningSummary;
	public static String MessageManager_sErrorSummary;
	public static String MessageManager_pMessageSummary;
	public static String MessageManager_pWarningSummary;
	public static String MessageManager_pErrorSummary;
	public static String ToggleHyperlink_accessibleColumn;
	public static String ToggleHyperlink_accessibleName;
}
