package eu.ydp.empiria.player.client.module.textentry;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import eu.ydp.empiria.player.client.gin.PlayerGinjector;
import eu.ydp.empiria.player.client.components.ExListBox;
import eu.ydp.empiria.player.client.module.gap.GapBase.Presenter;
import eu.ydp.empiria.player.client.module.gap.GapBase.PresenterHandler;
import eu.ydp.empiria.player.client.resources.StyleNameConstants;

public class TextEntryModulePresenter implements Presenter {
	
	@UiTemplate("TextEntryModule.ui.xml")
	interface TextEntryModuleUiBinder extends UiBinder<Widget, TextEntryModulePresenter>{};
	
	private final TextEntryModuleUiBinder uiBinder = GWT.create(TextEntryModuleUiBinder.class);
	
	@UiField
	protected TextBox textBox;
	
	@UiField
	protected Panel moduleWidget;

	private PresenterHandler changeHandler;
	
	private StyleNameConstants styleNames = PlayerGinjector.INSTANCE.getStyleNameConstants();
	
	public TextEntryModulePresenter(){
		uiBinder.createAndBindUi(this);
	}
	
	@UiHandler("textBox")
	protected void handleChange(ChangeEvent event){
		if(changeHandler != null){
			changeHandler.onChange(event);
		}
	}
	
	protected void handleBlur(BlurEvent event){
		if(changeHandler != null){
			changeHandler.onBlur(event);
		}
	}

	@Override
	public void setWidth(double value, Unit unit) {
		if (textBox != null) {
			textBox.setWidth(value + unit.getType());
		}
	}
	
	@Override
	public int getOffsetWidth() {
		return textBox.getOffsetWidth();
	}

	@Override
	public void setHeight(double value, Unit unit) {
		textBox.setHeight(value + unit.getType());
	}
	
	@Override
	public int getOffsetHeight() {
		return textBox.getOffsetHeight();
	}
	
	@Override
	public void setMaxLength(int length) {
		if (textBox != null) {
			textBox.setMaxLength(length);
		}
	}

	@Override
	public void setFontSize(double value, Unit unit) {
		if (textBox != null) {
			textBox.getElement().getStyle().setFontSize(value, unit);
		}
	}
	
	@Override
	public int getFontSize() {
		return Integer.parseInt(textBox.getElement().getStyle().getFontSize().replace("px", ""));
	}

	@Override
	public void setText(String text) {
		textBox.setText(text);
	}

	@Override
	public String getText() {
		return textBox.getText();
	}

	@Override
	public HasWidgets getContainer() {
		return moduleWidget;
	}

	@Override
	public void installViewInContainer(HasWidgets container) {
		container.add(moduleWidget);
	}

	@Override
	public void setViewEnabled(boolean enabled) {
		textBox.setEnabled(enabled);
	}

	@Override
	public void setMarkMode(String mode) {
		String markStyleName;
		
		if(Presenter.CORRECT.equals(mode)) {
			markStyleName = styleNames.QP_TEXT_TEXTENTRY_CORRECT();
		} else if(Presenter.WRONG.equals(mode)){
			markStyleName = styleNames.QP_TEXT_TEXTENTRY_WRONG();
		}else{
			markStyleName = styleNames.QP_TEXT_TEXTENTRY_NONE();
		}
		
		moduleWidget.setStyleName(markStyleName);
	}

	@Override
	public void removeMarking() {
		moduleWidget.setStyleName(styleNames.QP_TEXT_TEXTENTRY());
	}

	@Override
	public void addPresenterHandler(PresenterHandler handler) {
		changeHandler = handler;
	}

	@Override
	public void removeFocusFromTextField() {
		textBox.getElement().blur();		
	}

	@Override
	public ExListBox getListBox() {
		return null;
	}
}