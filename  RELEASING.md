# Releasing the Project

We use the official [Maven Release Plugin](https://maven.apache.org/maven-release/maven-release-plugin/usage/prepare-release.html)
to automate our release cycle.

If you want the plugin to automatically calculate the next version, 
use the [Auto](#auto) method. 
If you want to specify a custom version number interactively, 
use the [Manual](#manual) method.

### Auto

Use this method for standard non-interactive releases where Maven automatically increments the patch version (e.g., `1.8.14-SNAPSHOT` -> Release `1.8.14` -> Next Snapshot `1.8.15-SNAPSHOT`).

Before running, ensure your local `main` branch is clean and up to date:
```bash
git checkout main && git pull origin main
```

Run the batch-mode preparation:
```bash
mvn -B release:prepare
```

### Manual

Use this method if you want to manually specify custom version numbers or change the Git tag format interactively in the terminal prompt.

Before running, ensure your local `main` branch is clean and up to date:
```bash
git checkout main && git pull origin main
```

Run the interactive preparation:
```bash
mvn release:prepare
```
