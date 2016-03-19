package synapticloop.projectfilestatistics.gradle.exception;

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

public class ProjectFilestatisticsException extends Exception {
	private static final long serialVersionUID = 4516173724143453780L;

	public ProjectFilestatisticsException(String message) {
		super(message);
	}

	public ProjectFilestatisticsException(Throwable cause) {
		super(cause);
	}

	public ProjectFilestatisticsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectFilestatisticsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
