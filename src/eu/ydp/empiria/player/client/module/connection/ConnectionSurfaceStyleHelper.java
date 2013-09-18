package eu.ydp.empiria.player.client.module.connection;

import java.util.List;

import com.google.common.collect.Lists;

import eu.ydp.empiria.player.client.module.components.multiplepair.MultiplePairModuleConnectType;

public class ConnectionSurfaceStyleHelper {

	private final static String CONNECTION_LINE_PREFIX = "qp-connection-line";
	private final static String CONNECTION_LINE_CORRECT_PREFIX = "qp-connection-line-correct";
	private final static String CONNECTION_LINE_WRONG_PREFIX = "qp-connection-line-wrong";

	public List<String> getStylesForSurface(MultiplePairModuleConnectType type, int leftIndex, int rightIndex) {
		if (leftIndex < 0 || rightIndex < 0) {
			throw new IllegalArgumentException("Index of item could not be negative");
		}

		List<String> styles = Lists.newArrayList();
		styles.add(getStyleForNormalSurface(leftIndex, rightIndex));

		String additionalStyle = null;
		if (type == MultiplePairModuleConnectType.CORRECT) {
			additionalStyle = getStyleForCorrectSurface(leftIndex, rightIndex);
		} else if (type == MultiplePairModuleConnectType.WRONG) {
			additionalStyle = getStyleForWrongSurface(leftIndex, rightIndex);
		}

		if (additionalStyle != null) {
			styles.add(additionalStyle);
		}
		return styles;
	}

	private String getStyleForNormalSurface(int leftIndex, int rightIndex) {
		return CONNECTION_LINE_PREFIX + getIndexesSufix(leftIndex, rightIndex);
	}

	private String getStyleForCorrectSurface(int leftIndex, int rightIndex) {
		return CONNECTION_LINE_CORRECT_PREFIX + getIndexesSufix(leftIndex, rightIndex);
	}

	private String getStyleForWrongSurface(int leftIndex, int rightIndex) {
		return CONNECTION_LINE_WRONG_PREFIX + getIndexesSufix(leftIndex, rightIndex);
	}

	private String getIndexesSufix(int leftIndex, int rightIndex) {
		return "-" + leftIndex + "-" + rightIndex;
	}
}
