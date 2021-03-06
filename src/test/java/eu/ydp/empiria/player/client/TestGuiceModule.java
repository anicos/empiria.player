/*
 * Copyright 2017 Young Digital Planet S.A.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.ydp.empiria.player.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import eu.ydp.empiria.player.client.BindDescriptor.BindType;
import eu.ydp.empiria.player.client.controller.data.DataSourceDataSupplier;
import eu.ydp.empiria.player.client.controller.data.DataSourceManager;
import eu.ydp.empiria.player.client.controller.feedback.FeedbackParserFactory;
import eu.ydp.empiria.player.client.controller.feedback.FeedbackParserFactoryMock;
import eu.ydp.empiria.player.client.controller.feedback.FeedbackRegistry;
import eu.ydp.empiria.player.client.module.feedback.text.blend.FeedbackBlend;
import eu.ydp.empiria.player.client.controller.feedback.matcher.MatcherRegistry;
import eu.ydp.empiria.player.client.controller.feedback.matcher.MatcherRegistryFactory;
import eu.ydp.empiria.player.client.controller.flow.FlowDataSupplier;
import eu.ydp.empiria.player.client.controller.multiview.PanelCache;
import eu.ydp.empiria.player.client.controller.report.AssessmentReportFactory;
import eu.ydp.empiria.player.client.controller.session.datasupplier.SessionDataSupplier;
import eu.ydp.empiria.player.client.controller.variables.ResultExtractorsFactory;
import eu.ydp.empiria.player.client.controller.variables.processor.OutcomeAccessor;
import eu.ydp.empiria.player.client.controller.workmode.PlayerWorkModeState;
import eu.ydp.empiria.player.client.gin.EmpiriaExListBoxDelay;
import eu.ydp.empiria.player.client.gin.binding.UniqueId;
import eu.ydp.empiria.player.client.gin.factory.*;
import eu.ydp.empiria.player.client.gin.scopes.page.PageScoped;
import eu.ydp.empiria.player.client.media.texttrack.VideoTextTrackElementPresenter;
import eu.ydp.empiria.player.client.controller.item.ResponseSocket;
import eu.ydp.empiria.player.client.module.connection.ConnectionSurface;
import eu.ydp.empiria.player.client.module.connection.presenter.view.ConnectionView;
import eu.ydp.empiria.player.client.module.dragdrop.SourcelistManager;
import eu.ydp.empiria.player.client.module.feedback.image.ImageFeedback;
import eu.ydp.empiria.player.client.module.feedback.text.TextFeedback;
import eu.ydp.empiria.player.client.module.media.MediaControllerFactory;
import eu.ydp.empiria.player.client.module.sourcelist.structure.SourceListJAXBParser;
import eu.ydp.empiria.player.client.module.sourcelist.structure.SourceListJAXBParserMock;
import eu.ydp.empiria.player.client.overlaytypes.OverlayTypesParser;
import eu.ydp.empiria.player.client.overlaytypes.OverlayTypesParserMock;
import eu.ydp.empiria.player.client.style.StyleSocket;
import eu.ydp.empiria.player.client.util.dom.drag.DragDropHelper;
import eu.ydp.empiria.player.client.util.events.internal.bus.EventsBus;
import eu.ydp.empiria.player.client.util.events.internal.bus.PlayerEventsBus;
import eu.ydp.empiria.player.client.util.events.internal.emulate.HasTouchHandlersMock;
import eu.ydp.empiria.player.client.util.position.PositionHelper;
import eu.ydp.empiria.player.client.util.style.NativeStyleHelper;
import eu.ydp.gwtutil.client.components.exlistbox.ExListBoxDelays;
import eu.ydp.gwtutil.client.debug.log.ConsoleAppender;
import eu.ydp.gwtutil.client.debug.log.LogAppender;
import eu.ydp.gwtutil.client.json.NativeMethodInvocator;
import eu.ydp.gwtutil.client.scheduler.Scheduler;
import eu.ydp.gwtutil.client.scheduler.SchedulerMockImpl;
import eu.ydp.gwtutil.client.service.json.IJSONService;
import eu.ydp.gwtutil.client.service.json.NativeJSONService;
import eu.ydp.gwtutil.client.ui.GWTPanelFactory;
import eu.ydp.gwtutil.client.util.UserAgentUtil;
import eu.ydp.gwtutil.client.xml.XMLParser;
import eu.ydp.gwtutil.client.xml.proxy.XMLProxy;
import eu.ydp.gwtutil.client.xml.proxy.XMLProxyFactory;
import eu.ydp.gwtutil.xml.XMLProxyWrapper;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;

public class TestGuiceModule extends ExtendTestGuiceModule {
    private final Set<BindDescriptor<?>> bindDescriptors = new HashSet<>();
    private final GuiceModuleConfiguration moduleConfiguration;

    public TestGuiceModule() {
        moduleConfiguration = new GuiceModuleConfiguration();
    }

    public TestGuiceModule(Class<?>[] classToOmit, Class<?>[] classToMock, Class<?>[] classToSpy) {
        super(classToOmit);
        moduleConfiguration = new GuiceModuleConfiguration();
        moduleConfiguration.addAllClassToOmit(classToOmit);
        moduleConfiguration.addAllClassToMock(classToMock);
        moduleConfiguration.addAllClassToSpy(classToSpy);
    }

    public TestGuiceModule(GuiceModuleConfiguration moduleConfiguration) {
        super(moduleConfiguration.getClassToOmmit());
        this.moduleConfiguration = moduleConfiguration;
    }

    private void init() {
        bindDescriptors.add(new BindDescriptor<Scheduler>().bind(Scheduler.class)
                .to(SchedulerMockImpl.class)
                .in(Singleton.class));
        bindDescriptors.add(new BindDescriptor<ConnectionModuleFactory>().bind(ConnectionModuleFactory.class)
                .to(ConnectionModuleFactoryMock.class)
                .in(Singleton.class));
        bindDescriptors.add(new BindDescriptor<PanelCache>().bind(PanelCache.class));
        bindDescriptors.add(new BindDescriptor<FeedbackParserFactory>().bind(FeedbackParserFactory.class)
                .to(FeedbackParserFactoryMock.class));
    }

    @Override
    public void configure() {
        addPostConstructInterceptor(moduleConfiguration);
        init();
        List<Class<?>> classToMock = moduleConfiguration.getClassToMock();
        List<Class<?>> classToSpy = moduleConfiguration.getClassToSpy();
        for (BindDescriptor<?> bindDescriptor : bindDescriptors) {
            if (classToMock.contains(bindDescriptor.getBind())) {
                bind(bindDescriptor, BindType.MOCK);
            } else if (classToSpy.contains(bindDescriptor.getBind())) {
                bind(bindDescriptor, BindType.SPY);
            } else {
                bind(bindDescriptor, BindType.SIMPLE);
            }
        }
        Scheduler scheduler = new SchedulerMockImpl();
        binder.bind(EventsBus.class).toInstance(spy(new PlayerEventsBus(scheduler)));

        bind(SourceListJAXBParser.class).toInstance(spy(new SourceListJAXBParserMock()));
        bind(MatcherRegistry.class).in(Singleton.class);
        bind(OverlayTypesParser.class).toInstance(mock(OverlayTypesParserMock.class));
        bind(TextFeedback.class).toInstance(mock(TextFeedback.class));
        bind(ImageFeedback.class).toInstance(mock(ImageFeedback.class));
        bind(FeedbackRegistry.class).toInstance(mock(FeedbackRegistry.class));
        bind(XMLProxy.class).to(XMLProxyWrapper.class);
        bind(UserAgentUtil.class).toInstance(mock(UserAgentUtil.class));
        bind(SourcelistManager.class).toInstance(mock(SourcelistManager.class));
        bind(OutcomeAccessor.class).toInstance(mock(OutcomeAccessor.class));
        bind(FlowDataSupplier.class).toInstance(mock(FlowDataSupplier.class));
        bind(SessionDataSupplier.class).toInstance(mock(SessionDataSupplier.class));
        binder.bind(IJSONService.class)
                .to(NativeJSONService.class);
        binder.requestStaticInjection(XMLProxyFactory.class);
        bind(ResponseSocket.class).annotatedWith(PageScoped.class)
                .toInstance(mock(ResponseSocket.class));
        bind(String.class).annotatedWith(UniqueId.class)
                .toInstance("id");
        bind(ExListBoxDelays.class).to(EmpiriaExListBoxDelay.class);
        bind(LogAppender.class).to(ConsoleAppender.class);
        bind(PlayerWorkModeState.class).toInstance(mock(PlayerWorkModeState.class));
        bind(DataSourceDataSupplier.class).toInstance(mock(DataSourceManager.class));
        install(new FactoryModuleBuilder().build(VideoTextTrackElementFactory.class));
        install(new FactoryModuleBuilder().build(PageScopeFactory.class));
        install(new FactoryModuleBuilder().build(TextTrackFactory.class));
        install(new FactoryModuleBuilder().build(MatcherRegistryFactory.class));
        install(new FactoryModuleBuilder().build(AssessmentReportFactory.class));
        install(new FactoryModuleBuilder().build(SingleFeedbackSoundPlayerFactory.class));
        install(new FactoryModuleBuilder().build(ResultExtractorsFactory.class));
        install(new FactoryModuleBuilder().build(FeedbackModuleFactory.class));
        install(new FactoryModuleBuilder().build(MediaFactory.class));
        install(new StyleNameConstantsProvider());
    }

    private void addPostConstructInterceptor(GuiceModuleConfiguration guiceModuleConfiguration) {
        HasPostConstructMethod hasPostConstruct = new HasPostConstructMethod(guiceModuleConfiguration.getClassWithDisabledPostConstruct());
        final PostConstructInvoker postConstructInvoker = new PostConstructInvoker();
        bindListener(hasPostConstruct, new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(postConstructInvoker);
            }
        });
    }


    @Provides
    public MediaControllerFactory getMediaControllerFactory() {
        MediaControllerFactory factory = mock(MediaControllerFactory.class);
        return factory;
    }

    @Provides
    public FeedbackBlend getFeedbackBlend() {
        return mock(FeedbackBlend.class);
    }

    @Provides
    @Singleton
    public VideoTextTrackElementPresenter getVideoTextTrackElementPresenter() {
        return mock(VideoTextTrackElementPresenter.class);
    }

    @Provides
    public GWTPanelFactory gwtPanelFactory() {
        return mock(GWTPanelFactory.class);
    }

    @Provides
    public ConnectionView getConnectionView() {
        return mock(ConnectionView.class);
    }

    @Provides
    @Singleton
    public NativeStyleHelper getNativeStyleHelper() {
        return mock(NativeStyleHelper.class);
    }

    @Provides
    @Singleton
    public StyleSocket getStyleSocket() {
        return mock(StyleSocket.class);
    }

    @Provides
    @Singleton
    public XMLParser getXmlParser() {
        XMLParser parser = mock(XMLParser.class);
        when(parser.parse(Matchers.anyString())).then(new Answer<Document>() {
            @Override
            public Document answer(InvocationOnMock invocation) throws Throwable {
                return eu.ydp.gwtutil.xml.XMLParser.parse((String) invocation.getArguments()[0]);
            }
        });

        when(parser.createDocument()).then(new Answer<Document>() {
            @Override
            public Document answer(InvocationOnMock invocation) throws Throwable {
                return eu.ydp.gwtutil.xml.XMLParser.createDocument();
            }
        });
        return parser;
    }

    @Provides
    @Singleton
    public TouchRecognitionFactory getTouchRecognitionFactory() {
        TouchRecognitionFactory factory = mock(TouchRecognitionFactory.class);
        when(factory.getTouchRecognition(Matchers.any(Widget.class), Matchers.anyBoolean())).thenReturn(spy(new HasTouchHandlersMock()));
        return factory;
    }

    @Provides
    @Singleton
    public PositionHelper getPositionHelper() {
        PositionHelper helper = mock(PositionHelper.class);
        when(helper.getXPositionRelativeToTarget(Matchers.any(NativeEvent.class), Matchers.any(Element.class))).thenReturn(20);
        when(helper.getYPositionRelativeToTarget(Matchers.any(NativeEvent.class), Matchers.any(Element.class))).thenReturn(20);
        return helper;
    }

    @Provides
    DragDropHelper getDragDropHelper() {
        return mock(DragDropHelper.class);
    }

    @Provides
    ConnectionSurface getConnectionSurface() {
        return mock(ConnectionSurface.class);
    }

    @Provides
    @Singleton
    NativeMethodInvocator getMethodInvocator() {
        NativeMethodInvocator invocator = mock(NativeMethodInvocator.class);
        return invocator;
    }
}
