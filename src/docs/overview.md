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

