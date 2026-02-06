## Context

当前 `CropOverlayView` 在 `CropShape.RECTANGLE` 模式下已支持角标与中点短线样式，且 `borderCornerOffset` 允许负值外扩。但当裁剪框贴近视图边缘时，外扩 L 角会超出 `CropOverlayView` 边界并被裁剪。用户希望在负偏移时自动获得可见性保护，而不是引入新的显式配置。

约束与现状：
- 现有绘制逻辑集中在 `cropper/src/main/kotlin/com/canhub/cropper/CropOverlayView.kt`。
- 图片显示矩阵和边界约束集中在 `cropper/src/main/kotlin/com/canhub/cropper/CropImageView.kt`。
- `cropPoints` 通过逆矩阵映射生成，要求保持裁剪结果坐标语义稳定。
- 触控手柄逻辑由 `CropWindowHandler`/`CropWindowMoveHandler` 管理，不应引入新的交互类型。

## Goals / Non-Goals

**Goals:**
- 在 `borderCornerOffset < 0` 时自动启用 visual gutter，使外扩 L 角在常见场景下完整可见。
- 不新增用户显式配置开关来控制该自动行为。
- 保持 `borderCornerOffset` 符号语义不变（正值向内、负值向外）。
- 不改变裁剪结果坐标语义与触控模型。

**Non-Goals:**
- 不修改 `CropShape.OVAL`、`RECTANGLE_VERTICAL_ONLY`、`RECTANGLE_HORIZONTAL_ONLY` 的视觉策略。
- 不新增独立的中点偏移参数或新的拖拽手柄类型。
- 不保证在所有极端组合（极大负偏移、极小视图）下完全零裁剪，而是保证可解释且稳定的自动策略。

## Decisions

### 1) 中点短线继续保留在边框层绘制
- 决策：中点短线仍位于 `drawBorders()` 与 `drawCorners()` 之间。
- 原因：复用既有几何计算，且不影响触控判断。

### 2) 中点短线继续复用角标参数
- 决策：中点线复用 `borderCornerThickness`、`borderCornerColor`、`borderCornerLength`、`borderCornerOffset`。
- 原因：视觉一致、API 面最小。

### 3) 触发条件：仅负偏移自动启用 visual gutter
- 决策：仅当以下条件同时成立时，自动应用 visual gutter：
  - `borderCornerOffset < 0`
  - `cropShape == RECTANGLE`
  - `cornerShape != OVAL`
- 原因：仅在“需要向外扩且目标样式有效”时介入，避免影响其他形态。

### 4) visual gutter 作为内部策略，不暴露显式配置
- 决策：不新增 `enableAutoGutter` 等公开配置。
- 原因：用户明确要求“自动执行、不需要显示配置”，并减少配置复杂度。
- 备选方案：增加显式开关（可控但复杂），本次不采用。

### 5) gutter 作用于“显示区域”，不改变裁剪坐标换算语义
- 决策：通过收缩图片在 View 内的可视放置区域（display content rect）来提供外扩绘制空间；`cropPoints` 仍使用当前逆矩阵映射生成。
- 原因：可在提升可见性的同时维持结果坐标一致性。

## Risks / Trade-offs

- [风险] 自动 gutter 会改变负偏移场景下的历史视觉布局 → [缓解] 将触发条件限定为负偏移 + 矩形有效组合，并在变更说明中明确。
- [风险] 图片显示区域收缩可能改变用户主观“铺满”感受 → [缓解] gutter 仅在负偏移时生效，且按最小需要值计算。
- [风险] 矩阵边界计算复杂度上升，可能引入缩放/平移边界回归 → [缓解] 增加针对 `applyImageMatrix` 的回归测试与手工验证清单。

## Migration Plan

1. 保留中点短线核心能力与现有 offset 语义。
2. 在负偏移路径接入自动 gutter，限定触发条件。
3. 验证 `cropPoints`、`cropRect` 在负偏移场景下保持语义稳定。
4. 在 sample/README 说明“负偏移自动可见性保护”为默认行为。

## Open Questions

- 自动 gutter 的最小计算公式是否加入固定 safety 像素（例如 1dp）以抵消抗锯齿误差。
- 是否需要在 release note 中显式标记“负偏移视觉行为调整”。
