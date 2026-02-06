## Context

当前 `CropOverlayView` 在 `CropShape.RECTANGLE` 模式下仅绘制四个角标（L 角），不支持四边中点短线；而 `borderCornerOffset` 已支持通过正负值控制角标相对边框的内外偏移。需求是补齐矩形样式中的四边中点短线，并与现有角标参数保持一致，避免引入新的几何语义。

约束与现状：
- 现有绘制逻辑集中在 `cropper/src/main/kotlin/com/canhub/cropper/CropOverlayView.kt`。
- 现有触控手柄由 `CropWindowHandler`/`CropWindowMoveHandler` 管理，不需要新增交互类型。
- 用户已确认：中点短线复用 `borderCornerOffset`，且符号语义沿用现状（正值向内、负值向外）。
- 用户已确认：该样式仅用于 `CropShape.RECTANGLE`，`cornerShape = OVAL` 时禁用。

## Goals / Non-Goals

**Goals:**
- 为 `CropShape.RECTANGLE` 提供“角标 + 四边中点短线”样式。
- 中点短线复用 `borderCornerThickness`、`borderCornerColor`、`borderCornerLength`、`borderCornerOffset`。
- 保持 offset 语义与当前角标绘制一致，避免同一参数出现双语义。
- 通过配置开关实现向后兼容，避免默认视觉回归。

**Non-Goals:**
- 不修改 `CropShape.OVAL`、`RECTANGLE_VERTICAL_ONLY`、`RECTANGLE_HORIZONTAL_ONLY` 的现有表现。
- 不新增拖拽手势、命中区域或新的 `MoveHandler.Type`。
- 不在本次变更中引入新的独立“中点偏移”参数。

## Decisions

### 1) 将中点短线绘制放入 `CropOverlayView` 的边框层
- 决策：在 `drawBorders()` 与 `drawCorners()` 之间增加中点短线绘制（例如新增 `drawMiddleSegments()`）。
- 原因：
  - 与现有边框/角标绘制同层，复用 `RectF` 与 `Paint` 计算。
  - 避免影响 `CropWindowHandler` 触控判断。
- 备选方案：
  - 将中点短线并入 `drawCorners()`：可行，但会让 `RECTANGLE`/`OVAL`/only 模式分支更复杂，可读性更差。

### 2) 中点短线完全复用现有角标视觉参数
- 决策：中点短线使用 `mBorderCornerPaint` 和 `mBorderCornerLength`。
- 原因：
  - 保持整体视觉一致（粗细、颜色、长度）。
  - 减少新增 API 面，降低维护成本。
- 备选方案：
  - 为中点短线新增独立厚度/颜色/长度参数：灵活但会快速膨胀配置复杂度。

### 3) 中点短线与角标共用 `borderCornerOffset`
- 决策：中点线中心与边框的法线偏移使用 `borderCornerOffset`，符号语义沿用现状（正值向内，负值向外）。
- 原因：
  - 用户明确要求沿用现有语义。
  - 避免同一组件出现两套 offset 解释。
- 备选方案：
  - 增加独立 `middleSegmentOffset`：可降低耦合，但违背本次“最小改动”目标。

### 4) 仅在 `RECTANGLE` 且 `cornerShape != OVAL` 时绘制
- 决策：中点短线绘制条件为 `cropShape == RECTANGLE && cornerShape != OVAL`。
- 原因：
  - 与用户确认一致。
  - 避免圆角点样式与线段样式混搭导致观感不一致。
- 备选方案：
  - 在 `cornerShape = OVAL` 也绘制中点短线：视觉冲突风险高，不采纳。

### 5) 新增可选开关，默认关闭
- 决策：新增一个 Boolean 配置项（例如 `showMiddleSegments`，对应 XML attr），默认 `false`。
- 原因：
  - 默认不改变已有应用的裁剪框外观，避免回归。
  - 新样式按需启用。
- 备选方案：
  - 默认开启：实现简单，但会改变所有 `RECTANGLE` 现有 UI，回归风险高。

## Risks / Trade-offs

- [风险] 参数复用导致可配置性受限（中点线无法独立调节） → [缓解] 先满足最小能力，若后续有需求再增独立参数。
- [风险] 当裁剪框贴近视图边缘且 `borderCornerOffset < 0` 时，外扩线段可能被裁剪 → [缓解] 在文档中明确该视觉边界行为，并在样例中验证。
- [风险] 新增开关会增加 `CropImageOptions`/`attrs`/解析链路的一致性维护成本 → [缓解] 增加契约测试，覆盖 options→attrs→overlay 的传递。

## Migration Plan

1. 新增配置项并保持默认关闭，发布后对旧调用方无行为变化。
2. 在 sample 中增加显式开关示例，展示目标样式。
3. 为 `RECTANGLE` + `cornerShape` 组合补充渲染验证（至少含 offset 正/负值）。
4. 若发现视觉回归，可通过关闭开关快速回退到原样式。

## Open Questions

- 开关命名是否采用 `showMiddleSegments` 还是更明确的 `showCropFrameMiddleSegments`（需在可读性与 API 简洁之间取舍）。
- 是否需要在 README 增加“offset 符号语义（正内负外）”说明，避免误用。
