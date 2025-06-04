# Paste with Commas Plugin

[![Build and Test](https://github.com/YOUR_USERNAME/paste-with-commas-plugin/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/YOUR_USERNAME/paste-with-commas-plugin/actions/workflows/build-and-test.yml)
[![CI](https://github.com/YOUR_USERNAME/paste-with-commas-plugin/actions/workflows/ci.yml/badge.svg)](https://github.com/YOUR_USERNAME/paste-with-commas-plugin/actions/workflows/ci.yml)

An IntelliJ IDEA plugin that provides intelligent paste functionality with automatic comma insertion and quote wrapping.

## Features

### 🧠 Intelligent Paste with Commas
- **Smart Detection**: Automatically detects numbers vs text content
- **Numbers**: `1\n2\n3` → `1,\n2,\n3` (no quotes)
- **Text**: `apple\nbanana\ncherry` → `'apple',\n'banana',\n'cherry'` (with single quotes)
- **Mixed Content**: Uses quotes for all lines when content is mixed

### 📋 Additional Paste Options
Access via **Paste with Commas (additional)** submenu:
- **Simple**: Just adds commas, no quotes
- **Single Quotes**: Always wraps with single quotes + commas
- **Double Quotes**: Always wraps with double quotes + commas

## Menu Structure
```
Right-click Context Menu:
├── Paste
├── Paste with Commas (intelligent)
└── Paste with Commas (additional) ▶
    ├── Paste with Commas (simple)
    ├── Paste with Commas + '
    └── Paste with Commas + "
```

## Examples

| Input | Intelligent Output | Simple Output | Single Quotes | Double Quotes |
|-------|-------------------|---------------|---------------|---------------|
| `1\n2\n3` | `1,\n2,\n3` | `1,\n2,\n3` | `'1',\n'2',\n'3'` | `"1",\n"2",\n"3"` |
| `a\nb\nc` | `'a',\n'b',\n'c'` | `a,\nb,\nc` | `'a',\n'b',\n'c'` | `"a",\n"b",\n"c"` |
| `1\ntext\n3` | `'1',\n'text',\n'3'` | `1,\ntext,\n3` | `'1',\n'text',\n'3'` | `"1",\n"text",\n"3"` |

## Installation

### From Marketplace (Coming Soon)
1. Go to `File → Settings → Plugins`
2. Search for "Paste with Commas"
3. Install and restart IntelliJ IDEA

### Manual Installation
1. Download the latest release from [Releases](https://github.com/YOUR_USERNAME/paste-with-commas-plugin/releases)
2. Go to `File → Settings → Plugins`
3. Click ⚙️ → `Install Plugin from Disk...`
4. Select the downloaded `.zip` file
5. Restart IntelliJ IDEA

## Development

### Building from Source
```bash
git clone https://github.com/YOUR_USERNAME/paste-with-commas-plugin.git
cd paste-with-commas-plugin
./gradlew build
```

### Running Tests
```bash
./gradlew test
```

### Running in Development
```bash
./gradlew runIde
```

## Requirements
- IntelliJ IDEA 2024.2+ (Community or Ultimate)
- Java 21+

## Technology Stack
- **Language**: Kotlin
- **Framework**: IntelliJ Platform SDK
- **Build Tool**: Gradle with IntelliJ Platform Gradle Plugin
- **Testing**: JUnit 5 + IntelliJ Platform Test Framework

## Architecture

### Core Components
- **`BasePasteAction`**: Abstract base class with common clipboard/editor logic
- **`PasteWithCommasAction`**: Intelligent main action with number/text detection
- **`PasteWithCommasSimpleAction`**: Simple comma-only functionality
- **`PasteWithCommasAndQuotesAction`**: Single quotes + commas
- **`PasteWithCommasAndDoubleQuotesAction`**: Double quotes + commas

### Test Coverage
- **86 total tests**: 52 unit + 21 integration + 13 menu tests
- **Unit Tests**: Logic verification for all paste transformations
- **Integration Tests**: Real editor and clipboard integration
- **Menu Tests**: Action registration and menu structure verification

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Add tests for your changes
5. Ensure all tests pass (`./gradlew test`)
6. Commit your changes (`git commit -m 'Add amazing feature'`)
7. Push to the branch (`git push origin feature/amazing-feature`)
8. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Changelog

### v1.0.0
- ✨ Initial release
- 🧠 Intelligent paste with automatic number/text detection
- 📋 Four paste variants (intelligent, simple, single quotes, double quotes)
- 🎨 Custom icons for each action
- 📱 Submenu organization
- ✅ Comprehensive test suite (86 tests)
- 🔧 Refactored architecture with shared base class

## Support

If you encounter any issues or have feature requests, please [open an issue](https://github.com/YOUR_USERNAME/paste-with-commas-plugin/issues).