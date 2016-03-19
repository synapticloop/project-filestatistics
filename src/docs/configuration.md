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