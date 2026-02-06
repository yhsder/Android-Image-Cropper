## ADDED Requirements

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

### Requirement: New style MUST be opt-in to preserve backward compatibility
The middle-segment style MUST be controlled by a new configuration switch, and its default value MUST keep existing visuals unchanged.

#### Scenario: Default behavior remains unchanged
- **WHEN** existing integrations do not set the new middle-segment switch
- **THEN** crop frame rendering remains equivalent to pre-change behavior

#### Scenario: Explicit enablement activates new style
- **WHEN** the caller explicitly enables the middle-segment switch and uses `cropShape = RECTANGLE` with non-oval corner shape
- **THEN** middle segments are rendered according to existing corner style parameters
