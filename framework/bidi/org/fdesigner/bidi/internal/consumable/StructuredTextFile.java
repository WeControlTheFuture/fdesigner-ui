/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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
package org.fdesigner.bidi.internal.consumable;

import org.fdesigner.bidi.custom.StructuredTextTypeHandler;

/**
 *  Handler adapted to processing directory and file paths.
 */
public class StructuredTextFile extends StructuredTextTypeHandler {

	public StructuredTextFile() {
		super(":/\\."); //$NON-NLS-1$
	}
}
