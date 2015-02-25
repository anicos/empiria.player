package eu.ydp.empiria.player.client.module.slideshow.structure;

import com.google.gwt.core.client.GWT;
import com.peterfranza.gwt.jaxb.client.parser.JAXBParser;
import com.peterfranza.gwt.jaxb.client.parser.utils.XMLContent;
import eu.ydp.empiria.player.client.AbstractEmpiriaPlayerGWTTestCase;
import java.util.List;

public class SlideshowJAXBParserTest extends AbstractEmpiriaPlayerGWTTestCase {

	private final SourceBean firstSourceBean;
	private final SoundBean firstSoundBean;
	private final SlideBean firstSlideExpected;
	private final SourceBean secondSourceBean;
	private final SoundBean secondSoundBean;
	private final SlideBean secondSlideExpected;

	public SlideshowJAXBParserTest() {
		firstSourceBean = new SourceBean();
		firstSourceBean.setSrc("source1");

		firstSoundBean = new SoundBean();
		firstSoundBean.setSrc("test1.mp3");

		String firstSlideTitleString = "<slideTitle>slide title1</slideTitle>";
		XmlContentImpl firstSlideTitleContent = new XmlContentImpl();
		firstSlideTitleContent.setStringElement(firstSlideTitleString);

		SlideTitleBean firstSlideTitle = new SlideTitleBean();
		firstSlideTitle.setTitleValue(firstSlideTitleContent);

		String firstSlideNarrationString = "<narration>narration1</narration>";
		XmlContentImpl firstSlideNarrationContent = new XmlContentImpl();
		firstSlideNarrationContent.setStringElement(firstSlideNarrationString);

		SlideNarrationBean firstSlideNarration = new SlideNarrationBean();
		firstSlideNarration.setNarrationValue(firstSlideNarrationContent);

		firstSlideExpected = new SlideBean();
		firstSlideExpected.setSlideTitle(firstSlideTitle);
		firstSlideExpected.setNarration(firstSlideNarration);
		firstSlideExpected.setSource(firstSourceBean);
		firstSlideExpected.setSound(firstSoundBean);

		secondSourceBean = new SourceBean();
		secondSourceBean.setSrc("source2");

		secondSoundBean = new SoundBean();
		secondSoundBean.setSrc("test2.mp3");

		String secondSlideTitleString = "<slideTitle>slide title2</slideTitle>";
		XmlContentImpl secondSlideTitleContent = new XmlContentImpl();
		secondSlideTitleContent.setStringElement(secondSlideTitleString);

		SlideTitleBean secondSlideTitle = new SlideTitleBean();
		secondSlideTitle.setTitleValue(secondSlideTitleContent);

		String secondSlideNarrationString = "<narration>narration2</narration>";
		XmlContentImpl secondSlideNarrationContent = new XmlContentImpl();
		secondSlideNarrationContent.setStringElement(secondSlideNarrationString);

		SlideNarrationBean secondSlideNarration = new SlideNarrationBean();
		secondSlideNarration.setNarrationValue(secondSlideNarrationContent);

		secondSlideExpected = new SlideBean();
		secondSlideExpected.setSlideTitle(secondSlideTitle);
		secondSlideExpected.setNarration(secondSlideNarration);
		secondSlideExpected.setSource(secondSourceBean);
		secondSlideExpected.setSound(secondSoundBean);
	}

	public void testFullSlideshow_withBoldTitle() {
		// given
		String firstSlideTitleString = "<slideTitle>slide <bold>title1</bold></slideTitle>";
		XmlContentImpl firstSlideTitleContent = new XmlContentImpl();
		firstSlideTitleContent.setStringElement(firstSlideTitleString);
		firstSlideExpected.getSlideTitle().setTitleValue(firstSlideTitleContent);

		XmlContentImpl titleExpected = new XmlContentImpl();
		titleExpected.setStringElement("<title>title</title>");

		// when
		SlideshowPlayerBean bean = parse(SlideshowJAXBParserMock.FULL_SLIDESHOW);

		// then
		SlideshowBean slideshow = bean.getSlideshowBean();

		XMLContent titleResult = slideshow.getTitle().getTitleValue();
		assertEqualsXmlContent(titleExpected, titleResult);

		List<SlideBean> slides = slideshow.getSlideBeans();
		assertEquals(2, slides.size());

		SlideBean firstSlide = slides.get(0);
		assertSlidesEquals(firstSlideExpected, firstSlide);

		SlideBean secondSlide = slides.get(1);
		assertSlidesEquals(secondSlideExpected, secondSlide);

		assertTemplateIsNotPresent(bean);
	}

	public void testSlideshowWihoutSound() {
		// given
		XmlContentImpl titleExpected = new XmlContentImpl();
		titleExpected.setStringElement("<title>title</title>");

		firstSlideExpected.setSound(null);

		// when
		SlideshowPlayerBean bean = parse(SlideshowJAXBParserMock.SLIDESHOW_WITHOUT_SOUND);

		// then
		SlideshowBean slideshow = bean.getSlideshowBean();

		XMLContent titleResult = slideshow.getTitle().getTitleValue();
		assertEqualsXmlContent(titleExpected, titleResult);

		List<SlideBean> slides = slideshow.getSlideBeans();
		assertEquals(1, slides.size());

		SlideBean firstSlide = slides.get(0);
		assertSlidesEquals(firstSlideExpected, firstSlide);

		assertTemplateIsNotPresent(bean);
		assertSoundIsNotPresent(firstSlide);
	}

