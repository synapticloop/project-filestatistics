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

The over-ride property file allows definitions for existing