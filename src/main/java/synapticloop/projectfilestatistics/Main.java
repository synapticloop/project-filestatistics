package synapticloop.projectfilestatistics;

/*
 * Copyright (c) 2011-2015 synapticloop.
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

	private static void printExample() {
		StringBuilder stringBuilder = new StringBuilder(1024);
		char[] chars = new char[1024];
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(
							Main.class.getResourceAsStream("/filestatistics-build.xml")));
			int numRead = 0;
			while((numRead = bufferedReader.read(chars)) != -1) {
				String readData = String.valueOf(chars, 0, numRead);
				stringBuilder.append(readData);
			}
			bufferedReader.close();
		} catch (IOException jiioex) {
			jiioex.printStackTrace();
		}
		System.out.println(stringBuilder.toString());
	}

	private static void printUsage() {
		System.out.println("type:");
		System.out.println("  java -jar project-filestatistics.jar example\n");
		System.out.println("for a build file example.\n");

	}

	private static void printSorry() {
		System.out.println("\nWe are truly sorry, this jar cannot be run on the command line.");
		System.out.println("It is designed to be part of your build chain and invoked through ant.\n");
		printUsage();
	}

	private static void printUnknownArguments(String[] args) {
		System.out.println("\nOh dear!\n");
		System.out.println("We seem to have problem understanding what you wanted to do.\n");
		System.out.println("We received the following command line arguments:");
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];
			System.out.println("  " + i + ": " + arg);
		}
		System.out.println("");
	}

	public static void main(String[] args) {
		switch(args.length) {
		case 0:
			printSorry();
			printUsage();
			break;
		case 1:
			if(args[0].compareToIgnoreCase("example") == 0) {
				printExample();
			} else {
				printUnknownArguments(args);
				printUsage();
			}
			break;
		default:
			printUnknownArguments(args);
			printUsage();
			break;
		}
	}
}
