plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "1.9.25"
    id("org.jetbrains.intellij.platform") version "2.3.0"
}

group = "com.nick"
version = "1.0.2"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        create("IC", "2024.2.5")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        // Add necessary plugin dependencies for compilation here, example:
        // bundledPlugin("com.intellij.java")
    }
    
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "241"
            untilBuild = "251.*"
        }

        changeNotes = """
            <h3>1.0.2 - Bug Fix Release</h3>
            <ul>
                <li><b>Fixed Trailing Newlines</b> - Correctly handles input with trailing empty lines (e.g., "a\nb\n\n" → "a,\nb")</li>
                <li><b>Enhanced Test Coverage</b> - Added comprehensive tests for trailing newline scenarios</li>
                <li><b>Improved Reliability</b> - All paste variants now properly trim trailing empty content</li>
            </ul>
            
            <h3>1.0.1 - Extended Compatibility</h3>
            <ul>
                <li><b>Broader Compatibility</b> - Now supports IntelliJ IDEA 2024.1+ through 2025.1+ and newer versions</li>
                <li><b>Updated Build Range</b> - Compatible with build 241 through 251.*</li>
                <li><b>Enhanced Testing</b> - Added platform compatibility verification for 2024.1+</li>
            </ul>
            
            <h3>1.0.0 - Initial Release</h3>
            <ul>
                <li><b>Intelligent Paste with Commas</b> - Automatically detects numbers vs text and applies appropriate formatting</li>
                <li><b>Multiple Paste Options</b> - Simple, single quotes, and double quotes variants</li>
                <li><b>Smart Menu Organization</b> - Main action with submenu for additional options</li>
                <li><b>Custom Icons</b> - Visual distinction for each paste type</li>
                <li><b>Comprehensive Testing</b> - 95+ tests ensuring reliability</li>
            </ul>
        """.trimIndent()
    }
}

tasks {
    // Set the JVM compatibility versions for 2024.1+ support
    withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "17"
    }
    
    test {
        useJUnitPlatform()
    }
    
    // Override the until-build to support wider range of IDE versions
    patchPluginXml {
        untilBuild = "251.*"
    }
}
