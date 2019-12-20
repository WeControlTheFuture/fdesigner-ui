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
package org.fdesigner.ide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.fdesigner.resources.IResource;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.ui.jface.viewers.IStructuredSelection;
import org.fdesigner.ui.jface.viewers.StructuredSelection;

/**
 * Provides utilities for checking the validity of selections.
 * <p>
 * This class provides static methods only; it is not intended to be instantiated
 * or subclassed.
 * @since 3.6
 * </p>
 */
public class ResourceSelectionUtil {
	/*
	 * Private constructor to block instantiation.
	 */
	private ResourceSelectionUtil() {
	}

	/**
	 * Returns whether the types of the resources in the given selection are among
	 * the specified resource types.
	 *
	 * @param selection the selection
	 * @param resourceMask resource mask formed by bitwise OR of resource type
	 *   constants (defined on <code>IResource</code>)
	 * @return <code>true</code> if all selected elements are resources of the right
	 *  type, and <code>false</code> if at least one element is either a resource
	 *  of some other type or a non-resource
	 * @see IResource#getType()
	 */
	public static boolean allResourcesAreOfType(IStructuredSelection selection,
			int resourceMask) {
		Iterator resources = selection.iterator();
		while (resources.hasNext()) {
			Object next = resources.next();
			if (!(next instanceof IResource)) {
				return false;
			}
			if (!resourceIsType((IResource) next, resourceMask)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the selection adapted to IResource. Returns null if any of the
	 * entries are not adaptable.
	 *
	 * @param selection
	 *            the selection
	 * @param resourceMask
	 *            resource mask formed by bitwise OR of resource type constants
	 *            (defined on <code>IResource</code>)
	 * @return IStructuredSelection or null if any of the entries are not
	 *         adaptable.
	 * @see IResource#getType()
	 */
	public static IStructuredSelection allResources(IStructuredSelection selection, int resourceMask) {
		Iterator<?> adaptables = selection.iterator();
		List<IResource> result = new ArrayList<>();
		while (adaptables.hasNext()) {
			Object next = adaptables.next();
			IResource resource = Adapters.adapt(next, IResource.class);
			if (resource == null) {
				return null;
			}
			if (resourceIsType(resource, resourceMask)) {
				result.add(resource);
			}
		}
		return new StructuredSelection(result);
	}

	/**
	 * Returns whether the type of the given resource is among the specified
	 * resource types.
	 *
	 * @param resource the resource
	 * @param resourceMask resource mask formed by bitwise OR of resource type
	 *   constants (defined on <code>IResource</code>)
	 * @return <code>true</code> if the resources has a matching type, and
	 *   <code>false</code> otherwise
	 * @see IResource#getType()
	 */
	public static boolean resourceIsType(IResource resource, int resourceMask) {
		return (resource.getType() & resourceMask) != 0;
	}

}
