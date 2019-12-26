/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
 *     Prashant Deva - Bug 194674 [prov] Provide write access to metadata repository
 *     Cloudsmith Inc. - query indexes
 *******************************************************************************/
package org.fdesigner.p2.metadata.internal.p2.metadata;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.fdesigner.p2.core.IProvisioningAgent;
import org.fdesigner.p2.core.internal.p2.core.helpers.CollectionUtils;
import org.fdesigner.p2.metadata.IInstallableUnit;
import org.fdesigner.p2.metadata.KeyWithLocale;
import org.fdesigner.p2.metadata.index.IIndex;
import org.fdesigner.p2.metadata.index.IIndexProvider;
import org.fdesigner.p2.metadata.internal.p2.metadata.index.CapabilityIndex;
import org.fdesigner.p2.metadata.internal.p2.metadata.index.IdIndex;
import org.fdesigner.p2.metadata.internal.p2.metadata.index.IndexProvider;
import org.fdesigner.p2.metadata.query.IQuery;
import org.fdesigner.p2.metadata.query.IQueryResult;
import org.fdesigner.p2.repository.IRepositoryReference;
import org.fdesigner.p2.repository.metadata.spi.AbstractMetadataRepository;
import org.fdesigner.runtime.common.runtime.IProgressMonitor;
import org.fdesigner.runtime.common.runtime.URIUtil;

/**
 * A metadata repository backed by an arbitrary URL.
 */
public class URLMetadataRepository extends AbstractMetadataRepository implements IIndexProvider<IInstallableUnit> {

	public static final String CONTENT_FILENAME = "content"; //$NON-NLS-1$
	protected Collection<IRepositoryReference> references;
	public static final String XML_EXTENSION = ".xml"; //$NON-NLS-1$
	private static final String REPOSITORY_TYPE = URLMetadataRepository.class.getName();
	private static final Integer REPOSITORY_VERSION = 1;

	transient protected URI content;
	protected IUMap units = new IUMap();
	private IIndex<IInstallableUnit> idIndex;
	private IIndex<IInstallableUnit> capabilityIndex;
	private TranslationSupport translationSupport;

	public static URI getActualLocation(URI base) {
		return getActualLocation(base, XML_EXTENSION);
	}

	public static URI getActualLocation(URI base, String extension) {
		if (extension == null)
			extension = XML_EXTENSION;
		return URIUtil.append(base, CONTENT_FILENAME + extension);
	}

	public URLMetadataRepository(IProvisioningAgent agent) {
		super(agent);
	}

	public URLMetadataRepository(IProvisioningAgent agent, URI location, String name, Map<String, String> properties) {
		super(agent, name == null ? (location != null ? location.toString() : "") : name, REPOSITORY_TYPE, REPOSITORY_VERSION.toString(), location, null, null, properties); //$NON-NLS-1$
		content = getActualLocation(location);
	}

	// this is synchronized because content can be initialized in initializeAfterLoad
	protected synchronized URI getContentURL() {
		return content;
	}

	@Override
	public synchronized void initialize(RepositoryState state) {
		setName(state.Name);
		setType(state.Type);
		setVersion(state.Version.toString());
		setProvider(state.Provider);
		setDescription(state.Description);
		setLocation(state.Location);
		setProperties(state.Properties);
		this.units.addAll(state.Units);
		this.references = CollectionUtils.unmodifiableList(state.Repositories);
	}

	// Use this method to setup any transient fields etc after the object has been restored from a stream
	public synchronized void initializeAfterLoad(URI repoLocation) {
		setLocation(repoLocation);
		content = getActualLocation(repoLocation);
	}

	@Override
	public Collection<IRepositoryReference> getReferences() {
		return references;
	}

	@Override
	public boolean isModifiable() {
		return false;
	}

	@Override
	public synchronized IQueryResult<IInstallableUnit> query(IQuery<IInstallableUnit> query, IProgressMonitor monitor) {
		return IndexProvider.query(this, query, monitor);
	}

	@Override
	public synchronized IIndex<IInstallableUnit> getIndex(String memberName) {
		if (InstallableUnit.MEMBER_ID.equals(memberName)) {
			if (idIndex == null)
				idIndex = new IdIndex(units);
			return idIndex;
		}

		if (InstallableUnit.MEMBER_PROVIDED_CAPABILITIES.equals(memberName)) {
			if (capabilityIndex == null)
				capabilityIndex = new CapabilityIndex(units.iterator());
			return capabilityIndex;
		}
		return null;
	}

	@Override
	public synchronized Object getManagedProperty(Object client, String memberName, Object key) {
		if (!(client instanceof IInstallableUnit))
			return null;
		IInstallableUnit iu = (IInstallableUnit) client;
		if (InstallableUnit.MEMBER_TRANSLATED_PROPERTIES.equals(memberName)) {
			if (translationSupport == null)
				translationSupport = new TranslationSupport(this);
			return key instanceof KeyWithLocale ? translationSupport.getIUProperty(iu, (KeyWithLocale) key) : translationSupport.getIUProperty(iu, key.toString());
		}
		return null;
	}

	@Override
	public Iterator<IInstallableUnit> everything() {
		return units.iterator();
	}
}
