## Why

当前库在 `CropShape.RECTANGLE` 下只能绘制四个角标（L 角），无法实现“角标外扩 + 四边中点短线”的证件/文档类裁剪视觉样式。并且当 `borderCornerOffset` 为负值且裁剪框贴近视图边缘时，外扩 L 角会被 View 边界裁剪，影响可见性与一致性。

## What Changes

- 为矩形裁剪框增加“四边中点短线”绘制能力（上/下/左/右各一段）。
- 中点短线复用现有角标视觉参数：`borderCornerThickness`、`borderCornerColor`、`borderCornerLength`。
- 中点短线与角标共用 `borderCornerOffset`，并沿用当前符号语义（正值向内，负值向外）。
- 绘制范围限制为 `CropShape.RECTANGLE`；当 `cornerShape = OVAL` 时禁用中点短线。
- 当 `borderCornerOffset < 0` 且满足矩形样式条件时，自动启用显示留白（visual gutter）以避免外扩 L 角被 View 边界裁剪。
- visual gutter 为内部自动行为，不新增用户显式开关配置。
- 保持现有角标绘制与触控逻辑不变，不引入新的拖拽手柄类型。

## Capabilities

### New Capabilities
- `crop-overlay`: 为矩形裁剪框提供“角标 + 四边中点短线”的可配置视觉样式能力，以及负偏移下的自动可见性保护能力。

### Modified Capabilities
- 无

## Impact

- 主要影响文件：`cropper/src/main/kotlin/com/canhub/cropper/CropOverlayView.kt`（中点短线绘制路径与条件）。
- 可能影响文件：`cropper/src/main/kotlin/com/canhub/cropper/CropImageView.kt`（图片显示区域与矩阵边界计算）、`cropper/src/main/kotlin/com/canhub/cropper/CropImageOptions.kt`（如需收敛显式开关语义）。
- 测试影响：需要补充或更新裁剪框渲染测试/快照，覆盖 `RECTANGLE` + `cornerShape` 组合、`borderCornerOffset` 正负值场景，以及负偏移自动留白触发/不触发路径。
