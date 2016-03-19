package synapticloop.projectfilestatistics.plugin;

/*
 * Copyright (c) 2011-2015 Synapticloop.
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
	private boolean verbose = false;

	private String directory = ".";
	private String extension = "md";

	public String getDirectory() {
		return(directory);
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public boolean getVerbose() {
		return this.verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}
}
