/*******************************************************************************
 *  Copyright (c) 2008, 2017 IBM Corporation and others.
 *
 *  This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License 2.0
 *  which accompanies this distribution, and is available at
 *  https://www.eclipse.org/legal/epl-2.0/
 *
 *  SPDX-License-Identifier: EPL-2.0
 * 
 *  Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.fdesigner.p2.engine.internal.p2.engine;

import java.net.URI;
import java.util.Map;

import org.fdesigner.p2.core.ProvisionException;
import org.fdesigner.p2.repository.IRepositoryManager;
import org.fdesigner.p2.repository.metadata.IMetadataRepository;
import org.fdesigner.p2.repository.metadata.spi.MetadataRepositoryFactory;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;

public class ProfileMetadataRepositoryFactory extends MetadataRepositoryFactory {

	/**
	 * @throws ProvisionException
	 * documenting to avoid warning 
	 */
	@Override
	public IMetadataRepository create(URI location, String name, String type, Map<String, String> properties) throws ProvisionException {
		return null;
	}

	@Override
	public IMetadataRepository load(URI location, int flags, IProgressMonitor monitor) throws ProvisionException {
		//return null if the caller wanted a modifiable repo
		if ((flags & IRepositoryManager.REPOSITORY_HINT_MODIFIABLE) > 0) {
			return null;
		}
		return new ProfileMetadataRepository(getAgent(), location, monitor);
	}
}
