package com.gti.wbs;

import static java.lang.System.lineSeparator;

public class NodeStyle {

	private String horizontalAlignment;
	private String verticalAlignment;
	private int maximumLineWidth; //pixels
	private String lineColor;
	private String backgroundColor;
	private float lineThikness;
	private float shadowing;
	private int roundCorner;
	private int padding;
	private int margin;

	private String styleDefinition;

	public NodeStyle() {
		this.margin 			= -1;
		this.maximumLineWidth 	= -1;
		this.padding 			= -1;
		this.lineThikness 		= -1;
		this.shadowing 			= -1;
		this.roundCorner 		= -1;
	}

	public String getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public String getVerticalAlignment() {
		return verticalAlignment;
	}

	public int getMaximumLineWidth() {
		return maximumLineWidth;
	}

	public String getLineColor() {
		return lineColor;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

	public float getLineThikness() {
		return lineThikness;
	}

	public float getShadowing() {
		return shadowing;
	}

	public int getRoundCorner() {
		return roundCorner;
	}

	public int getPadding() {
		return padding;
	}

	public int getMargin() {
		return margin;
	}

	public String getStyleDefinition() {
		return styleDefinition;
	}

	public static class StyleBuilder {

		private static final String START_TAG = "<style>";
		private static final String END_TAG = "</style>";
		private static final String START_NODE = "node {";
		private static final String END_NODE = "}";

		private NodeStyle nodeStyle = new NodeStyle();
		private StringBuilder style = new StringBuilder();

		public StyleBuilder withHorizontalAlignment(String alignment) {
			nodeStyle.horizontalAlignment = alignment;
			return this;
		}

		public StyleBuilder withVerticalAlignment(String alignment) {
			nodeStyle.verticalAlignment = alignment;
			return this;
		}

		public StyleBuilder withMaximumLineWidth(int pixels) {
			nodeStyle.maximumLineWidth = pixels;
			return this;
		}

		public StyleBuilder withLineColor(String color) {
			nodeStyle.lineColor = color;
			return this;
		}

		public StyleBuilder withBackgroundColor(String color) {
			nodeStyle.backgroundColor = color;
			return this;
		}

		public StyleBuilder withLineThikness(float thickness) {
			nodeStyle.lineThikness = thickness;
			return this;
		}

		public StyleBuilder withLineShadowing(float shadowing) {
			nodeStyle.shadowing = shadowing;
			return this;
		}

		public StyleBuilder withCornerRounding(int round) {
			nodeStyle.roundCorner = round;
			return this;
		}

		public StyleBuilder withPadding(int padding) {
			nodeStyle.padding = padding;
			return this;
		}

		public StyleBuilder withMargin(int margin) {
			nodeStyle.margin = margin;
			return this;
		}

		public NodeStyle createStyle() {
			build();
			nodeStyle.styleDefinition = style.toString();
			return this.nodeStyle;
		}

		private void build() {
			style.append(START_TAG)
				.append(lineSeparator())
				.append(START_NODE)
				.append(lineSeparator());

			appendNodeProperties();

			style.append(END_NODE)
				.append(lineSeparator())
				.append(END_TAG);
		}

		private void appendNodeProperties() {
			if (!(nodeStyle.horizontalAlignment == null || nodeStyle.horizontalAlignment.isEmpty())) {
				style.append("HorizontalAlignment ").append(nodeStyle.horizontalAlignment).append(lineSeparator());
			}
			if (!(nodeStyle.verticalAlignment == null || nodeStyle.verticalAlignment.isEmpty())) {
				style.append("VerticalAlignment ").append(nodeStyle.verticalAlignment).append(lineSeparator());
			}
			if (!(nodeStyle.backgroundColor == null || nodeStyle.backgroundColor.isEmpty())) {
				style.append("BackgroundColor ").append(nodeStyle.backgroundColor).append(lineSeparator());
			}
			if (!(nodeStyle.lineColor == null || nodeStyle.lineColor.isEmpty())) {
				style.append("LineColor ").append(nodeStyle.lineColor).append(lineSeparator());
			}
			if (nodeStyle.maximumLineWidth > -1) {
				style.append("MaximumWidth ").append(nodeStyle.maximumLineWidth).append(lineSeparator());
			}
			if (nodeStyle.lineThikness > -1) {
				style.append("LineThickness ").append(nodeStyle.lineThikness).append(lineSeparator());
			}
			if (nodeStyle.roundCorner > -1) {
				style.append("RoundCorner ").append(nodeStyle.roundCorner).append(lineSeparator());
			}
			if (nodeStyle.shadowing > -1) {
				style.append("Shadowing ").append(nodeStyle.shadowing).append(lineSeparator());
			}
			if (nodeStyle.padding > -1) {
				style.append("Padding ").append(nodeStyle.padding).append(lineSeparator());
			}
			if (nodeStyle.margin > -1) {
				style.append("Margin ").append(nodeStyle.margin).append(lineSeparator());
			}
		}
	}
}
