/*******************************************************************************
 * Copyright (c) 2000, 2015 IBM Corporation and others.
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
package org.fdesigner.navigator.internal.navigator.framelist;

import org.fdesigner.workbench.PlatformUI;

/**
 * Generic "Go Into" action which goes to the frame for the current selection.
 * @since 3.4
 */
public class GoIntoAction extends FrameAction {

	private static final String ID = "org.eclipse.ui.framelist.goInto"; //$NON-NLS-1$

	/**
	 * Constructs a new action for the specified frame list.
	 *
	 * @param frameList the frame list
	 */
	public GoIntoAction(FrameList frameList) {
		super(frameList);
		setId(ID);
		setText(FrameListMessages.GoInto_text);
		setToolTipText(FrameListMessages.GoInto_toolTip);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(this,
				IFrameListHelpContextIds.GO_INTO_ACTION);
		update();
	}

	private Frame getSelectionFrame(int flags) {
		return getFrameList().getSource().getFrame(
				IFrameSource.SELECTION_FRAME, flags);
	}

	/**
	 * Calls <code>gotoFrame</code> on the frame list with a frame
	 * representing the currently selected container.
	 */
	@Override
	public void run() {
		Frame selectionFrame = getSelectionFrame(IFrameSource.FULL_CONTEXT);
		if (selectionFrame != null) {
			getFrameList().gotoFrame(selectionFrame);
		}
	}

	/**
	 * Updates this action's enabled state.
	 * This action is enabled only when there is a frame for the current selection.
	 */
	@Override
	public void update() {
		super.update();
		Frame selectionFrame = getSelectionFrame(0);
		setEnabled(selectionFrame != null);
	}
}
