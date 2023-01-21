<!-- GitHub Markdown Flavour. Align and percentages supported. -->
<img align="right" width=26% src="./assets/logo.webp" />

# Valkea

![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/bluelhf/Valkea/maven.yml?color=%23ffffff&logo=apachemaven&logoColor=%23ffffff) ![GitHub](https://img.shields.io/github/license/bluelhf/Valkea?color=%23ffffff&label=licence&logo=gnu)

Valkea is a [Paper](https://papermc.io) server plugin that adds configurable
spawn points, each with their own commands.

## Usage
Valkea has no built-in management commands. Instead, all configuration
is done via the configuration file, which is in YAML format (`.yml`).

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
