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
package org.olat.course.nodes.scorm;

import org.olat.course.assessment.handler.AssessmentConfig;
import org.olat.modules.ModuleConfiguration;

/**
 * 
 * Initial date: 19 Aug 2019<br>
 * @author uhensler, urs.hensler@frentix.com, http://www.frentix.com
 *
 */
public class ScormAssessmentConfig implements AssessmentConfig {
	
	private final ModuleConfiguration config;

	public ScormAssessmentConfig(ModuleConfiguration config) {
		this.config = config;
	}

	@Override
	public boolean isAssessable() {
		return true;
	}

	@Override
	public boolean isEvaluationPersisted() {
		return true;
	}

	@Override
	public boolean isEvaluationCalculated() {
		return false;
	}

	@Override
	public boolean hasScore() {
		boolean assessable = config.getBooleanSafe(ScormEditController.CONFIG_ISASSESSABLE, true);
		if (assessable) {
			String type = config.getStringValue(ScormEditController.CONFIG_ASSESSABLE_TYPE,
					ScormEditController.CONFIG_ASSESSABLE_TYPE_SCORE);
			return ScormEditController.CONFIG_ASSESSABLE_TYPE_SCORE.equals(type);
		}
		return false;
	}

	@Override
	public Float getMaxScore() {
		// According to SCORM Standard, SCORE is between 0 and 100.
		return Float.valueOf(100);
	}

	@Override
	public Float getMinScore() {
		// According to SCORM Standard, SCORE is between 0 and 100.
		return Float.valueOf(0);
	}
	
	@Override
	public boolean hasPassed() {
		return config.getBooleanSafe(ScormEditController.CONFIG_ISASSESSABLE, true);
	}
	
	@Override
	public Float getCutValue() {
		int cutValue = config.getIntegerSafe(ScormEditController.CONFIG_CUTVALUE, 0); 
		return Float.valueOf(cutValue);
	}
	
	@Override
	public Mode getCompletionMode() {
		return Mode.none;
	}
	
	@Override
	public boolean hasAttempts() {
		return config.getBooleanSafe(ScormEditController.CONFIG_ISASSESSABLE, true);
	}

	@Override
	public boolean hasComment() {
		return false;
	}

	@Override
	public boolean hasIndividualAsssessmentDocuments() {
		return false;
	}

	@Override
	public boolean hasStatus() {
		return false;
	}

	@Override
	public boolean isAssessedBusinessGroups() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return config.getBooleanSafe(ScormEditController.CONFIG_ISASSESSABLE, true);
	}

	@Override
	public boolean isBulkEditable() {
		return false;
	}

	@Override
	public boolean hasEditableDetails() {
		return config.getBooleanSafe(ScormEditController.CONFIG_ISASSESSABLE, true);
	}
	
	@Override
	public boolean isExternalGrading() {
		return false;
	}

	@Override
	public boolean isObligationOverridable() {
		return true;
	}

}
