## Why

当前库在 `CropShape.RECTANGLE` 下只能绘制四个角标（L 角），无法实现“角标外扩 + 四边中点短线”的证件/文档类裁剪视觉样式。该样式在扫描类与试卷类场景中较常见，且现有用户已可通过负值 `borderCornerOffset` 将 L 角外移，因此现在补齐中点短线可以以较小改动满足完整样式需求。

## What Changes

- 为矩形裁剪框增加“四边中点短线”绘制能力（上/下/左/右各一段）。
- 中点短线复用现有角标视觉参数：`borderCornerThickness`、`borderCornerColor`、`borderCornerLength`。
- 中点短线与角标共用 `borderCornerOffset`，并沿用当前符号语义（正值向内，负值向外）。
- 绘制范围限制为 `CropShape.RECTANGLE`；当 `cornerShape = OVAL` 时禁用中点短线。
- 保持现有角标绘制与触控逻辑不变，不引入新的拖拽手柄类型。

## Capabilities

### New Capabilities
- `crop-overlay`: 为矩形裁剪框提供“角标 + 四边中点短线”的可配置视觉样式能力。

### Modified Capabilities
- 无

## Impact

- 主要影响文件：`cropper/src/main/kotlin/com/canhub/cropper/CropOverlayView.kt`（新增中点短线绘制路径）。
- 可能影响文件：`cropper/src/main/kotlin/com/canhub/cropper/CropImageOptions.kt`、`cropper/src/main/res/values/attrs.xml`、`cropper/src/main/kotlin/com/canhub/cropper/CropImageView.kt`（若引入开关配置）。
- 测试影响：需要补充或更新裁剪框渲染测试/快照，覆盖 `RECTANGLE` + `cornerShape` 组合与 `borderCornerOffset` 正负值场景。

