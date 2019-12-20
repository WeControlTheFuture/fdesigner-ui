/*******************************************************************************
 * Copyright (c) 2014-2016 Red Hat Inc., and others
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Mickael Istria (Red Hat Inc.) - initial API and implementation
 ******************************************************************************/
package org.fdesigner.ide.internal.wizards.datatransfer.expressions;

import org.fdesigner.expressions.EvaluationResult;
import org.fdesigner.expressions.Expression;
import org.fdesigner.expressions.IEvaluationContext;
import org.fdesigner.resources.IContainer;
import org.fdesigner.resources.IResource;
import org.fdesigner.resources.IResourceVisitor;
import org.fdesigner.runtime.common.runtime.Adapters;
import org.fdesigner.runtime.common.runtime.CoreException;
import org.fdesigner.runtime.registry.runtime.IConfigurationElement;

/**
 * Expression to check whether a given {@link IContainer} contains a file with
 * provided suffix.
 *
 * @since 3.12
 */
public class HasFileWithSuffixRecursivelyExpression extends Expression {

	/**
	 * The name of the XML tag to use this rule in a plugin.xml
	 */
	public static final String TAG = "hasFileWithSuffixRecursively"; //$NON-NLS-1$

	private String suffix;

	/**
	 * Build expression with a suffix.
	 *
	 * @param suffix
	 */
	public HasFileWithSuffixRecursivelyExpression(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Build expression retrieving the suffix as the 'suffix' attribute on the
	 * provided {@link IConfigurationElement}.
	 *
	 * @param element
	 */
	public HasFileWithSuffixRecursivelyExpression(IConfigurationElement element) {
		this(element.getAttribute("suffix")); //$NON-NLS-1$
	}

	@Override
	public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
		Object root = context.getDefaultVariable();
		IContainer container = Adapters.adapt(root, IContainer.class);
		if (container != null) {
			RecursiveSuffixFileFinder finder = new RecursiveSuffixFileFinder();
			container.accept(finder);
			return EvaluationResult.valueOf(finder.foundFileWithSuffix());
		}
		return EvaluationResult.FALSE;
	}

	private class RecursiveSuffixFileFinder implements IResourceVisitor {

		private boolean res = false;

		@Override
		public boolean visit(IResource resource) {
			if (resource.getType() == IResource.FILE && resource.getName().endsWith(HasFileWithSuffixRecursivelyExpression.this.suffix)) {
				this.res = true;
			}
			if (this.res) {
				return false;
			}
			return resource instanceof IContainer;
		}

		public boolean foundFileWithSuffix() {
			return this.res;
		}
	}
}
