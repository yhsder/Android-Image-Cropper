# Crop Frame Style Spec

## 1. Goal

- Create a document-scan style rectangular crop frame.
- Only change visual style.
- Do not change crop algorithm, coordinate mapping, or drag interaction rules.
- Decorative elements must always stay outside the thin border.
- Do not use a fallback strategy that moves decorations inside when space is tight.

## 2. Base Shape

- The crop frame body is a fully closed thin rectangular border.
- Decorative elements are drawn outside and tightly attached to the thin border.
- Decorative elements include:
  - One `L` corner mark on each of the four corners
  - One short center segment on each of the four edges
- No 3x3 guidelines inside the crop area.
- No circular drag handles.
- Keep the existing drag hit area and behavior unchanged.

## 3. Border Structure

- Main border:
  - Fully closed, continuous on all four sides
  - Thinner than decorative strokes
  - Default width: `1dp`
  - Configurable
- Outer decorations:
  - Drawn outside the main border
  - Tightly attached to the outer edge of the main border, with no extra gap
  - Default width: `3dp`
  - Configurable
  - Corner marks and center segments use the same stroke width

## 4. Corner Marks

- All four corners have `L` shaped corner marks.
- Each corner mark consists of one horizontal line and one vertical line.
- The two legs must have equal length.
- Corner length is configurable.
- Recommended default: around `20dp`
- Corner marks are placed outside the main border and tightly attached to it.
- The corner joint stays a sharp right angle.
- Only the line ends are rounded.
- No rounded corner transition at the bend.
- Recommended drawing style:
  - `strokeCap = ROUND`
  - `strokeJoin = MITER` or any equivalent sharp-corner join
- All four corners must remain strictly symmetrical.

## 5. Edge Center Segments

- Four center segments are required:
  - Top center
  - Bottom center
  - Left center
  - Right center
- Each segment is drawn outside the corresponding border edge and tightly attached to it.
- Segment ends are rounded.
- Center segments are independent short strokes and do not share endpoints with corner marks.
- Center alignment must be exact:
  - Top and bottom segments align to the horizontal center
  - Left and right segments align to the vertical center

## 6. Center Segment Length Rule

- Center segment length must shrink as the crop frame gets smaller.
- Do not use a fixed-only length.
- Use proportional scaling with min/max clamping.
- Recommended rule:
  - Horizontal segment length = `cropWidth * ratio`
  - Vertical segment length = `cropHeight * ratio`
  - Final length = `clamp(minLength, scaledLength, maxLength)`
- Recommended defaults:
  - `ratio = 0.18f`
  - `minLength = 10dp`
  - `maxLength = 28dp`
- When the crop frame is small, center segments are allowed to visually approach or touch corner marks.
- Decorative elements may visually touch each other on small sizes, but must not exceed the controlled drawing area.

## 7. Colors

- Provide two configurable color groups:
  - `frameBorderColor` for the thin border
  - `frameAccentColor` for corner marks and center segments
- Default values should be the same color to match the target look.
- The configuration must still allow separate colors.
- Visual recommendation:
  - Border can be slightly lighter or weaker
  - Accent elements can be slightly brighter

## 8. Stroke and Visual Details

- Main border uses standard rectangle stroke rendering.
- Corner marks and center segments:
  - Rounded line ends
  - No circular node handles
  - No rounded rectangle crop frame
- Decorative elements must be visually stronger than the main border.
- Visual emphasis should be on corner marks and center segments, not on the thin border.

## 9. Crop Shape Scope

- This style only targets the standard rectangular crop frame.
- No need to adapt this style for oval crop shapes.
- No need to define a dedicated style for `RECTANGLE_VERTICAL_ONLY` or `RECTANGLE_HORIZONTAL_ONLY` in the first version.

## 10. Edge Visibility Strategy

- Requirement: corner marks and center segments must always stay outside the thin border.
- Do not use an inside fallback when the crop frame is near the view edge.
- To keep outer decorations visible, reserve a unified safe display area.
- Recommended behavior:
  - Shrink the image display area inward by a configurable inset
  - Shrink the crop frame movement boundary inward by the same inset
  - Continue drawing decorations outside the thin border
- Recommended default safe inset: `16dp`
- The safe inset must be configurable.
- This changes the maximum selectable crop boundary, but does not change the crop algorithm itself.

## 11. Algorithm and Interaction Boundary

- Do not change:
  - Crop result calculation
  - Coordinate mapping to the source image
  - Existing drag hit testing
  - Zoom, rotation, and output crop logic
- Only change:
  - Overlay drawing style
  - Visual safe display area
  - Crop frame movement boundary
- Summary:
  - Do not change how cropping works
  - Only change how far the crop frame can expand visually

## 12. Suggested Configurable Parameters

- `frameBorderWidth`
- `frameBorderColor`
- `frameAccentWidth`
- `frameAccentColor`
- `frameCornerLength`
- `frameCenterLineRatio`
- `frameCenterLineMinLength`
- `frameCenterLineMaxLength`
- `frameSafeInset`

## 13. Suggested Default Values

- `frameBorderWidth = 1dp`
- `frameAccentWidth = 3dp`
- `frameCornerLength = 20dp`
- `frameCenterLineRatio = 0.18f`
- `frameCenterLineMinLength = 10dp`
- `frameCenterLineMaxLength = 28dp`
- `frameSafeInset = 16dp`
- `frameBorderColor = same default value as frameAccentColor`
- `frameAccentColor = bright light color close to white-blue`

## 14. Non-goals

- Do not add new drag handle interactions
- Do not show guidelines
- Do not add edge-based visual fallback logic
- Do not change the crop algorithm
- Do not change snap behavior or interaction model
