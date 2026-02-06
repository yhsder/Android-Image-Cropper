## 1. API 与配置链路

- [x] 1.1 在 `CropImageOptions` 增加中点短线开关字段（默认关闭），并补充可序列化传递。
- [x] 1.2 在 `attrs.xml` 增加对应 XML attr，并在 `CropImageView` 的 attrs 解析中接入该开关。
- [x] 1.3 在 `CropOverlayView.setInitialAttributeValues` 接收并保存该开关，确保 options→view 传递完整。

## 2. 裁剪框绘制实现

- [x] 2.1 在 `CropOverlayView` 新增中点短线绘制函数，并在绘制流程中接入（边框与角标之间）。
- [x] 2.2 按 `RECTANGLE` 且 `cornerShape != OVAL` 的条件控制中点短线绘制。
- [x] 2.3 复用 `borderCornerThickness`、`borderCornerColor`、`borderCornerLength`、`borderCornerOffset` 计算中点短线样式与位置。
- [x] 2.4 校验 offset 方向与现有语义一致（正值向内、负值向外、0 在线中心）。

## 3. 测试与样例验证

- [x] 3.1 增加/更新单元或快照测试，覆盖开关关闭时与历史行为一致。
- [x] 3.2 增加/更新测试，覆盖开关开启时四边中点短线渲染（含 offset 正/负值）。
- [x] 3.3 增加/更新测试，覆盖禁用条件：`cornerShape = OVAL` 与非 `RECTANGLE` 的 `cropShape`。
- [x] 3.4 在 sample 中增加可视化开关或固定示例，手工验证目标样式。

## 4. 文档与收尾

- [x] 4.1 更新 README 或开发说明，明确中点短线能力与 `borderCornerOffset` 符号语义。
- [x] 4.2 运行格式检查与测试命令，确认改动可合入。
