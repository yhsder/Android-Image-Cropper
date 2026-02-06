## Purpose

Define crop overlay frame rendering behavior for rectangular and related styles, including middle-segment rendering and negative-offset visibility protection.

## Requirements

### Requirement: Rectangular crop frame SHALL support middle segments
When `cropShape` is `RECTANGLE`, the system SHALL be able to render four middle segments at the center of each edge (top, bottom, left, right) as part of the crop frame style.

#### Scenario: Render middle segments on all four edges
- **WHEN** `cropShape = RECTANGLE` and middle-segment style is enabled
- **THEN** the crop overlay renders exactly one middle segment at each edge center (top, bottom, left, right)

### Requirement: Middle segments MUST reuse existing corner style parameters
The middle segments MUST reuse existing corner style parameters: `borderCornerThickness`, `borderCornerColor`, `borderCornerLength`, and `borderCornerOffset`.

#### Scenario: Segment visual style follows corner parameters
- **WHEN** the caller changes any of `borderCornerThickness`, `borderCornerColor`, or `borderCornerLength`
- **THEN** both corner L lines and middle segments reflect the updated thickness, color, and length consistently

#### Scenario: Segment offset follows existing offset sign semantics
- **WHEN** `borderCornerOffset` is positive, zero, or negative
- **THEN** middle segments use the same direction semantics as corners (positive inward, negative outward, zero aligned on border center)

### Requirement: Middle segments SHALL be disabled for non-target shape combinations
The system SHALL disable middle-segment rendering unless `cropShape = RECTANGLE` and `cornerShape != OVAL`.

#### Scenario: Disabled for oval corner shape
- **WHEN** `cropShape = RECTANGLE` and `cornerShape = OVAL`
- **THEN** middle segments are not rendered

#### Scenario: Disabled for non-rectangle crop shapes
- **WHEN** `cropShape` is `OVAL`, `RECTANGLE_VERTICAL_ONLY`, or `RECTANGLE_HORIZONTAL_ONLY`
- **THEN** middle segments are not rendered

### Requirement: Negative corner offset SHALL automatically preserve outward corner visibility
When `borderCornerOffset < 0` and rectangle style conditions are met, the system SHALL automatically apply a visual gutter strategy so outward L corners remain visible instead of being clipped by the view edge.

#### Scenario: Auto gutter is applied on negative offset
- **WHEN** `borderCornerOffset < 0`, `cropShape = RECTANGLE`, and `cornerShape != OVAL`
- **THEN** the rendering pipeline automatically reserves display space to keep outward corners visible

#### Scenario: Auto gutter is not applied on non-negative offset
- **WHEN** `borderCornerOffset >= 0`
- **THEN** no extra visual gutter is applied by this rule

#### Scenario: Auto gutter is not applied for non-target shape combinations
- **WHEN** `cropShape` is not `RECTANGLE` or `cornerShape = OVAL`
- **THEN** the negative-offset auto gutter rule does not apply

### Requirement: Auto gutter MUST NOT require explicit user configuration
The negative-offset visibility protection MUST be an internal automatic behavior and MUST NOT require callers to set an additional switch.

#### Scenario: Automatic behavior without extra configuration
- **WHEN** callers only configure `borderCornerOffset < 0` under valid rectangle conditions
- **THEN** outward-corner visibility protection is active without additional API or XML flags

### Requirement: Middle-segment style MUST remain opt-in for backward compatibility
Middle-segment rendering capability MUST remain disabled by default to preserve legacy visuals unless explicitly enabled.

#### Scenario: Default behavior remains unchanged
- **WHEN** existing integrations do not enable middle-segment style
- **THEN** crop frame rendering remains equivalent to pre-middle-segment behavior
