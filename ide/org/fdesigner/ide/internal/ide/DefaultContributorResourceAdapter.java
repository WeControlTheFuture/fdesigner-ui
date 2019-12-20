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
package org.fdesigner.ide.internal.ide;

import org.fdesigner.ide.IContributorResourceAdapter2;
import org.fdesigner.ide.extensions.IContributorResourceAdapter;
import org.fdesigner.resources.IResource;
import org.fdesigner.resources.mapping.ResourceMapping;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.runtime.common.runtime.IAdaptable;

/**
 * The DefaultContributorResourceAdapter is the default
 * implementation of the IContributorResourceAdapter used for
 * one to one resource adaption.
 */
public class DefaultContributorResourceAdapter implements
		IContributorResourceAdapter2 {

	private static IContributorResourceAdapter singleton;

	/**
	 * Constructor for DefaultContributorResourceAdapter.
	 */
	public DefaultContributorResourceAdapter() {
		super();
	}

	/**
	 * Return the default instance used for TaskList adapting.
	 * @return the default instance used for TaskList adapting
	 */
	public static IContributorResourceAdapter getDefault() {
		if (singleton == null) {
			singleton = new DefaultContributorResourceAdapter();
		}
		return singleton;
	}

	/*
	 * @see IContributorResourceAdapter#getAdaptedResource(IAdaptable)
	 */
	@Override
	public IResource getAdaptedResource(IAdaptable adaptable) {
		return Adapters.adapt(adaptable, IResource.class);
	}

	@Override
	public ResourceMapping getAdaptedResourceMapping(IAdaptable adaptable) {
		return Adapters.adapt(adaptable, ResourceMapping.class);
	}
}