	public void testSlideshowWihoutNarration() {
		// given
		XmlContentImpl titleExpected = new XmlContentImpl();
		titleExpected.setStringElement("<title>title</title>");

		firstSlideExpected.setNarration(null);
		// when
		SlideshowPlayerBean bean = parse(SlideshowJAXBParserMock.SLIDESHOW_WITHOUT_NARRATION);

		// then
		SlideshowBean slideshow = bean.getSlideshowBean();

		XMLContent titleResult = slideshow.getTitle().getTitleValue();
		assertEqualsXmlContent(titleExpected, titleResult);

		List<SlideBean> slides = slideshow.getSlideBeans();
		assertEquals(1, slides.size());

		SlideBean firstSlide = slides.get(0);
		assertSlidesEquals(firstSlideExpected, firstSlide);

		assertTemplateIsNotPresent(bean);
	}

	public void testSlideshowWithoutTitle() {
		// given
		XmlContentImpl titleExpected = new XmlContentImpl();
		titleExpected.setStringElement("<title></title>");

		// when
		SlideshowPlayerBean bean = parse(SlideshowJAXBParserMock.FULL_SLIDESHOW_WITHOUT_TITLE);

		// then
		SlideshowBean slideshow = bean.getSlideshowBean();
		XMLContent titleResult = slideshow.getTitle().getTitleValue();
		assertEqualsXmlContent(titleExpected, titleResult);

		List<SlideBean> slides = slideshow.getSlideBeans();
		assertEquals(2, slides.size());

		SlideBean firstSlide = slides.get(0);
		assertSlidesEquals(firstSlideExpected, firstSlide);

		SlideBean secondSlide = slides.get(1);
		assertSlidesEquals(secondSlideExpected, secondSlide);

		assertTemplateIsNotPresent(bean);
	}

	public void testSlideshowWithTemplate() {
		SlideshowPlayerBean slideshowPlayer = parse(SlideshowJAXBParserMock.FULL_SLIDESHOW_WITH_TEMPLATE);
		SlideshowTemplate template = slideshowPlayer.getTemplate();

		assertTrue(slideshowPlayer.hasTemplate());
		assertTrue(template.hasSlideshowPager());
	}

	private void assertSlidesEquals(SlideBean slideExpected, SlideBean slideActual) {
		assertEqualsSlideTitle(slideExpected.getSlideTitle(), slideActual.getSlideTitle(), slideExpected.hasSlideTitle());
		assertEqualsSlideNarration(slideExpected.getNarration(), slideActual.getNarration(), slideExpected.hasNarration());
		assertSourceEqual(slideExpected.getSource(), slideActual.getSource());
		assertSoundEqual(slideExpected.getSound(), slideActual.getSound(), slideExpected.hasSound());
	}

	private void assertEqualsSlideTitle(SlideTitleBean expectedNarration, SlideTitleBean actualNarration, boolean hasNarration) {
		if (hasNarration) {
			assertEqualsXmlContent(expectedNarration.getTitleValue(), actualNarration.getTitleValue());
		} else {
			assertEquals(expectedNarration, actualNarration);
		}
	}

	private void assertEqualsSlideNarration(SlideNarrationBean expectedNarration, SlideNarrationBean actualNarration, boolean hasNarration) {
		if (hasNarration) {
			assertEqualsXmlContent(expectedNarration.getNarrationValue(), actualNarration.getNarrationValue());
		} else {
			assertEquals(expectedNarration, actualNarration);
		}
	}

	private void assertEqualsXmlContent(XMLContent expectedSlideTitle, XMLContent resultSlideTitle) {
		assertEquals(expectedSlideTitle.toString(), resultSlideTitle.getValue().toString());
	}

	private void assertSourceEqual(SourceBean sourceExpected, SourceBean sourceActual) {
		assertEquals(sourceExpected.getSrc(), sourceActual.getSrc());
	}

	private void assertSoundEqual(SoundBean soundExpected, SoundBean soundActual, boolean hasSound) {
		if (hasSound) {
			assertEquals(soundExpected.getSrc(), soundActual.getSrc());
		} else {
			assertEquals(soundExpected, soundActual);
		}
	}

	private SlideshowPlayerBean parse(String xml) {
		SlideshowJAXBParser jaxbParserFactory = GWT.create(SlideshowJAXBParser.class);
		JAXBParser<SlideshowPlayerBean> jaxbParser = jaxbParserFactory.create();
		SlideshowPlayerBean slideshowPlayer = jaxbParser.parse(xml);
		return slideshowPlayer;
	}

	private void assertTemplateIsNotPresent(SlideshowPlayerBean slideshowPlayer) {
		assertFalse(slideshowPlayer.hasTemplate());
	}

	private void assertSoundIsNotPresent(SlideBean slideActual) {
		assertFalse(slideActual.hasSound());
	}
}
