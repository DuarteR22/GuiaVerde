---
name: Guia Verde
colors:
  surface: '#f8f9fa'
  surface-dim: '#d9dadb'
  surface-bright: '#f8f9fa'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f3f4f5'
  surface-container: '#edeeef'
  surface-container-high: '#e7e8e9'
  surface-container-highest: '#e1e3e4'
  on-surface: '#191c1d'
  on-surface-variant: '#3f4941'
  inverse-surface: '#2e3132'
  inverse-on-surface: '#f0f1f2'
  outline: '#6f7a70'
  outline-variant: '#bec9bf'
  surface-tint: '#006d40'
  primary: '#005b34'
  on-primary: '#ffffff'
  primary-container: '#137547'
  on-primary-container: '#9ef8be'
  inverse-primary: '#80d9a1'
  secondary: '#2c694e'
  on-secondary: '#ffffff'
  secondary-container: '#aeeecb'
  on-secondary-container: '#316e52'
  tertiary: '#005a3b'
  on-tertiary: '#ffffff'
  tertiary-container: '#00754e'
  on-tertiary-container: '#94f9c5'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#9cf6bc'
  primary-fixed-dim: '#80d9a1'
  on-primary-fixed: '#002110'
  on-primary-fixed-variant: '#00522f'
  secondary-fixed: '#b1f0ce'
  secondary-fixed-dim: '#95d4b3'
  on-secondary-fixed: '#002114'
  on-secondary-fixed-variant: '#0e5138'
  tertiary-fixed: '#92f7c3'
  tertiary-fixed-dim: '#75daa8'
  on-tertiary-fixed: '#002113'
  on-tertiary-fixed-variant: '#005235'
  background: '#f8f9fa'
  on-background: '#191c1d'
  surface-variant: '#e1e3e4'
typography:
  display-lg:
    fontFamily: Chivo
    fontSize: 44px
    fontWeight: '700'
    lineHeight: 52px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Chivo
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.01em
  headline-lg-mobile:
    fontFamily: Chivo
    fontSize: 26px
    fontWeight: '700'
    lineHeight: 34px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Chivo
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
  title-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 18px
    fontWeight: '700'
    lineHeight: 24px
  title-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 22px
  body-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  label-lg:
    fontFamily: Plus Jakarta Sans
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Plus Jakarta Sans
    fontSize: 12px
    fontWeight: '600'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Plus Jakarta Sans
    fontSize: 11px
    fontWeight: '500'
    lineHeight: 14px
    letterSpacing: 0.03em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-sm: 0.75rem
  margin: 1rem
  margin-lg: 1.5rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style
The design system channels the clarity, authority, and rapid legibility of Portuguese motorway signage (Autoestradas e ex-SCUTs). Engineered for drivers, road-trippers, and daily commuters, the aesthetic merges Material Design 3 systematic rigor with the physical language of European transit networks: deep evergreen expanses, crisp reflective white pictograms, and tactile pill containers.

The visual style is modern utility minimalism infused with roadside infrastructural cues:
- **Instant Glanceability:** Generous vertical rhythm, robust touch ergonomics, and assertive typographic hierarchies that prevent cognitive fatigue in bright daylight or car cockpit setups.
- **Physical Roadway Metaphor:** Surfaces behave like high-grade transit signs and highway gantry panels—clean planes, distinct tonal hierarchy, and purposeful borders mimicking retroreflective signage trim.
- **Tone:** Competent, transparent, and direct. It replaces the anxiety of automated electronic gantries (pórticos ex-SCUT) and complex toll plaza calculations with calm certainty.

## Colors
The palette directly mirrors Portuguese motorway color conventions: forest green primary routes, emerald active zones, and mint confirmation states.

- **Primary (`#137547`):** Direct highway emerald green. Used for key call-to-actions, top app bars, primary route polyline badges, and primary calculation triggers.
- **Secondary (`#2D6A4F`):** Deep roadside forest green. Drives secondary interactions, structural headers, inactive tab states, and toll authority grouping containers.
- **Tertiary (`#52B788`):** Signal mint green. Used sparingly for positive confirmation states, electronic toll transponder indicators (Via Verde active status), and discount tags.
- **Neutral Surface Baseline (`#F8F9FA`):** Low-glare pavement off-white to eliminate driver eye-strain under harsh sunlight while maintaining pure `#FFFFFF` for elevated foreground cards.
- **On-Surface / Text (`#1B262C` & `#212529`):** Deep slate-tinted obsidian ensuring compliance with strict WCAG AAA contrast requirements against white and mint containers.

## Typography
Typographic rhythm pairs the directional, utilitarian velocity of **Chivo** for destinations, motorways (e.g., A1, A22, A28), and monetary sums, with the balanced readability of **Plus Jakarta Sans** for vehicle parameters, distance markers, and instructional UI.

- **Numerics & Toll Costs:** All currency amounts (€) and gantry distances leverage Chivo with tabular figures (`tnum`) enabled to prevent layout jumps during dynamic route updates.
- **Scale Hierarchy:** Mobile displays collapse larger display headers into `headline-lg-mobile` (26px) to maximize above-the-fold map viewports and route summary real estate.
- **Wayfinding Labels:** Section trackers and vehicle categories use high-weight uppercase or title-case `label-lg` with slight positive tracking (+0.01em) to simulate Portuguese motorway signage placards.

