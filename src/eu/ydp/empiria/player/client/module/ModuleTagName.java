package eu.ydp.empiria.player.client.module;

public enum ModuleTagName {
	DIV("div"), GROUP("group"), SPAN("span"), TEXT_INTERACTION("textInteraction"), IMG("img"),
	CHOICE_INTERACTION("choiceInteraction"),
	SELECTION_INTERACTION("selectionInteraction"), IDENTYFICATION_INTERACTION("identificationInteraction"),
	TEXT_ENTRY_INTERACTION("textEntryInteraction"),	INLINE_CHOICE_INTERACTION("inlineChoiceInteraction"),
	SIMPLE_TEXT("simpleText"), AUDIO_PLAYER("audioPlayer"), MATH_TEXT("mathText"), MATH_INTERACTION("mathInteraction"),
	OBJECT("object"), SLIDESHOW_PLAYER("slideshowPlayer"), PROMPT("prompt"), TABLE("table"),
	PAGE_IN_PAGE("pageInPage"), SHAPE("shape"), INFO("info"), REPORT("report"), LINK("link"),
	NEXT_ITEM_NAVIGATION("nextItemNavigation"), PREV_ITEM_NAVIGATION("prevItemNavigation"), PAGES_SWITCH_BOX("pagesSwitchBox"),
	MARK_ALL_BUTTON("markAllButton"), SHOW_ANSWERS_BUTTON("showAnswersButton"), RESET_BUTTON("resetButton");

	String name = null;
	private ModuleTagName(String name){
		this.name = name;
	}

	public String tagName(){
		return name;
	}

	public ModuleTagName getTag(String name){
		for(ModuleTagName tag : ModuleTagName.values()){
			if(tag.name.equals(name)){
				return tag;
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return name;
	}


}
