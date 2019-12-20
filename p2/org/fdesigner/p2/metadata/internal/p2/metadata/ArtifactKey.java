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
 *******************************************************************************/
package org.fdesigner.p2.metadata.internal.p2.metadata;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.fdesigner.p2.metadata.IArtifactKey;
import org.fdesigner.p2.metadata.Version;
import org.fdesigner.p2.metadata.expression.IMemberProvider;
import org.fdesigner.runtime.common.runtime.Assert;

/** 
 * The concrete type for representing IArtifactKey's.
 * <p>
 * See {link IArtifact for a description of the lifecycle of artifact keys) 
 */
public class ArtifactKey implements IArtifactKey, IMemberProvider {
	private static final String SEPARATOR = ","; //$NON-NLS-1$

	public static final String MEMBER_ID = "id"; //$NON-NLS-1$
	public static final String MEMBER_CLASSIFIER = "classifier"; //$NON-NLS-1$
	public static final String MEMBER_VERSION = "version"; //$NON-NLS-1$

	private final String id;
	private final String classifier;
	private final Version version;

	private static String[] getArrayFromList(String stringList, String separator) {
		if (stringList == null || stringList.trim().length() == 0)
			return new String[0];
		ArrayList<String> list = new ArrayList<>();
		boolean separatorSeen = true;
		StringTokenizer tokens = new StringTokenizer(stringList, separator, true);
		while (tokens.hasMoreTokens()) {
			String token = tokens.nextToken().trim();
			if (token.equals(separator)) {
				if (separatorSeen)
					list.add(""); //$NON-NLS-1$
				separatorSeen = true;
			} else {
				separatorSeen = false;
				if (token.length() != 0)
					list.add(token);
			}
		}
		if (separatorSeen)
			list.add(""); //$NON-NLS-1$
		return list.toArray(new String[list.size()]);
	}

	public static IArtifactKey parse(String specification) {
		String[] parts = getArrayFromList(specification, SEPARATOR);
		if (parts.length < 2 || parts.length > 3)
			throw new IllegalArgumentException("Unexpected number of parts in artifact key: " + specification); //$NON-NLS-1$
		Version version = Version.emptyVersion;
		if (parts.length == 3 && parts[2].trim().length() > 0)
			version = Version.parseVersion(parts[2]);
		try {
			return new ArtifactKey(parts[0], parts[1], version);
		} catch (IllegalArgumentException e) {
			throw (IllegalArgumentException) new IllegalArgumentException("Wrong version syntax in artifact key: " + specification).initCause(e); //$NON-NLS-1$
		}
	}

	public ArtifactKey(String classifier, String id, Version version) {
		super();
		Assert.isNotNull(classifier);
		Assert.isNotNull(id);
		Assert.isNotNull(version);
		if (classifier.contains(SEPARATOR))
			throw new IllegalArgumentException("comma not allowed in classifier"); //$NON-NLS-1$
		if (id.contains(SEPARATOR))
			throw new IllegalArgumentException("comma not allowed in id"); //$NON-NLS-1$
		this.classifier = classifier;
		this.id = id;
		this.version = version;
	}

	public ArtifactKey(IArtifactKey artifactKey) {
		this.classifier = artifactKey.getClassifier();
		this.id = artifactKey.getId();
		this.version = artifactKey.getVersion();
	}

	@Override
	public String getClassifier() {
		return classifier;
	}

	@Override
	public Version getVersion() {
		return version;
	}

	@Override
	public int hashCode() {
		int hash = id.hashCode();
		hash = 17 * hash + getVersion().hashCode();
		hash = 17 * hash + classifier.hashCode();
		return hash;
	}

	@Override
	public String toString() {
		return classifier + SEPARATOR + id + SEPARATOR + getVersion();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof IArtifactKey))
			return false;
		IArtifactKey ak = (IArtifactKey) obj;
		return ak.getId().equals(id) && ak.getVersion().equals(getVersion()) && ak.getClassifier().equals(classifier);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String toExternalForm() {
		StringBuffer data = new StringBuffer(classifier).append(SEPARATOR);
		data.append(id).append(SEPARATOR);
		data.append(version.toString());
		return data.toString();
	}

	@Override
	public Object getMember(String memberName) {
		// It is OK to use identity comparisons here since
		// a) All constant valued strings are always interned
		// b) The Member constructor always interns the name
		//
		if (MEMBER_ID == memberName)
			return id;
		if (MEMBER_VERSION == memberName)
			return version;
		if (MEMBER_CLASSIFIER == memberName)
			return classifier;
		throw new IllegalArgumentException("No such member: " + memberName); //$NON-NLS-1$
	}
}
