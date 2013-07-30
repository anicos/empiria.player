package eu.ydp.empiria.player.client.module.tutor.actions.popup;


import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import eu.ydp.empiria.player.client.controller.extensions.internal.tutor.TutorPersonaProperties;
import eu.ydp.empiria.player.client.resources.EmpiriaPaths;
import eu.ydp.empiria.player.client.util.geom.Size;

public class PersonaToViewDtoConverterTest {

	private PersonaToViewDtoConverter converter;
	private EmpiriaPaths empiriaPaths;
	
	
	@Before
	public void setUp() {
		empiriaPaths = mock(EmpiriaPaths.class);
		converter = new PersonaToViewDtoConverter(empiriaPaths);
	}
	
	@Test
	public void testConvert() throws Exception {
		//given
		int firstIndex = 3;
		String firstAvatarFileName = "firstAcatarFileName";
		int secondIndex = 3;
		String secondAvatarFileName = "secondAvatarFileName";
		
		TutorPersonaProperties firstPersonaProperties = createPersonaProperties(firstIndex, firstAvatarFileName);
		TutorPersonaProperties secondPersonaProperties = createPersonaProperties(secondIndex, secondAvatarFileName);
		List<TutorPersonaProperties> personasProperties = Lists.newArrayList(firstPersonaProperties, secondPersonaProperties);
		
		String firstAvatarFileFullPath = "firstAvatarFileFullPath";
		when(empiriaPaths.getCommonsFilePath(firstAvatarFileName))
			.thenReturn(firstAvatarFileFullPath);
		
		String secondAvatarFileFullPath = "secondAvatarFileFullPath";
		when(empiriaPaths.getCommonsFilePath(secondAvatarFileName))
		.thenReturn(secondAvatarFileFullPath);
		
		//when
		List<PersonaViewDto> createPersonasDtos = converter.convert(personasProperties);
		
		
		//then
		assertEquals(createPersonasDtos.get(0).getPersonaIndex(), firstIndex);
		assertEquals(createPersonasDtos.get(0).getAvatarUrl(), firstAvatarFileFullPath);
		assertEquals(createPersonasDtos.get(1).getPersonaIndex(), secondIndex);
		assertEquals(createPersonasDtos.get(1).getAvatarUrl(), secondAvatarFileFullPath);
		
		assertEquals(2, createPersonasDtos.size());
	}
	
	private TutorPersonaProperties createPersonaProperties(int index, String avatarFileName) {
		return new TutorPersonaProperties(index, new Size(), 60, "name", false, avatarFileName);
	}
}
