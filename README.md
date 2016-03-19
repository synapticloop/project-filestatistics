[![Build Status](https://travis-ci.org/synapticloop/project-filestatistics.svg?branch=master)](https://travis-ci.org/synapticloop/project-filestatistics) [![Download](https://api.bintray.com/packages/synapticloop/maven/project-filestatistics/images/download.svg)](https://bintray.com/synapticloop/maven/project-filestatistics/_latestVersion) [![GitHub Release](https://img.shields.io/github/release/synapticloop/project-filestatistics.svg)](https://github.com/synapticloop/project-filestatistics/releases) [![Gradle Plugin Release](https://img.shields.io/badge/gradle%20plugin-1.0.2-blue.svg)](https://plugins.gradle.org/plugin/synapticloop.projectFilestatistics) 

> **This project requires JVM version of at least 1.7**




# project-filestatistics



> generate statistics about the files for the project


# Overview

This plugin outputs the number of lines of 

  - code
  - comments
  - blank

for all files within the `includes` list (and excluding the files within the `excludes` list)

### How it works

Each file is scanned, line by line and is determined to be one of the following:

  - blank line - either empy, or only containing whitespace
  - comments - either single or multi line - defined through the properties file
  - code - anything else remaining

See the section below for adding new definitions or over-riding existing definitions.



# Gradle plugin usage

## Build script snippet for use in all Gradle versions:

```
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.synapticloop:project-filestatistics:1.0.2"
  }
}

apply plugin: "synapticloop.projectFilestatistics"
```


## Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:

```
plugins {
  id "synapticloop.projectFilestatistics" version "1.0.2"
}
```



# Example output for this project...

Number of files and their break-up of types of lines



```

Line number report (NumberTextReporter)
=======================================
   File type     #    Code(      %)    Comment(      %)    Blank(      %)    Total(      %)  
------------  ----  ---------------  ------------------  ----------------  ----------------  
       .java    13    3982( 86.47%)        383(  8.32%)      240(  5.21%)     4605( 96.14%)  
         .md     2      46( 68.66%)          0(  0.00%)       21( 31.34%)       67(  1.40%)  
 .properties     2      44( 51.16%)         23( 26.74%)       19( 22.09%)       86(  1.80%)  
        .txt     2      29( 90.62%)          0(  0.00%)        3(  9.38%)       32(  0.67%)  
------------  ----  ---------------  ------------------  ----------------  ----------------  
     4 types    19    4101( 85.62%)        406(  8.48%)      283(  5.91%)     4790(100.00%)  
============  ====  ===============  ==================  ================  ================  
```

> see `src/docs/NumberTextReporter.txt`



A *quasi*-graphical representation of the percentages of types of line



```

Line number report (CumulativeBarTextReporter)
==============================================
   File type  
------------  0                   25                  50                   75                  100
              +---------------------------------------------------------------------+-------+----+
       .java  |#####################################################################|:::::::|    |
              +-------------------------------------------------------++-------------------------+
         .md  |#######################################################||                         |
              +-----------------------------------------+---------------------+------------------+
 .properties  |#########################################|:::::::::::::::::::::|                  |
              +------------------------------------------------------------------------++--------+
        .txt  |########################################################################||        |
              +------------------------------------------------------------------------++--------+

Key:
----
  '#' code
  ':' comment
  ' ' blank
```

> see `src/docs/CumulativeBarTextReporter.txt`

# Configuration

The plugin may be configured with the following properties:

```
projectFilestatistics {
	// the files to include in the generation of the statistics
	includes = [
		"src/main/**/*.*",
		"src/docs/**/*.*"
	]

	// the files to exlude from the generation
	excludes = [
	
	]

	// the output directory for the generated statistics
	outputDirectory = 'src/docs'

	// if you require to over-ride how single and multi-line comments work, or
	// need to add in some extensions which aren't built in...
	propertyFile = 'over-ride.properties'
}
```
> see `src/main/resources/project-filestatistics.properties` for the pre-defined types

### Property file over-riding

The over-ride property file allows definitions for existing single and multi-line properties to be over-ridden, or new definitions for single and multi-line comments to be added.

The general form is

```
# The following defines the single line comment, if the lines starts
# with the '//' then it is considered a single line comment
myextension.comment.single=//

# The following define the start and end of a multi-line comment, the
# line __must__ start with a '/*', and will continue until the end
# comment '*/' irrespective of where on the line it occurs
myextension.comment.multi.start=/*
myextension.comment.multi.end=*/
```

The in-built extensions and their properties are as follows:



```
#
# This file details the comments that are applicable to each of the file types
# is in the format of <file extension>.comment.(single|multi).[start|end]
#

# normal java commenting
java.comment.single=//
java.comment.multi.start=/*
java.comment.multi.end=*/

# normal groovy commenting
groovy.comment.single=//
groovy.comment.multi.start=/*
groovy.comment.multi.end=*/

# gradle settings - just like groovy
gradle.comment.single=//
gradle.comment.multi.start=/*
gradle.comment.multi.end=*/

# these are actually particular to MySQL
sql.comment.single=--
sql.comment.multi.start=/*
sql.comment.multi.end=*/

# properties files do not have a multi line comment format 
properties.comment.single=#

# xml doesn't have a different single line comment
xml.comment.multi.start=<!--
xml.comment.multi.end=-->

# xsl doesn't have a different single line comment
xsl.comment.multi.start=<!--
xsl.comment.multi.end=-->

#xslt - same as xsl
xslt.comment.multi.start=<!--
xslt.comment.multi.end=-->

#xsd - same as xsl
xsd.comment.multi.start=<!--
xsd.comment.multi.end=-->

#xsd - same as xsl
tld.comment.multi.start=<!--
tld.comment.multi.end=-->

# python - no multi line
py.comment.single=#

# shell - no multi line
sh.comment.single=#

# html - xml type
html.comment.multi.start=<!--
html.comment.multi.end=-->

# htm - xml type
htm.comment.multi.start=<!--
htm.comment.multi.end=-->

# javascript 
js.comment.multi.start=/*
js.comment.multi.end=*/
js.comment.single=//

# jsp - like java
jsp.comment.single=//
jsp.comment.multi.start=/*
jsp.comment.multi.end=*/

# css - multi line only
css.comment.multi.start=/*
css.comment.multi.end=*/

# sass/scss
scss.comment.single=//
scss.comment.multi.start=/*
scss.comment.multi.end=*/

# less
less.comment.single=//
less.comment.multi.start=/*
less.comment.multi.end=*/

```



# Building the Package

## *NIX/Mac OS X

From the root of the project, simply run

`./gradlew build`


## Windows

`./gradlew.bat build`


This will compile and assemble the artefacts into the `build/libs/` directory.

Note that this may also run tests (if applicable see the Testing notes)

# Artefact Publishing - Github

This project publishes artefacts to [GitHib](https://github.com/)

> Note that the latest version can be found [https://github.com/synapticloop/project-filestatistics/releases](https://github.com/synapticloop/project-filestatistics/releases)

As such, this is not a repository, but a location to download files from.

# Artefact Publishing - Bintray

This project publishes artefacts to [bintray](https://bintray.com/)

> Note that the latest version can be found [https://bintray.com/synapticloop/maven/project-filestatistics/view](https://bintray.com/synapticloop/maven/project-filestatistics/view)

## maven setup

this comes from the jcenter bintray, to set up your repository:

```
<?xml version="1.0" encoding="UTF-8" ?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd' xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
  <profiles>
    <profile>
      <repositories>
        <repository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>bintray</name>
          <url>http://jcenter.bintray.com</url>
        </repository>
      </repositories>
      <pluginRepositories>
        <pluginRepository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>central</id>
          <name>bintray-plugins</name>
          <url>http://jcenter.bintray.com</url>
        </pluginRepository>
      </pluginRepositories>
      <id>bintray</id>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>bintray</activeProfile>
  </activeProfiles>
</settings>
```

## gradle setup

Repository

```
repositories {
	maven {
		url  "http://jcenter.bintray.com" 
	}
}
```

or just

```
repositories {
	jcenter()
}
```

# Artefact Publishing - gradle plugin portal

This project publishes artefacts to [the gradle plugin portal](https://plugins.gradle.org/)

> Note that the latest version can be found [https://plugins.gradle.org/plugin/synapticloop.projectFilestatistics](https://plugins.gradle.org/plugin/synapticloop.projectFilestatistics)

## Dependencies - Gradle

```
dependencies {
	runtime(group: 'synapticloop', name: 'project-filestatistics', version: '1.0.2', ext: 'jar')

	compile(group: 'synapticloop', name: 'project-filestatistics', version: '1.0.2', ext: 'jar')
}
```

or, more simply for versions of gradle greater than 2.1

```
dependencies {
	runtime 'synapticloop:project-filestatistics:1.0.2'

	compile 'synapticloop:project-filestatistics:1.0.2'
}
```

## Dependencies - Maven

```
<dependency>
	<groupId>synapticloop</groupId>
	<artifactId>project-filestatistics</artifactId>
	<version>1.0.2</version>
	<type>jar</type>
</dependency>
```

## Dependencies - Downloads


You will also need to download the following dependencies:



### compile dependencies


**NOTE:** You may need to download any dependencies of the above dependencies in turn (i.e. the transitive dependencies)

# License

```
The MIT License (MIT)

Copyright (c) 2016 synapticloop

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```


--

> `This README.md file was hand-crafted with care utilising synapticloop`[`templar`](https://github.com/synapticloop/templar/)`->`[`documentr`](https://github.com/synapticloop/documentr/)

--

