# Repository Guidelines

## Project Structure & Module Organization

- `cropper/` 是核心库模块。代码在 `cropper/src/main/kotlin`，资源在 `cropper/src/main/res`，清单在 `cropper/src/main/AndroidManifest.xml`。
- `sample/` 是示例应用。代码在 `sample/src/main/kotlin`，资源在 `sample/src/main/res`。
- 测试主要在 `cropper/src/test/kotlin`，测试资源在 `cropper/src/test/resources`，Paparazzi 快照在 `cropper/src/test/snapshots`。

## Build, Test, and Development Commands

- `./gradlew build`：构建全部模块（库与示例）。
- `./gradlew testDebug`：运行单元测试（CI 使用此命令）。
- `./gradlew ktlint`：执行格式检查，规则来自 `.editorconfig`。
- `./gradlew licensee`：校验依赖许可证。
- `./gradlew :sample:installDebug`：安装示例应用（需要设备或模拟器）。

## Coding Style & Naming Conventions

- Kotlin/Gradle Kotlin DSL，缩进 2 空格，允许尾随逗号（见 `.editorconfig`）。
- 类名使用 PascalCase（例如 `CropImageView`），资源使用 `snake_case`（例如 `crop_image_view.xml`）。
- 包名遵循 `com.canhub.cropper` 与 `com.canhub.cropper.sample`，优先 AndroidX API。

## Testing Guidelines

- 主要框架：JUnit4、Robolectric、MockK、Paparazzi。
- 测试类命名与用例描述多采用可读句式（参考 `BitmapUtilsTest` 中反引号用例）。
- 若更新 UI 输出，确保同步更新 `cropper/src/test/snapshots`。

## Commit & Pull Request Guidelines

- 没有严格约定，但近期提交常用前缀如 `Fix:`、`Update:`、`Technical:`、`Security:`、`API:`，并包含 PR 编号 `(#123)`。
- 提交信息建议：简短动词开头 + 影响范围；若有行为变更请明确写出。
- PR 需包含变更说明、必要的复现步骤，涉及 UI 的请附示例界面截图。

## Security & Configuration Tips

- `customOutputUri` 必须是 `content://` 且扩展名需匹配压缩格式，否则会触发验证异常。
- `CropFileProvider` 配置在 `cropper/src/main/AndroidManifest.xml`，修改 authority 时请同步更新调用方。
