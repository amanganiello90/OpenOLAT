/**
 * <a href="http://www.openolat.org">
 * OpenOLAT - Online Learning and Training</a><br>
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License.<br>
 * You may obtain a copy of the License at the
 * <a href="http://www.apache.org/licenses/LICENSE-2.0">Apache homepage</a>
 * <p>
 * Unless required by applicable law or agreed to in writing,<br>
 * software distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License.
 * <p>
 * Initial code contributed and copyrighted by<br>
 * frentix GmbH, http://www.frentix.com
 * <p>
 */
package org.olat.modules.lecture;

import org.olat.core.configuration.AbstractSpringModule;
import org.olat.core.configuration.ConfigOnOff;
import org.olat.core.util.StringHelper;
import org.olat.core.util.coordinate.CoordinatorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 
 * Initial date: 17 mars 2017<br>
 * @author srosse, stephane.rosse@frentix.com, http://www.frentix.com
 *
 */
@Service
public class LectureModule extends AbstractSpringModule implements ConfigOnOff {
	
	private static final String LECTURE_ENABLED = "lecture.enabled";
	private static final String AUTHORIZED_ABSENCE_ENABLED = "lecture.authorized.absence.enabled";
	
	@Value("${lecture.enabled:true}")
	private boolean enabled;
	@Value("${lecture.authorized.absence.enabled:true}")
	private boolean authorizedAbsenceEnabled;
	
	@Autowired
	public LectureModule(CoordinatorManager coordinatorManager) {
		super(coordinatorManager);
	}

	@Override
	public void init() {
		//module enabled/disabled
		String enabledObj = getStringPropertyValue(LECTURE_ENABLED, true);
		if(StringHelper.containsNonWhitespace(enabledObj)) {
			enabled = "true".equals(enabledObj);
		}
	}

	@Override
	protected void initFromChangedProperties() {
		init();
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
		setStringProperty(LECTURE_ENABLED, Boolean.toString(enabled), true);
	}

	public boolean isAuthorizedAbsenceEnabled() {
		return authorizedAbsenceEnabled;
	}

	public void setAuthorizedAbsenceEnabled(boolean enabled) {
		this.authorizedAbsenceEnabled = enabled;
		setStringProperty(AUTHORIZED_ABSENCE_ENABLED, Boolean.toString(enabled), true);
	}
	
	
}
