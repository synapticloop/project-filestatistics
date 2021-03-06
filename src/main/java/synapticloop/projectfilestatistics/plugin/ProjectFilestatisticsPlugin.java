package synapticloop.projectfilestatistics.plugin;

/*
 * Copyright (c) 2016 Synapticloop.
 * 
 * All rights reserved.
 *
 * This source code and any derived binaries are covered by the terms and
 * conditions of the Licence agreement ("the Licence").  You may not use this
 * source code or any derived binaries except in compliance with the Licence.
 * A copy of the Licence is available in the file named LICENCE shipped with
 * this source code or binaries.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations
 * under the Licence.
 */

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class ProjectFilestatisticsPlugin implements Plugin<Project> {


	@Override
	public void apply(Project project) {
		project.getExtensions().create("projectFilestatistics", ProjectFilestatisticsPluginExtension.class);
		project.getTasks().create("projectFilestatistics", ProjectFilestatisticsTask.class);
	}

}
