package synapticloop.projectfilestatistics.ant;

/*
 * Copyright (c) 2011-2016 Synapticloop.
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

import org.apache.tools.ant.Task;

import synapticloop.projectfilestatistics.util.Constants;

public class ListPluginsTask extends Task {
	public void execute() {
		String[] plugins = Constants.DEFAULT_PLUGIN_LIST.split(",");
		System.out.println("Available built-in plugin names:");
		for (int i = 0; i < plugins.length; i++) {
			String plugin = plugins[i];
			System.out.println("\t" + plugin);
		}
	}

}
