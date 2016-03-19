[![Build Status](https://travis-ci.org/synapticloop/project-filestatistics.svg?branch=master)](https://travis-ci.org/synapticloop/project-filestatistics) [![Download](https://api.bintray.com/packages/synapticloop/maven/project-filestatistics/images/download.svg)](https://bintray.com/synapticloop/maven/project-filestatistics/_latestVersion) [![GitHub Release](https://img.shields.io/github/release/synapticloop/project-filestatistics.svg)](https://github.com/synapticloop/project-filestatistics/releases) [![Gradle Plugin Release](https://img.shields.io/badge/gradle%20plugin-1.0.0-blue.svg)](https://plugins.gradle.org/plugin/synapticloop.projectFilestatistics) 

> **This project requires JVM version of at least 1.7**




# project-filestatistics



> generate statistics about the files for the project


# Overview

This plugin outputs the number of lines of 

  - code
  - comments
  - blank

for all files within the included list

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
    classpath "gradle.plugin.synapticloop:project-filestatistics:1.0.0"
  }
}

apply plugin: "synapticloop.projectFilestatistics"
```


## Build script snippet for new, incubating, plugin mechanism introduced in Gradle 2.1:

```
plugins {
  id "synapticloop.projectFilestatistics" version "1.0.0"
}
```



# Example output for this project...

Number of files and their break-up of types of lines



```

Line number report (NumberTextReporter)
=======================================
   File type     #    Code(      %)    Comment(      %)    Blank(      %)    Total(      %)  
------------  ----  ---------------  ------------------  ----------------  ----------------  
         .MF     1       1(100.00%)          0(  0.00%)        0(  0.00%)        1(  0.02%)  
       .java    14    4089( 86.36%)        390(  8.24%)      256(  5.41%)     4735( 94.29%)  
         .md     2      22( 68.75%)          0(  0.00%)       10( 31.25%)       32(  0.64%)  
 .properties     3      75( 46.58%)         52( 32.30%)       34( 21.12%)      161(  3.21%)  
        .txt     2      29( 90.62%)          0(  0.00%)        3(  9.38%)       32(  0.64%)  
        .xml     1      44( 72.13%)          8( 13.11%)        9( 14.75%)       61(  1.21%)  
------------  ----  ---------------  ------------------  ----------------  ----------------  
     6 types    23    4260( 84.83%)        450(  8.96%)      312(  6.21%)     5022(100.00%)  
============  ====  ===============  ==================  ================  ================  
```

> see `src/docs/NumberTextReporter.txt`



A *quasi*-graphical representation of the percentages of types of line



```

Line number report (CumulativeBarTextReporter)
==============================================
   File type  
------------  0                   25                  50                   75                  100
              +--------------------------------------------------------------------------------+++
         .MF  |################################################################################|||
              +---------------------------------------------------------------------+-------+----+
       .java  |#####################################################################|:::::::|    |
              +-------------------------------------------------------++-------------------------+
         .md  |#######################################################||                         |
              +-------------------------------------+--------------------------+-----------------+
 .properties  |#####################################|::::::::::::::::::::::::::|                 |
              +------------------------------------------------------------------------++--------+
        .txt  |########################################################################||        |
              +----------------------------------------------------------+----------+------------+
        .xml  |##########################################################|::::::::::|            |
              +----------------------------------------------------------+----------+------------+

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
}


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
	runtime(group: 'synapticloop', name: 'project-filestatistics', version: '1.0.0', ext: 'jar')

	compile(group: 'synapticloop', name: 'project-filestatistics', version: '1.0.0', ext: 'jar')
}
```

or, more simply for versions of gradle greater than 2.1

```
dependencies {
	runtime 'synapticloop:project-filestatistics:1.0.0'

	compile 'synapticloop:project-filestatistics:1.0.0'
}
```

## Dependencies - Maven

```
<dependency>
	<groupId>synapticloop</groupId>
	<artifactId>project-filestatistics</artifactId>
	<version>1.0.0</version>
	<type>jar</type>
</dependency>
```

## Dependencies - Downloads


You will also need to download the following dependencies:



### compile dependencies

  - org.apache.ant:ant:1.8.4: (It may be available on one of: [bintray](https://bintray.com/org.apache.ant/maven/ant/1.8.4/view#files/org.apache.ant/ant/1.8.4) [mvn central](http://search.maven.org/#artifactdetails|org.apache.ant|ant|1.8.4|jar))
  - synapticloop:simpleusage:1.1.1: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simpleusage/1.1.1/view#files/synapticloop/simpleusage/1.1.1) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simpleusage|1.1.1|jar))
  - synapticloop:simplelogger:1.1.0: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simplelogger/1.1.0/view#files/synapticloop/simplelogger/1.1.0) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simplelogger|1.1.0|jar))
  - commons-io:commons-io:2.4: (It may be available on one of: [bintray](https://bintray.com/commons-io/maven/commons-io/2.4/view#files/commons-io/commons-io/2.4) [mvn central](http://search.maven.org/#artifactdetails|commons-io|commons-io|2.4|jar))
  - org.json:json:20160212: (It may be available on one of: [bintray](https://bintray.com/org.json/maven/json/20160212/view#files/org.json/json/20160212) [mvn central](http://search.maven.org/#artifactdetails|org.json|json|20160212|jar))


### runtime dependencies

  - synapticloop:simpleusage:1.1.1: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simpleusage/1.1.1/view#files/synapticloop/simpleusage/1.1.1) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simpleusage|1.1.1|jar))
  - synapticloop:simplelogger:1.1.0: (It may be available on one of: [bintray](https://bintray.com/synapticloop/maven/simplelogger/1.1.0/view#files/synapticloop/simplelogger/1.1.0) [mvn central](http://search.maven.org/#artifactdetails|synapticloop|simplelogger|1.1.0|jar))
  - commons-io:commons-io:2.4: (It may be available on one of: [bintray](https://bintray.com/commons-io/maven/commons-io/2.4/view#files/commons-io/commons-io/2.4) [mvn central](http://search.maven.org/#artifactdetails|commons-io|commons-io|2.4|jar))
  - org.json:json:20160212: (It may be available on one of: [bintray](https://bintray.com/org.json/maven/json/20160212/view#files/org.json/json/20160212) [mvn central](http://search.maven.org/#artifactdetails|org.json|json|20160212|jar))


### testCompile dependencies

  - junit:junit:4.12: (It may be available on one of: [bintray](https://bintray.com/junit/maven/junit/4.12/view#files/junit/junit/4.12) [mvn central](http://search.maven.org/#artifactdetails|junit|junit|4.12|jar))
  - org.mockito:mockito-all:1.10.19: (It may be available on one of: [bintray](https://bintray.com/org.mockito/maven/mockito-all/1.10.19/view#files/org.mockito/mockito-all/1.10.19) [mvn central](http://search.maven.org/#artifactdetails|org.mockito|mockito-all|1.10.19|jar))


### testRuntime dependencies

  - junit:junit:4.12: (It may be available on one of: [bintray](https://bintray.com/junit/maven/junit/4.12/view#files/junit/junit/4.12) [mvn central](http://search.maven.org/#artifactdetails|junit|junit|4.12|jar))
  - org.mockito:mockito-all:1.10.19: (It may be available on one of: [bintray](https://bintray.com/org.mockito/maven/mockito-all/1.10.19/view#files/org.mockito/mockito-all/1.10.19) [mvn central](http://search.maven.org/#artifactdetails|org.mockito|mockito-all|1.10.19|jar))

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

