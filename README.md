# Lesshint linter for IntelliJ

This is a simple linter plugin for the IntelliJ Idea IDE. It assumes a global installation of
[Lesshint](https://github.com/lesshint/lesshint) in the environment. The plugin will underline
errors reported by the linter for `.css` and `.less` files.

## Development

The code does not currently perform any form of error handling, so if something is not working it's
easiest to manually debug the code. To debug, just open the project folder in IntelliJ Idea and
set breakpoints where necessary. `Shift + F9` will run a second instance of IntelliJ with the
plugin pre-installed.

## Deployment

Open the project root in IntelliJ, head to `Build -> Prepare Plugin Module ... for deployment`.
This will create a `.jar` file which you can move to your IntelliJ plugins folder. More detailed
instructions and OS-specific directories can be found in
[the official guide](https://www.jetbrains.org/intellij/sdk/docs/basics/getting_started/deploying_plugin.html)
and on
[the official website](https://intellij-support.jetbrains.com/hc/en-us/articles/206544519-Directories-used-by-the-IDE-to-store-settings-caches-plugins-and-logs).