## Layout & Spacing
The layout follows a fluid-column architecture optimized for mobile handheld use and vehicular dashboard mounts.

- **Grid & Margins:** Standard mobile canvas relies on a 4-column fluid grid with `margin` set to `1rem` (16px) and `gutter` at `1rem` (16px). For compact landscape mode (dashboard phone docks), margins scale to `margin-lg` (24px).
- **Driver Touch Targets:** All interactive control zones conform strictly to a minimum bounding box of 48×48px. Form controls, vehicle class chips, and search anchors default to 52px or 56px height to prevent mis-taps on irregular pavement.
- **Rhythm Rules:**
  - Component-internal padding (inputs, trip cards, chips) uses `space-sm` (8px) for compact elements and `space-md` (16px) for major card interiors.
  - Section separation (e.g., origin/destination input group to vehicle selector group) strictly maintains `space-lg` (24px) to preserve semantic hierarchy.

## Elevation & Depth
Depth adheres to Material 3 elevation principles, tailored for daytime contrast and quick parsing:

- **Surface Tiers:**
  - **Level 0 (Canvas):** Tone `#F8F9FA` — background canvas for map controls and inactive screen areas.
  - **Level 1 (Cards & Inputs):** Solid `#FFFFFF` surface with a subtle slate-tinted ambient shadow: `0px 1px 3px rgba(27, 38, 44, 0.08), 0px 2px 6px rgba(27, 38, 44, 0.04)`.
  - **Level 2 (Active Trip Sheets & Modal Bottom Sheets):** `#FFFFFF` with `0px 4px 12px rgba(19, 117, 71, 0.08), 0px 2px 8px rgba(27, 38, 44, 0.06)`. Note the subtle emerald ambient tint.
  - **Level 3 (Floating Action Button & Sticky Toll Tally):** `#137547` filled surface with elevated projection: `0px 6px 16px rgba(19, 117, 71, 0.28)`.
- **Highway Signage Borders:** Instead of harsh borders, structural panels (like toll breakdowns per concessionaire) utilize a soft 1px surface stroke in `#E5E9EB` to provide structure under high ambient sun exposure.

## Shapes
The design uses a rounded geometry (Level 2: 0.5rem / 8px base radius) paired with dedicated pill geometry (9999px) for status badges and selection chips.

- **Containers & Cards:** 0.5rem (8px) base radius for structural stability; large route cards and bottom sheets use `rounded-xl` (1.5rem / 24px) at top corners for a modern, approachable card sheet.
- **Vehicle Class Chips & Gantry Badges:** Pill-shaped (`rounded-full` / 9999px) to distinctly isolate vehicle options (Classe 1, Classe 2, Classe 3, Classe 4, Motas) and mimic regulatory road signs.
- **Inputs & Text Fields:** 0.75rem (12px) continuous curvature for modern tactile comfort.

## Components

### 1. Buttons
- **Primary Action (e.g., "Calcular Portagens"):** Height 54px. Solid `#137547` background, white bold Chivo text, 12px rounded corners. Pressed state shifts to `#0F5132`.
- **Secondary (e.g., "Inverter Trajeto"):** Height 48px. Border 1.5px solid `#2D6A4F`, transparent background, text `#2D6A4F`.
- **Icon Buttons:** Minimum 48×48px tap container, centered 24px iconography with `#F0FDF4` hover/focus surface ring.

### 2. Chips (Vehicle Classes & Pass Systems)
- **Vehicle Selector:** Horizontal scroll container with single-select pill chips (e.g., "Classe 1", "Classe 2", "Mota", "Via Verde").
- **Selected State:** Solid `#2D6A4F` fill, pure `#FFFFFF` label, elevation Level 1.
- **Unselected State:** Surface `#FFFFFF`, border 1.5px `#E5E9EB`, label `#1B262C`.
- **Height & Padding:** 44px height, horizontal padding 18px.

### 3. Input Fields (Location & Route Setup)
- **Origin / Destination Group:** Unified 12px rounded white card container holding two stacked 52px fields connected by a subtle vertical transit line.
- **Left Adornments:** Distinct icons: Green outlined circle for Origin, Green solid pin for Destination.
- **Right Adornments:** Dedicated cross-hair button ("Localização Atual") and clear input tap target (48px hit area).
- **Active Focus:** 2px stroke in `#137547` with faint green focus glow (`rgba(19, 117, 71, 0.12)`).

### 4. Route Breakdown & Trip Summary Cards
- **Trip Summary Floating Card:** Elevated Level 2 card pinned to the bottom viewport showing total trip cost in display typography (e.g., `14,85 €`), distance (`248 km`), and duration.
- **Toll Gantry Sub-Items (Ex-SCUT Breakdown):** Segmented card listing individual overhead gantries (Pórticos) with gantry ID, concessionaire badge (e.g., "Ascendi", "Brisa"), and individual fee (`0,65 €`).
- **Badge Accents:** Pure pill badges with green fill (`#E8F5E9`) and dark green text (`#0F5132`) signifying physical toll plaza vs. electronic-only ex-SCUT gantries.

### 5. Checkboxes, Toggles & Switches
- **Via Verde Identifier Switch:** Standard MD3 switch with emerald `#137547` active track and crisp white thumb.
- **Avoid Tolls Option:** Clean MD3 checkbox with 24px touch bounds, `#137547` checked state.