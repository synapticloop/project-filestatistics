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

