<!-- GitHub Markdown Flavour. Align and percentages supported. -->
<img align="right" width=26% src="./assets/logo.webp" />

# Valkea
![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/bluelhf/Valkea/maven.yml?color=%23ffffff&logo=apachemaven&logoColor=%23ffffff) ![GitHub](https://img.shields.io/github/license/bluelhf/Valkea?color=%23ffffff&label=licence&logo=gnu)
> Tukela on tuletta olla, vaiva suuri valkeatta,  
> ikävä inehmisien, ikävä itsen Ukonki.  
> — _The seventy-fifth poem, Kalevala_

Valkea is a [Paper](https://papermc.io) server plugin that adds configurable
spawn points, each with their own commands.

## Usage
Valkea has no built-in management commands. Instead, all configuration
is done via the configuration file, which is in YAML format (`.yml`).

### Installing Valkea
Valkea isn't published anywhere, because SpigotMC doesn't accept Paper-only
plugins and Hangar is still in the closed alpha development stage at the
time of writing[^1]. Nightly artifacts are uploaded to GitHub Actions, and retained for ninety days.
1. Download the ZIP from [GitHub Actions](https://nightly.link/bluelhf/Valkea/workflows/maven/main/Valkea%20JAR.zip) or [build Valkea yourself](#compiling).
2. Extract it to get the Valkea plugin JAR.
3. Place it in your server's <kbd>plugins</kbd> directory.
4. Restart the server.

### Configuring Valkea
As a little exercise, let's try removing one of the two default spawn
points included with Valkea, and adding our own one for the End.

1. Open the <kbd>plugins</kbd> > <kbd>Valkea</kbd> > <kbd>config.yml</kbd> file.
2. Write something a little like this:
   ```yaml
   spawn:
     display_name: "<rainbow>Rainbow spawn!</rainbow>"
     world: "minecraft:overworld"

     x: 143.5
     y: 73.0
     z: 269.5
   
     yaw: 45
     pitch: -90
   ```
3. Save the file.
4. Run the `/spawn` command to go to the spawn point. No reloads necessary!

## Compiling
You can always clone this repository manually and build it with [Maven](https://maven.apache.org/index.html). That being said,
if you have IntelliJ IDEA installed, the simplest way to compile Valkea is to use that instead:

1. <kbd>File</kbd> > </kbd>New</kbd> > </kbd>Project from Version Control...</kbd>
2. Enter `https://github.com/bluelhf/Valkea` as the repository URL
3. <kbd>Alt + Shift + F10</kbd> > <kbd>Build with Maven</kbd>
4. Get the Valkea JAR from <kbd>target</kbd>

### Footnotes
[^1]: Time of writing is 2023-01-21.
