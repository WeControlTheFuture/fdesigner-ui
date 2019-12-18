/*******************************************************************************
 * Copyright (c) 2004, 2015 IBM Corporation and others.
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
package org.fdesigner.workbench.internal.intro;

import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.ui.jface.resource.ImageDescriptor;
import org.fdesigner.workbench.intro.IIntroPart;

/**
 * Describes an introduction extension.
 *
 * @since 3.0
 */
public interface IIntroDescriptor {

	/**
	 * Creates an instance of the intro part defined in the descriptor.
	 */
	IIntroPart createIntro() throws CoreException;

	/**
	 * Returns the part id.
	 *
	 * @return the id of the part
	 */
	String getId();

	/**
	 * Returns the descriptor of the image for this part.
	 *
	 * @return the descriptor of the image to display next to this part
	 */
	ImageDescriptor getImageDescriptor();

	/**
	 * Return the label override string for this part.
	 *
	 * @return the label override string or <code>null</code> if one has not been
	 *         specified
	 * @since 3.2
	 */
	String getLabelOverride();
}
