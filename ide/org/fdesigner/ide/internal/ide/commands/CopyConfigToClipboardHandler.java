/*******************************************************************************
 * Copyright (c) 2008, 2015 IBM Corporation and others.
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

package org.fdesigner.ide.internal.ide.commands;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.fdesigner.commands.AbstractHandler;
import org.fdesigner.commands.ExecutionEvent;
import org.fdesigner.workbench.handlers.HandlerUtil;
import org.fdesigner.workbench.internal.ConfigurationInfo;

/**
 * Copies the configuration data present in the about dialog to the clipboard.
 *
 * @since 3.4
 */
public class CopyConfigToClipboardHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		String contents = ConfigurationInfo.getSystemSummary();
		Clipboard clipboard = null;
		try {
			clipboard = new Clipboard(HandlerUtil.getActiveShell(event)
					.getDisplay());
			clipboard.setContents(new Object[] { contents },
					new Transfer[] { TextTransfer.getInstance() });
		} finally {
			if (clipboard != null) {
				clipboard.dispose();
			}
		}
		return null;
	}

}
