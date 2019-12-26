/*******************************************************************************
 *  Copyright (c) 2007, 2018 IBM Corporation and others.
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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import org.fdesigner.p2.engine.IProfile;
import org.fdesigner.p2.metadata.IInstallableUnit;
import org.fdesigner.p2.metadata.internal.p2.metadata.io.MetadataWriter;
import org.fdesigner.p2.metadata.query.QueryUtil;
import org.fdesigner.p2.repository.internal.p2.persistence.XMLWriter.ProcessingInstruction;

public class ProfileWriter extends MetadataWriter implements ProfileXMLConstants {

	public ProfileWriter(OutputStream output, ProcessingInstruction[] processingInstructions) {
		super(output, processingInstructions);
	}

	public void writeProfile(IProfile profile) {
		start(PROFILE_ELEMENT);
		attribute(ID_ATTRIBUTE, profile.getProfileId());
		attribute(TIMESTAMP_ATTRIBUTE, Long.toString(profile.getTimestamp()));
		writeProperties(profile.getProperties());
		ArrayList<IInstallableUnit> ius = new ArrayList<>(profile.query(QueryUtil.createIUAnyQuery(), null).toUnmodifiableSet());
		Collections.sort(ius, new Comparator<IInstallableUnit>() {
			@Override
			public int compare(IInstallableUnit iu1, IInstallableUnit iu2) {
				int IdCompare = iu1.getId().compareTo(iu2.getId());
				if (IdCompare != 0)
					return IdCompare;

				return iu1.getVersion().compareTo(iu2.getVersion());
			}
		});
		writeInstallableUnits(ius.iterator(), ius.size());
		writeInstallableUnitsProperties(ius.iterator(), ius.size(), profile);
		end(PROFILE_ELEMENT);
		flush();
	}

	private void writeInstallableUnitsProperties(Iterator<IInstallableUnit> it, int size, IProfile profile) {
		if (size == 0)
			return;
		start(IUS_PROPERTIES_ELEMENT);
		attribute(COLLECTION_SIZE_ATTRIBUTE, size);
		while (it.hasNext()) {
			IInstallableUnit iu = it.next();
			Map<String, String> properties = profile.getInstallableUnitProperties(iu);
			if (properties.isEmpty())
				continue;

			start(IU_PROPERTIES_ELEMENT);
			attribute(ID_ATTRIBUTE, iu.getId());
			attribute(VERSION_ATTRIBUTE, iu.getVersion().toString());
			writeProperties(properties);
			end(IU_PROPERTIES_ELEMENT);
		}
		end(IUS_PROPERTIES_ELEMENT);
	}
}
