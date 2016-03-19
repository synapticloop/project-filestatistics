package synapticloop.projectfilestatistics.gradle.plugin;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2016 Synapticloop.
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

public class ProjectFilestatisticsPluginExtension {
	private List<String> includes = new ArrayList<String>();
	private List<String> excludes = new ArrayList<String>();

	private String directory = ".";
	private boolean ignoreBinary = false;
	private String propertyFile = null;
	private String outputDirectory = ".";

	public String getDirectory() {
		return(directory);
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public boolean getIgnoreBinary() {
		return ignoreBinary;
	}

	public void setIgnoreBinary(boolean ignoreBinary) {
		this.ignoreBinary = ignoreBinary;
	}

	public String getPropertyFile() {
		return propertyFile;
	}

	public void setPropertyFile(String propertyFile) {
		this.propertyFile = propertyFile;
	}

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	public List<String> getIncludes() {
		return includes;
	}

	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	public List<String> getExcludes() {
		return excludes;
	}

	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}
}
